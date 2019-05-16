package main;

import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.sql.*;
import java.util.*;

import org.javatuples.Pair;


/**
 * Computes the shortest path between two nodes (the distance itself, as well as the path)
 * @author
 *
 */

public class PathGenerator {

     private String dbPath = "../database/faculty.db";

    private String URL = "jdbc:sqlite:" + dbPath;

    private Connection connection=null;

    private String query="select * from nodes where id=";
    private List<Integer> nodes=new ArrayList<>();
    private List<Integer> path=new ArrayList<>();
    private int[] previous;
    private LinkedList<Edge>[] adjacencylist;
    private int vertices;
    private HashMap<Number, Integer> roomsPerFloor = new HashMap<Number,Integer>();
    private HashMap<Number, Integer> occupiedRoomsPerFloor = new HashMap<Number,Integer>();
    private HashMap<Number, Integer> getFloor = new HashMap<Number,Integer>();
    private List<Number> occupiedFloors;


    static class Edge {
        int id1;
        int id2;
        double cost;
        Edge(int id1, int id2,double cost)
        {
            this.id1=id1;
            this.id2=id2;
            this.cost=cost;
        }
    }

    public PathGenerator() throws SQLException {

        connection = DriverManager.getConnection(URL);

        getNodes();

        vertices = nodes.size();
        adjacencylist = new LinkedList[vertices];

        getAdjList();

        //array of previous nodes (used to return the path from destination to source)
        previous = new int[vertices];

    }

