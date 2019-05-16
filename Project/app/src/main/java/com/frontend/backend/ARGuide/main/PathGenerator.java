package com.frontend.backend.ARGuide.main;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.*;

import org.javatuples.Pair;

/**
 * Computes the shortest path between two nodes (the distance itself, as well as the path)
 * @author Ioana-Balan
 *
 */

public class PathGenerator {
    private List<Integer> nodes=new ArrayList<>();
    private List<Integer> path=new ArrayList<>();
    private int[] previous;
    private LinkedList<Edge>[] adjacencylist;
    private int vertices;
    private SQLiteDatabase db;


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

    public PathGenerator(DatabaseEmissary dbEmissary) {
        this.db = dbEmissary.getReadableDatabase();

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

        Cursor rs = db.rawQuery("SELECT id1, id2, cost FROM edges", null);

        rs.moveToFirst();

        //getting the source, destination and cost of each edge
        while (!rs.isAfterLast()) {
            id1 = rs.getInt(0) - 1;
            id2 = rs.getInt(1) - 1;
            cost = rs.getDouble(2);
            Edge edge = new Edge(id1, id2, cost);
            adjacencylist[id1].addFirst(edge);
            rs.moveToNext();
        }

        rs.close();
    }

    public void getNodes() {
        Cursor rs = db.rawQuery("SELECT * FROM nodes", null);

        rs.moveToFirst();

        //getting the source, destination and cost of each edge
        while (!rs.isAfterLast()) {
            nodes.add(rs.getInt(0));
            rs.moveToNext();
        }

        rs.close();
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
            if (!SPT[extractedVertex]) {
                SPT[extractedVertex] = true;

                //iterate through all the adjacent vertices and update the keys
                LinkedList<Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    Edge edge = list.get(i);
                    int destination = edge.id2;
                    //only if edge destination is not present in mst
                    if (!SPT[destination]) {
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
}