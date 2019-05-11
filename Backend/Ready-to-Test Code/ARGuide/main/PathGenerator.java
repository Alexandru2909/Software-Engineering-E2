package main;

import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Computes the shortes path between two nodes (the distance itself, as well as the path)
 * @author
 *
 */

public class PathGenerator {

    public final BuildingPlan myBuildingPlan;
    private List<BuildingPlan.Node> nodes;
    private List<BuildingPlan.Edge> edges;
    private List<BuildingPlan.Node> path;
    int[] previous;
    private LinkedList<BuildingPlan.Edge>[] adjacencylist;
    public int vertices;

    public PathGenerator(BuildingPlan g)
    {
        this.myBuildingPlan = g;
        this.edges = myBuildingPlan.getEdges();
        this.nodes = myBuildingPlan.getNodes();
        vertices = nodes.size();
        //array of previous nodes (used to return the path from destination to source)
        previous = new int[vertices];
        adjacencylist = new LinkedList[vertices];

        //initialize adjacency lists for all the  nodes
        for (int i = 0; i < vertices; i++)
            adjacencylist[i] = new LinkedList<>();
        //creating the adjacency list
        for (int i = 0; i < edges.size(); i++)
            adjacencylist[edges.get(i).getId_node1()].addFirst(edges.get(i));
    }

    public void dijkstra_GetMinDistances(int sourceVertex, int destinationVertex) {

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
        distance[0] = 0;
        Pair<Integer, Integer> p0 = new Pair<>(distance[0], 0);
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
                LinkedList<BuildingPlan.Edge> list = adjacencylist[extractedVertex];
                for (int i = 0; i < list.size(); i++) {
                    BuildingPlan.Edge edge = list.get(i);
                    int destination = edge.getId_node2();
                    //only if edge destination is not present in mst
                    if (SPT[destination] == false) {
                        ///check if distance needs an update or not
                        //means check total weight from source to vertex_V is less than
                        //the current distance value, if yes then update the distance
                        int newKey = distance[extractedVertex] + (int) edge.getCost();
                        int currentKey = distance[destination];
                        if (currentKey > newKey) {
                            Pair<Integer, Integer> p = new Pair<>(newKey, destination);
                            pq.offer(p);
                            distance[destination] = newKey;
                            previous[edge.getId_node2()]=edge.getId_node1();
                        }
                    }
                }
            }
        }
        //print Shortest Path Tree
        printDijkstra(distance, sourceVertex,destinationVertex);
    }

    public void printDijkstra(int[] distance, int sourceVertex, int destinationVertex) {
        System.out.println("Dijkstra Algorithm: (Adjacency List + Priority Queue)");
        for (int i = 0; i < vertices; i++) {
            System.out.println("Source Vertex: " + sourceVertex + " to vertex " + +i +
                    " distance: " + distance[i]);
        }
        int i = destinationVertex;
        path.add(nodes.get(i));
        while (distance[i] != 0) {
            System.out.println(nodes.get(previous[i]));
            i=previous[i];
            path.add(nodes.get(i));
        }
    }
}