    private void getAdjList() {

        //initialize adjacency lists for all the  nodes
        for (int i = 0; i <vertices; i++)
            adjacencylist[i] = new LinkedList();


        //creating the adjacency list
        int id1, id2;
        double cost;

        //getting the source, destination and cost of each edge
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select id1,id2,cost from edges;")) {
            while (rs.next()) {
                id1 = rs.getInt(1)-1;
                id2 = rs.getInt(2)-1;
                cost = rs.getDouble(3);
                Edge edge = new Edge(id1, id2, cost);
                adjacencylist[id1].addFirst(edge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getNodes()
    {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select id from nodes;"))
        {
            while(rs.next())
            {
                nodes.add(rs.getInt(1)-1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> dijkstra(int sourceVertex, int destinationVertex) {

        destinationVertex--;
        sourceVertex--;
        boolean[] SPT = new boolean[vertices];
        //distance used to store the distance of vertex from a source
        int[] distance = new int[vertices];

        //Initialize all the distance to infinity
        for (int i = 0; i < vertices; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        //Initialize priority queue
        //override the comparator to do the sorting based keys
        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(vertices, new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                //sort using distance values
                int key1 = p1.getValue0();
                int key2 = p2.getValue0();
                return key1 - key2;
            }
        });
        //create the pair for for the source index
        distance[sourceVertex] = 0;
        Pair<Integer, Integer> p0 = new Pair<>(distance[sourceVertex], sourceVertex);
        //add it to pq
        pq.offer(p0);

        //while priority queue is not empty
        while (!pq.isEmpty()) {
            //extract the min
            Pair<Integer, Integer> extractedPair = pq.poll();

            //extracted vertex
            int extractedVertex = extractedPair.getValue1();
            if (SPT[extractedVertex] == false) {
                SPT[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    int destination = edge.id2;
                    //only if edge destination is not present in mst
                    if (SPT[destination] == false) {
                        ///check if distance needs an update or not
                        //means check total weight from source to vertex_V is less than
                        //the current distance value, if yes then update the distance
                        int newKey = distance[extractedVertex] + (int) edge.cost;
                        int currentKey = distance[destination];
                        if (currentKey > newKey) {
                            Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                            pq.offer(p);
                            distance[destination] = newKey;
                            previous[edge.id2]=edge.id1;
                        }
                    }
                }
            }
        }
        //compute actual path

        path.add(destinationVertex+1);
        int i=destinationVertex;
        while(i!=sourceVertex)
        {
            i=previous[i];
            path.add(i+1);
        }
        Collections.reverse(path);

        return path;
    }


 public void getRoomFloors() throws SQLException
    {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select id, floor from nodes;"))
        {
            while(rs.next())
            {
                getFloor.put(rs.getDouble(1)-1, rs.getInt(2));
            }
        }
    }

    public void getOccupiedFloors()
    {
        String currentMinute, currentHour,currentDay="",scheduleDay, startingTimeHour;
        Integer floor;
        SimpleDateFormat sdf = new SimpleDateFormat("hh");
        currentHour = sdf.format(new Date());
        sdf = new SimpleDateFormat("mm");
        currentMinute = sdf.format(new Date());

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                currentDay="DUMINICA";
                break;
            case Calendar.MONDAY:
                currentDay="LUNI";
                break;
            case Calendar.TUESDAY:
                currentDay="MARTI";
                break;
            case Calendar.WEDNESDAY:
                currentDay="MIERCURI";
                break;
            case Calendar.THURSDAY:
                currentDay="JOI";
                break;
            case Calendar.FRIDAY:
                currentDay="VINERI";
                break;
            case Calendar.SATURDAY:
                currentDay="SAMBATA";
                break;
        }
        //currentHour="11";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("select floor,day,substr(starting_time,1,2) from nodes natural join schedule;"))
        {
            while(rs.next())
            {
                floor=rs.getInt(1);
                scheduleDay=rs.getString(2);
                startingTimeHour=rs.getString(3);
                if(scheduleDay.equals(currentDay))
                {
                    if((startingTimeHour.equals(currentHour) && Integer.parseInt(currentMinute)<=15)||(Integer.parseInt(startingTimeHour)==Integer.parseInt(currentHour)-1) && Integer.parseInt(currentMinute)>=15)
                    {
                        if (occupiedRoomsPerFloor.get(floor) != null) {
                            int i = occupiedRoomsPerFloor.get(floor) + 1;
                            occupiedRoomsPerFloor.put(floor, i);
                        } else
                            occupiedRoomsPerFloor.put(floor, 1);
                        occupiedRoomsPerFloor.get(floor);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (Number i : roomsPerFloor.keySet())
        {
            if((roomsPerFloor.get(i)/2)==occupiedRoomsPerFloor.get(i))
                if(!occupiedFloors.contains(i))
                    occupiedFloors.add(i);
        }

    }

 public List<Integer> dijkstra_activity_level(int sourceVertex, int destinationVertex) throws SQLException {

        destinationVertex--;
        sourceVertex--;
        getOccupiedFloors();
        getRoomFloors();
        boolean[] SPT = new boolean[vertices];
        //distance used to store the distance of vertex from a source
        int[] distance = new int[vertices];

        //Initialize all the distance to infinity
        for (int i = 0; i < vertices; i++) {
            distance[i] = Integer.MAX_VALUE;
        }
        //Initialize priority queue
        //override the comparator to do the sorting based keys
        PriorityQueue<Pair<Integer, Integer>> pq = new PriorityQueue<>(vertices, new Comparator<Pair<Integer, Integer>>() {
            @Override
            public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                //sort using distance values
                int key1 = p1.getKey();
                int key2 = p2.getKey();
                return key1 - key2;
            }
        });
        //create the pair for for the first index, 0 distance 0 index
        distance[sourceVertex] = 0;
        Pair<Integer, Integer> p0 = new Pair<>(distance[sourceVertex], sourceVertex);
        //add it to pq
        pq.offer(p0);

        //while priority queue is not empty
        while (!pq.isEmpty()) {
            //extract the min
            Pair<Integer, Integer> extractedPair = pq.poll();

            //extracted vertex
            int extractedVertex = extractedPair.getValue();
            if (SPT[extractedVertex] == false) {
                SPT[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    int destination = edge.id2;
                    Number currentFloor=getFloor.get(destination);
                    //only if edge destination is not present in mst
                    if (SPT[destination] == false) {
                        ///check if distance needs an update or not
                        //means check total weight from source to vertex_V is less than
                        //the current distance value, if yes then update the distance
                        int newKey = distance[extractedVertex] + (int) edge.cost;
                        int currentKey = distance[destination];
                        if (currentKey > newKey && !occupiedFloors.contains(currentFloor))
                        {
                            Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                            pq.offer(p);
                            distance[destination] = newKey;
                            previous[edge.id2]=edge.id1;
                        }
                    }
                }
            }
        }
        //compute actual path

        path.add(destinationVertex+1);
        int i=destinationVertex;
        while(sourceVertex!=i)
        {
            i=previous[i];
            path.add(i+1);
        }
        Collections.reverse(path);

        for(int j=0;j<path.size();j++)
            System.out.print(path.get(j)+"+");
        return path;
    }


}