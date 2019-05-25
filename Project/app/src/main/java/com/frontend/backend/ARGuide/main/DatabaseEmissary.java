package com.frontend.backend.ARGuide.main;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the main class whose instance will hold the current connection to the database and be able to perform certain tasks related to it
 * @author Paul-Reftu
 *
 */
public class DatabaseEmissary extends SQLiteOpenHelper {
    private final String DB_PATH;
    private final String DB_TYPE;

    /**
     * construct the instance w.r.t this class, given the database context, the path to the database and its type
     * @param dbContext the context of the db to be instantiated
     * @param dbPath the path to the database
     * @param dbType the type of the database to be created (i.e, a standard one or a specific type that requires
     *               a particular schema like the Faculty of Computer Science of "Alexandru Ioan Cuza" University)
     */
    public DatabaseEmissary(Context dbContext, String dbPath, String dbType) {
        super(dbContext, dbPath, null, 1);
        this.DB_PATH = dbPath;
        this.DB_TYPE = dbType;
    }

    /**
     * creates the database and the tables based on the required schema for the current particular DB_TYPE
     * @param db the database object itself
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        switch (DB_TYPE) {
            /*
             * creation of DB for the Faculty of Computer Science, within the University "Alexandru Ioan Cuza" of Iasi
             */
            case "faculty_uaic_cs": {
                List<String> queries = new ArrayList<>(Arrays.asList(
                        "DROP TABLE IF EXISTS schedule\r\n",
                        "DROP TABLE IF EXISTS edges\r\n",
                        "DROP TABLE IF EXISTS images\r\n",
                        "DROP TABLE IF EXISTS nodes\r\n",
                        "DROP TABLE IF EXISTS courses\r\n",

                        "CREATE TABLE nodes (\r\n" +
                                "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" +
                                "  floor INTEGER,\r\n" +
                                "  name VARCHAR(50),\r\n" +
                                "  type VARCHAR(15),\r\n" +
                                "  lat DOUBLE,\r\n" +
                                "  lon DOUBLE\r\n" +
                                ")\r\n",

                        "CREATE TABLE edges (\r\n" +
                                "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" +
                                "  id1 INTEGER NOT NULL,\r\n" +
                                "  id2 INTEGER NOT NULL,\r\n" +
                                "  cost DOUBLE PRECISION,\r\n" +
                                "  CONSTRAINT fk_edges_nodes1 FOREIGN KEY (id1) REFERENCES nodes(id),\r\n" +
                                "  CONSTRAINT fk_edges_nodes2 FOREIGN KEY (id2) REFERENCES nodes(id)\r\n" +
                                ")\r\n",

                        "CREATE TABLE images (\r\n" +
                                "  node_id INTEGER NOT NULL,\r\n" +
                                "  image VARCHAR(100),\r\n" +
                                "   CONSTRAINT fk_edges_nodes FOREIGN KEY (node_id) REFERENCES nodes(id)\r\n" +
                                ")\r\n",

                        "CREATE TABLE courses (\r\n" +
                                "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" +
                                "  name VARCHAR2(60) NOT NULL,\r\n" +
                                "  studyGroup VARCHAR2(100)\r\n" +
                                ")\r\n",

                        "CREATE TABLE schedule (\r\n" +
                                "  node_id INTEGER not null,\r\n" +
                                "  course_id INTEGER not null,\r\n" +
                                "  starting_time VARCHAR2(20),\r\n" +
                                "  ending_time VARCHAR2(20),\r\n" +
                                "  day VARCHAR(10),\r\n" +
                                "  CONSTRAINT fk_schedule_nodes FOREIGN KEY (node_id) REFERENCES nodes(id),\r\n" +
                                "  CONSTRAINT fk_schedule_courses FOREIGN KEY (course_id) REFERENCES courses(id)\r\n" +
                                ")"));

                for (String query : queries)
                    db.execSQL(query);
                break;
            }
            /*
             * standard DB creation (three standard tables - 'nodes', 'edges' and 'images')
             */
            default: {
                List<String> queries = new ArrayList<>(Arrays.asList(
                        "DROP TABLE IF EXISTS edges\r\n",
                        "DROP TABLE IF EXISTS images\r\n",
                        "DROP TABLE IF EXISTS nodes\r\n",

                        "CREATE TABLE nodes (\r\n" +
                                "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" +
                                "  floor INTEGER,\r\n" +
                                "  name VARCHAR(50),\r\n" +
                                "  type VARCHAR(15),\r\n" +
                                "  lat DOUBLE,\r\n" +
                                "  lon DOUBLE\r\n" +
                                ")\r\n",

                        "CREATE TABLE edges (\r\n" +
                                "  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" +
                                "  id1 INTEGER NOT NULL,\r\n" +
                                "  id2 INTEGER NOT NULL,\r\n" +
                                "  cost DOUBLE PRECISION,\r\n" +
                                "  CONSTRAINT fk_edges_nodes1 FOREIGN KEY (id1) REFERENCES nodes(id),\r\n" +
                                "  CONSTRAINT fk_edges_nodes2 FOREIGN KEY (id2) REFERENCES nodes(id)\r\n" +
                                ")\r\n",

                        "CREATE TABLE images (\r\n" +
                                "  node_id INTEGER NOT NULL,\r\n" +
                                "  image VARCHAR(100),\r\n" +
                                "   CONSTRAINT fk_edges_nodes FOREIGN KEY (node_id) REFERENCES nodes(id)\r\n" +
                                ")"

                ));

                for (String query : queries)
                    db.execSQL(query);
                break;
            }
        }
    }

    /**
     * upgrades the previous database to a new version
     * @param db the database in question
     * @param oldVersion the old version of the given database
     * @param newVersion the new version of the given database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (DB_TYPE) {
            /*
             * the upgrade process of a database destined for the Faculty of Computer Science,
             * within the University "Alexandru Ioan Cuza" of Iasi
             */
            case "faculty_uaic_cs": {
                List<String> queries = new ArrayList<>(Arrays.asList(
                        "DROP TABLE IF EXISTS schedule\r\n",
                        "DROP TABLE IF EXISTS edges\r\n",
                        "DROP TABLE IF EXISTS images\r\n",
                        "DROP TABLE IF EXISTS nodes\r\n",
                        "DROP TABLE IF EXISTS courses\r\n"
                ));

                for (String query : queries)
                    db.execSQL(query);

                onCreate(db);

                break;
            }
            /*
             * the database upgrade process for a standard building (the standard tables are dropped)
             */
            default: {
                List<String> queries = new ArrayList<>(Arrays.asList(
                        "DROP TABLE IF EXISTS edges\r\n",
                        "DROP TABLE IF EXISTS images\r\n",
                        "DROP TABLE IF EXISTS nodes\r\n"
                ));

                for (String query : queries)
                    db.execSQL(query);

                onCreate(db);

                break;
            }
        }
    }

    /**
     * select all classroom names in our database
     * @return the list of results w.r.t the query (i.e, all classrooms)
     */
    public List<String> selectAllClassroomNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> queryResults = new ArrayList<>();
        Cursor rs = db.rawQuery("SELECT name FROM nodes WHERE type IN ('Classroom', 'Amphitheatre')", null);

        if (rs.getCount() == 0)
            return null;

        rs.moveToFirst();

        while (!rs.isAfterLast()) {
            queryResults.add(rs.getString(0));
            rs.moveToNext();
        }

        rs.close();

        return queryResults;
    }

    /**
     * select all schedule entries related to the given classroom name (could return null in case that specific classroom does not exist in our DB)
     * @param classroomName the name of the classroom whose schedule should be returned
     * @return the list of results w.r.t the query (i.e, a set of tuples of the form (day, starting_time, ending_time, course_name), which are each stored in their own list)
     */
    public List<List<String>> selectClassroomSchedule(String classroomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<List<String>> queryResults = new ArrayList<>();

        Cursor rs = db.rawQuery(
                "SELECT s.day, s.starting_time, s.ending_time, c.name AS course_name " +
                        "FROM nodes n JOIN schedule s ON n.id=s.node_id " +
                        "JOIN courses c ON s.course_id=c.id " +
                        "WHERE n.type IN ('Classroom', 'Amphitheatre') AND n.name = ? " +
                        "ORDER BY CASE " +
                        "WHEN s.day = 'LUNI' THEN 1 " +
                        "WHEN s.day = 'MARTI' THEN 2 " +
                        "WHEN s.day = 'MIERCURI' THEN 3 " +
                        "WHEN s.day = 'JOI' THEN 4 " +
                        "WHEN s.day = 'VINERI' THEN 5 " +
                        "WHEN s.day = 'SAMBATA' THEN 6 " +
                        "WHEN s.day = 'DUMINICA' THEN 7 " +
                        "END ASC",
                new String[] {classroomName}
        );

        if (rs.getCount() == 0)
            return null;

        rs.moveToFirst();

        while (!rs.isAfterLast()) {
            List<String> resultEntry = new ArrayList<>(Arrays.asList(rs.getString(0),
                    rs.getString(1), rs.getString(2), rs.getString(3)));

            queryResults.add(resultEntry);
            rs.moveToNext();
        }

        rs.close();

        return queryResults;
    }

    /**
     * select a classroom's unique id by its name
     * @param classroomName the name of the classroom
     * @return the id of the classroom
     */
    public Integer selectClassroomIdByName(String classroomName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT id FROM nodes WHERE type IN ('Classroom', 'Amphitheatre') AND name=?", new String[] {classroomName});

        if (rs.getCount() == 0)
            return null;

        rs.moveToFirst();

        return rs.getInt(0);
    }

    /**
     * select a classroom's name by its unique id
     * @param id the id of the classroom
     * @return the name of the classroom
     */
    public String selectClassroomNameById(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT name FROM nodes WHERE type IN ('Classroom', 'Amphitheatre') AND id=?", new String[] {String.valueOf(id)});

        if (rs.getCount() == 0)
            return null;

        rs.moveToFirst();

        return rs.getString(0);
    }

    /**
     * checks whether the database at the currently-declared path exists
     * @return true if the database specified by the current path exists; false otherwise
     */
    public boolean doesDbExist() {
        File f = new File(DB_PATH);

        return f.exists() && !f.isDirectory();
    }

    /**
     * checks whether the given tables exist in our database
     * @param tableNameList the list of the names of the tables to check whether they exist in our DB or not
     * @return true if given tables exist, false otherwise
     */
    public boolean doDbTablesExist(List<String> tableNameList) {
        SQLiteDatabase db = this.getReadableDatabase();

        for (String tableName : tableNameList) {
            Cursor rs = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name= ?", new String[] {tableName});

            if (rs.getCount() == 0)
                return false;

            rs.close();
        }

        return true;
    }

    /**
     * checks whether the given tables are empty
     * @param tableNameList the list of the names of the tables to check whether they are filled w/ information
     * @return true if the tables are filled, false otherwise
     */
    public boolean areDbTablesFilled(List<String> tableNameList) {
        SQLiteDatabase db = this.getReadableDatabase();

        for (String tableName : tableNameList) {
            /*
             * for the moment, we have no images stored
             */
            if (tableName.equals("images"))
                continue;

            Cursor rs = db.query(tableName, new String[] {"COUNT(*)"},
                    null, null,
                    null, null, null);

            rs.moveToFirst();

            if (rs.getInt(0) == 0) {
                rs.close();
                return false;
            }

            rs.close();
        }

        return true;
    }

    /**
     * selects the names of all nodes in our database
     * @return the list w/ the names of all nodes in the DB
     */
    public List<String> selectAllNodes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> queryResults = new ArrayList<>();
        Cursor rs = db.rawQuery("SELECT name FROM nodes", null);

        if (rs.getCount() == 0)
            return null;

        rs.moveToFirst();

        while (!rs.isAfterLast()) {
            queryResults.add(rs.getString(0));
            rs.moveToNext();
        }

        rs.close();

        return queryResults;
    }

    /**
     * select the (latitude, longitude) tuple w.r.t the node identified by given id
     * @param nodeId the id of the node to search for
     * @return the (lat, lon) tuple of the node in question
     * @throws DbEmissaryException upon the given node id not existing in our database
     */
    public Pair<Double, Double> selectLatLonById(Integer nodeId) throws DbEmissaryException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor rs = db.rawQuery(
                "SELECT lat, lon FROM nodes " +
                        "WHERE id=?",

                new String[] {String.valueOf(nodeId)}
        );

        if (rs.getCount() <= 0)
            throw new DbEmissaryException("Given node id does not seem to exist in our database!");

        rs.moveToFirst();

        Pair<Double, Double> nodeCoord = new Pair<>(rs.getDouble(0), rs.getDouble(1));

        rs.close();

        return nodeCoord;
    }

    /**
     * select the type of a node by its unique id
     * @param nodeId the unique id of the node
     * @return the type of the node identified by the given id
     * @throws DbEmissaryException upon the given node id not existing in our database
     */
    public String selectNodeTypeById(Integer nodeId) throws DbEmissaryException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor rs = db.rawQuery(
                "SELECT type FROM nodes " +
                        "WHERE id=?",
                new String[] {String.valueOf(nodeId)}
        );

        if (rs.getCount() <= 0)
            throw new DbEmissaryException("Given node id does not seem to exist in our database!");

        rs.moveToFirst();

        String nodeType = rs.getString(0);

        rs.close();

        return nodeType;
    }

    /**
     * select the floor of a node by its unique id
     * @param nodeId the unique id of the node
     * @return the floor number of the node identified by the given id
     * @throws DbEmissaryException upon the given node id not existing in our database
     */
    public Integer selectNodeFloorById(Integer nodeId) throws DbEmissaryException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor rs = db.rawQuery(
                "SELECT floor FROM nodes " +
                        "WHERE id=?",
                new String[] {String.valueOf(nodeId)}
        );

        if (rs.getCount() <= 0)
            throw new DbEmissaryException("Given node id does not seem to exist in our database!");

        rs.moveToFirst();

        Integer nodeFloor = rs.getInt(0);

        rs.close();

        return nodeFloor;
    }
}
