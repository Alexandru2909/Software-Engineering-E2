package com.frontend.backend.ARGuide.main;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.javatuples.Pair;

/**
 * Computes the shortest path between two nodes (the distance itself, as well as the path)
 * @author Ioana-Balan
 *
 */

public class PathGenerator {
    private List<Integer> nodes=new ArrayList<>();
    private List<Integer> path=new ArrayList<>();
    public ArrayList<Integer>[] adj = new ArrayList<>();
    private int[] previous;
    private LinkedList<Edge>[] adjacencylist;
    private int vertices;
    private SQLiteDatabase db;
    
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

    
    public void getRoomFloors() throws SQLException
    {
        Cursor rs=db.rawQuery("select id,floor from nodes;",null);
        rs.moveToFirst();
            while(!rs.isAfterLast())
            {
                getFloor.put(rs.getDouble(1)-1, rs.getInt(2));
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
        Cursor rs=db.rawQuery("select floor,day,substr(starting_time,1,2) from nodes natural join schedule;",null);
        rs.moveToFirst();
            while(!rs.isAfterLast())
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
        for (Number i : roomsPerFloor.keySet())
        {
            if((roomsPerFloor.get(i)/2)==occupiedRoomsPerFloor.get(i))
                if(!occupiedFloors.contains(i))
                    occupiedFloors.add(i);
        }

    }
    public List<Integer> dijkstra_activity(int sourceVertex, int destinationVertex) throws SQLException {

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
                int key1 = p1.getValue0();
                int key2 = p2.getValue0();
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
            int extractedVertex = extractedPair.getValue1();
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
    
    //***************************************************************
    // bfs shortest path for unweighted graphs
private void add_edge(int src, int dest)
{
	adj[src].add(dest);
	adj[dest].add(src);
}

private boolean BFS(int src, int dest, int[] pred, int[] dist)
{
	LinkedList<Integer> queue = new LinkedList<Integer>();
	boolean[] visited = new boolean[vertices];

	for (int i = 0; i < vertices; i++)
	{
		visited[i] = false;
		dist[i] = INT_MAX;
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
	{
		System.out.print("Given source and destination");
		System.out.print(" are not connected");
		return;
	}

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
