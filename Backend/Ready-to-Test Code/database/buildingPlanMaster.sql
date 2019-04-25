SET SERVEROUTPUT ON;

CREATE OR REPLACE PACKAGE BuildingPlanMaster AS  

  v_currDir VARCHAR2(50);
  PROCEDURE createDirectory (p_dirName VARCHAR2, p_dir VARCHAR2);
  PROCEDURE storeBuildingPlan (p_filePath VARCHAR2, p_fileName VARCHAR2);
  PROCEDURE storeBuildingPlanAux (l_jsonString CLOB);
  PROCEDURE removeBuildingPlan;
  PROCEDURE updateBuildingPlan (p_newFilePath VARCHAR2, p_newFileName VARCHAR2);
END BuildingPlanMaster;
/

CREATE OR REPLACE PACKAGE BODY BuildingPlanMaster AS

  PROCEDURE createDirectory (p_dirName VARCHAR2, p_dir VARCHAR2) AS
  v_creationQuery VARCHAR2(500);
  BEGIN
    /*
     * also set package's 'v_currDir' var. to 'p_dirName' so that we
     * can use it to know where to extract the schedule from
     */
    v_currDir := p_dirName;
    v_creationQuery := 'CREATE OR REPLACE DIRECTORY ' || p_dirName ||
      ' AS ''' || p_dir || '''';
    EXECUTE IMMEDIATE (v_creationQuery);
    
    DBMS_OUTPUT.PUT_LINE('IMPORTANT NOTICE: You must execute ' ||
      'the following command as an administrator (e.g SYS) for ' ||
      'the storeSchedule() method to successfully complete its task!');
    DBMS_OUTPUT.PUT_LINE('GRANT READ ON DIRECTORY ' ||
      p_dirName || ' TO <yourUsername>;');
  END createDirectory;


  PROCEDURE storeBuildingPlan (p_filePath VARCHAR2, p_fileName VARCHAR2) AS
  /*
   * file handler for reading an external large object
   */
  l_fileHandler BFILE;
  /*
   * character large object (CLOB) used to store the JSON string
   */
  l_jsonString CLOB;
  
  l_fileHandlerOffset INTEGER := 1;
  l_jsonStringOffset INTEGER := 1;
  l_bfile_csid NUMBER := DBMS_LOB.DEFAULT_CSID;
  l_lang_ctx NUMBER := DBMS_LOB.DEFAULT_LANG_CTX;
  l_warning NUMBER;
  BEGIN
    /*
     * create dir. var. corresponding to the given path
     */
    createDirectory('BUILDING_PLAN_SOURCE_DIR', p_filePath);
  
    /*
     * Open, read and close the JSON file
     */
    l_fileHandler := BFileName(v_currDir, p_fileName);
    DBMS_LOB.OPEN(l_fileHandler, DBMS_LOB.LOB_READONLY);
    
    DBMS_LOB.CREATETEMPORARY(l_jsonString, true);
          
    DBMS_LOB.LoadCLOBFromFile(l_jsonString, l_fileHandler, 
      DBMS_LOB.getLength(l_fileHandler), l_fileHandlerOffset, l_jsonStringOffset,
        l_bfile_csid, l_lang_ctx, l_warning);
    
    DBMS_LOB.CLOSE(l_fileHandler);
    
     --store the building plan

    storeBuildingPlanAux(l_jsonString);
    
    DBMS_OUTPUT.PUT_LINE('');
  END storeBuildingPlan;

  PROCEDURE storeBuildingPlanAux (l_jsonString CLOB) AS
  j_emptyObject JSON := JSON('{}');
  
  j_string JSON;
  
  j_nodesList JSON_LIST;
  j_node JSON;
  
  j_name JSON_VALUE;
  j_floor JSON_VALUE;
  j_type JSON_VALUE;
  j_id JSON_VALUE;
  
  j_edgesList JSON_LIST;
  j_edge JSON;
  
  j_id1 JSON_VALUE;
  j_id2 JSON_VALUE;
  j_cost JSON_VALUE;
  
  v_id NUMBER;
  BEGIN
    j_string := JSON(l_jsonString);
    j_nodesList := JSON_LIST(j_string.get('nodes'));
    j_edgesList := JSON_LIST(j_string.get('edges'));
    
    FOR i IN 1 .. j_nodesList.count LOOP
      j_node := JSON(j_nodesList.get(i));
      
      j_name := j_node.get('name');
      j_id:=j_node.get('id');
      j_floor:=j_node.get('floor');
      j_type:=j_node.get('type');
         
      -- insert new node
      
      INSERT INTO nodes VALUES(j_id.get_string, j_floor.get_string, j_name.get_string, j_type.get_string);
          COMMIT;
    END LOOP; -- end of nodes list loop
    
    for v_id in 1 ..j_edgesList.count LOOP
      j_edge:=JSON(j_edgesList.get(v_id));
      
      j_id1:=j_edge.get('id_node1');
      j_id1:=j_edge.get('id_node1');
      j_cost:=j_edge.get('cost');
    
      -- insert new edge
      
      INSERT INTO edges VALUES(v_id, j_id1.get_string, j_id2.get_string, j_cost.get_string);
        COMMIT;

    END LOOP;--end of edges loop
    
  END storeBuildingPlanAux;



  PROCEDURE removeBuildingPlan AS
  v_truncScheduleQuery VARCHAR(100);
  BEGIN
    v_truncScheduleQuery := 'TRUNCATE TABLE edges';
  
    EXECUTE IMMEDIATE (v_truncScheduleQuery);
    
    v_truncScheduleQuery := 'TRUNCATE TABLE nodes';
  
    EXECUTE IMMEDIATE (v_truncScheduleQuery);
  END removeBuildingPlan;
  
  PROCEDURE updateBuildingPlan (p_newFilePath VARCHAR2, p_newFileName VARCHAR2) AS
  BEGIN
    removeBuildingPlan();
    storeBuildingPlan(p_newFilePath, p_newFileName);
  END;
END BuildingPlanMaster;
/

/*
 * example of 'ScheduleMaster' package usage
 */
/*
BEGIN
  ScheduleMaster.storeBuildingPlan('C:\Users\User\Desktop\Uni\ip\proj\Software-Engineering-E2\buildingPlan', 'buildingPlan.json');
  ScheduleMaster.removeBuildingPlan();
  ScheduleMaster.updateBuildingPlan('C:\Users\User\Desktop\Uni\ip\proj\Software-Engineering-E2\buildingPlan', 'buildingPlan.json');
END;
/
*/

/*
 * the following are the modified tables
 */
/*
SELECT * FROM nodes;
SELECT * FROM courses;
SELECT * FROM schedule;
*/

SET SERVEROUTPUT OFF;