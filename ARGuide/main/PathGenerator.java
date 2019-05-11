package main;

import java.sql.*;
import java.util.*;
import javafx.util.Pair;

/**
 * Computes the shortes path between two nodes (the distance itself, as well as the path)
 * @author
 *
 */

public class PathGenerator {

    private String dbPath = "../database/faculty.db";

    private String URL = "jdbc:sqlite:" + dbPath;

    Connection connection=null;

    private List<Integer> nodes=new ArrayList<>();
    private List<Integer> path=new ArrayList<>();
    int[] previous;
    private LinkedList<Edge>[] adjacencylist;
    public int vertices;


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
                id1 = rs.getInt(1);
                id2 = rs.getInt(2);
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
             ResultSet rs = stmt.executeQuery("select * from nodes;"))
        {
            while(rs.next())
            {
                nodes.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> dijkstra(int sourceVertex, int destinationVertex) {

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
            int extractedVertex = extractedPair.getValue();
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

        path.add(destinationVertex);
        int i=destinationVertex;
        while(i!=sourceVertex)
        {
            i=previous[i];
            path.add(i);
        }
        Collections.reverse(path);

        return path;
    }
}