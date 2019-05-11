/**
 * 
 */
package com.frontend.backend.ARGuide.main;

import java.util.List;

/**
 * The blueprint for a Building Plan object
 * @author Paul-Reftu
 *
 */
public class BuildingPlan {
	private List<Node> nodes;
	private List<Edge> edges;

	/**
	 * 
	 */
	public BuildingPlan() {
		
	}
	
	/**
	 * @return the nodes
	 */
	public List<Node> getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * @param edges the edges to set
	 */
	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public class Node {
		private int id;
		private String name;
		private String type;
		private int floor;
		/**
		 * @return the id
		 */
		public int getId() {
			return id;
		}
		/**
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}
		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return the type
		 */
		public String getType() {
			return type;
		}
		/**
		 * @param type the type to set
		 */
		public void setType(String type) {
			this.type = type;
		}
		/**
		 * @return the floor
		 */
		public int getFloor() {
			return floor;
		}
		/**
		 * @param floor the floor to set
		 */
		public void setFloor(int floor) {
			this.floor = floor;
		}
	}
	
	public class Edge {
		private int id_node1;
		private int id_node2;
		private double cost;
		/**
		 * @return the id_node1
		 */
		public int getId_node1() {
			return id_node1;
		}
		/**
		 * @param id_node1 the id_node1 to set
		 */
		public void setId_node1(int id_node1) {
			this.id_node1 = id_node1;
		}
		/**
		 * @return the id_node2
		 */
		public int getId_node2() {
			return id_node2;
		}
		/**
		 * @param id_node2 the id_node2 to set
		 */
		public void setId_node2(int id_node2) {
			this.id_node2 = id_node2;
		}
		/**
		 * @return the cost
		 */
		public double getCost() {
			return cost;
		}
		/**
		 * @param cost the cost to set
		 */
		public void setCost(double cost) {
			this.cost = cost;
		}
	}

}
