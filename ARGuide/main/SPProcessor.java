package IP_ThirdSprint;

import java.sql.SQLException;
import java.util.List;

public class SPProcessor {
    private PathGenerator pg;

    public List<Integer> generateSP(String request,Integer start,Integer destination)throws SPException{
        if(start<0 || destination<0){
            throws new SPException("Invalid parameter value");
        }else{
            switch(request){
                case "classicSP":{
                    List<Integer> returnValue=pg.dijkstra(start,destination);
                    if(returnValue==null){
                        throws new SPException("Null Pointer Return from disjkstra function");
                    }
                    return returnValue;
                }
                default:{
                    return null;
                }
            }

        }
    }
    public SPProcessor()throws SQLException {
        this.pg=new PathGenerator();

    }
}


