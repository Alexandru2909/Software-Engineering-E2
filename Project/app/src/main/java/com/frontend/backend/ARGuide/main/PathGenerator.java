package com.frontend.backend.ARGuide.main;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.SimpleDateFormat;

import java.util.LinkedList;
import java.util.List;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Vector;
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
    private Vector<Integer> adj[];
    //private Vector<Integer>[][] adj = new Vector<Integer>[][];
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


    public boolean isFree(int classId){

        String classSTHour;
        String classENDHour;
        Cursor rs = db.rawQuery("SELECT starting_time,ending_time FROM schedules WHERE node_id= "+classId+" ", null);

        rs.moveToFirst();
        classSTHour=rs.getString(0);
        classENDHour=rs.getString(1);

        Calendar cal=Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH");
        String hour=sdf.format(cal.getTime());

        if(classSTHour.contains(hour)){
            return false;
        }else {
            if(classENDHour.contains(hour)){
                return false;
            }
            else{
                return true;
            }
        }
    }

    public boolean isClassOrAmph(int roomId){
        Cursor rs = db.rawQuery("SELECT type FROM nodes WHERE node_id= "+roomId+" ", null);

        rs.moveToFirst();

        String type=rs.getString(0);

        if(type.matches("Classroom") || type.matches("Amphitheatre")){
            return true;
        }else{
            return false;
        }

    }
    
    public List<Integer> GetToTheClosestFreeRoom(int sourceRoom) {

        boolean notFound=true;
        int destination = sourceRoom;

        boolean visited[]= new boolean[nodes.size()];

        LinkedList<Integer> queue = new LinkedList<Integer>();

        visited[sourceRoom]=true;
        queue.add(sourceRoom);

        while(queue.size() !=0 && notFound==true) {

            int node=queue.pop();
            LinkedList<Edge> neighbours = adjacencylist[node];
            for (int i = 0; i < neighbours.size(); i++) {
                Edge edge = neighbours.get(i);
                if (isFree(edge.id2) && isClassOrAmph(edge.id2)) {
                        destination = edge.id2;
                        notFound=false;
                }
                if(!visited[edge.id2])
                {
                    visited[edge.id2]=true;
                    queue.add(edge.id2);
                }
            }
        }

        return dijkstra(sourceRoom, destination);
    }
    
    
    //***************************************************************
    // bfs shortest path for unweighted graphs
//private void add_edge(int src, int dest)
//{
//	(adj.elementAt(src)).add(dest);
//	(adj.elementAt(dest)).add(src);
//}

private boolean BFS(int src, int dest, int[] pred, int[] dist)
{
	LinkedList<Integer> queue = new LinkedList<Integer>();
	boolean[] visited = new boolean[vertices];

	for (int i = 0; i < vertices; i++)
	{
		visited[i] = false;
		dist[i] = Integer.MAX_VALUE;
		pred[i] = -1;
	}

	visited[src] = true;
	dist[src] = 0;
	queue.addLast(src);


	while (!queue.isEmpty())
	{
		int u = queue.getFirst();
		queue.removeFirst();
		for (int i = 0; i < adj[u].size(); i++)
		{
			if (visited[adj[u].get(i)] == false)
			{
				visited[adj[u].get(i)] = true;
				dist[adj[u].get(i)] = dist[u] + 1;
				pred[adj[u].get(i)] = u;
				queue.addLast(adj[u].get(i));

				if (adj[u].get(i) == dest)
				{
				   return true;
				}
			}
		}
	}

	return false;
}


public ArrayList<Integer> bfsShortestDistance(int src, int dest)
{
		int[] pred = new int[vertices];
		int[] dist = new int[vertices];

	if (BFS(src, dest, pred, dist) == false)
		System.err.println("Error");
	
	ArrayList<Integer> path = new ArrayList<Integer>();
	int crawl = dest;
	path.add(crawl);
	while (pred[crawl] != -1)
	{
		path.add(pred[crawl]);
		crawl = pred[crawl];
	}

	return path;
}
    

}
