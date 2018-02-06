package group32.planning;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;

import com.unimelb.swen30006.partc.roads.Road;

/**
 * Code resourced from http://rosettacode.org/wiki/Dijkstra%27s_algorithm#Java and adapted to the 
 * Driving it Home use case for finding the shortest path from the current road to the destination road
 * Roads are vertices and edges are weighted by the length of the current road.
 * @author jordanlopresti
 *
 */
public class Dijkstra {

}

class Graph {
	private final Map<Road, Vertex> graph; // mapping of vertex names to Vertex objects, built from a set of Edges
	private static int count;


	/** One edge of the graph (only used by Graph constructor) */
	public static class Edge {
		public final Road v1, v2;
		public final float dist;
		public Edge(Road road, Road road2, float f) {
			this.v1 = road;
			this.v2 = road2;
			this.dist = f;
		}
	}

	/** One vertex of the graph, complete with mappings to neighbouring vertices */
	public static class Vertex implements Comparable<Vertex> {
		//private ArrayList<Road> path = new ArrayList<Road>();
		public final Road name;
		public float dist = Float.MAX_VALUE; // MAX_VALUE assumed to be infinity
		public Vertex previous = null;
		public final Map<Vertex, Float> neighbours = new HashMap<Vertex, Float>();

		public Vertex(Road v1) {
			this.name = v1;
		}

		public boolean getPath(ArrayList<Road> path) {
			if(this == this.previous) {
				path.add(this.name);
			} else if(this.previous == null) {
				return false;
			}
			else {
				this.previous.getPath(path);
				path.add(this.name);
			}
			return true;
		}
		
		private void printPath() {
			if (this == this.previous) {
				Graph.count++;
				System.out.printf("%s", this.name);
			} else if (this.previous == null) {
				System.out.printf("%s(unreached)", this.name);
			} else {
				this.previous.printPath();
				System.out.printf(" -> %s(%.0f)", this.name, this.dist);
			}
		}

		public int compareTo(Vertex other) {
			return Float.compare(dist, other.dist);
		}
	}

	/** Builds a graph from a set of edges */
	public Graph(Edge[] edges) {
		graph = new HashMap<Road, Vertex>(edges.length);

		//one pass to find all vertices
		for (Edge e : edges) {
			if (!graph.containsKey(e.v1)) graph.put(e.v1, new Vertex(e.v1));
			if (!graph.containsKey(e.v2)) graph.put(e.v2, new Vertex(e.v2));
		}

		//another pass to set neighbouring vertices
		for (Edge e : edges) {
			graph.get(e.v1).neighbours.put(graph.get(e.v2), e.dist);
			graph.get(e.v2).neighbours.put(graph.get(e.v1), e.dist); // also do this for an undirected graph
		}
	}

	/** Runs dijkstra using a specified source vertex */ 
	public void dijkstra(Road road) {
		if (!graph.containsKey(road)) {
			System.err.printf("Graph doesn't contain start vertex \"%s\"\n", road);
			return;
		}
		final Vertex source = graph.get(road);
		NavigableSet<Vertex> q = new TreeSet<Vertex>();

		// set-up vertices
		for (Vertex v : graph.values()) {
			v.previous = v == source ? source : null;
			v.dist = v == source ? 0 : Integer.MAX_VALUE;
			q.add(v);
		}

		dijkstra(q);
	}

	/** Implementation of dijkstra's algorithm using a binary heap. */
	private void dijkstra(final NavigableSet<Vertex> q) {      
		Vertex u, v;
		while (!q.isEmpty()) {

			u = q.pollFirst(); // vertex with shortest distance (first iteration will return source)
			if (u.dist == Integer.MAX_VALUE) break; // we can ignore u (and any other remaining vertices) since they are unreachable

			//look at distances to each neighbour
			for (Map.Entry<Vertex, Float> a : u.neighbours.entrySet()) {
				v = a.getKey(); //the neighbour in this iteration

				final float alternateDist = u.dist + a.getValue();
				if (alternateDist < v.dist) { // shorter path to neighbour found
					q.remove(v);
					v.dist = alternateDist;
					v.previous = u;
					q.add(v);
				} 
			}
		}
	}

	public ArrayList<Road> getPath(Road road) {
		ArrayList<Road> tmp = new ArrayList<Road>();
		if (!graph.containsKey(road)) {
			System.err.printf("Graph doesn't contain end vertex \"%s\"\n", road);
			return null;
		}

		if(graph.get(road).getPath(tmp)) {
			return tmp;
		} else {
			return null;
		}
		//System.out.println();
	}
	/** Prints a path from the source to the specified vertex */
	public void printPath(Road road) {
		if (!graph.containsKey(road)) {
			System.err.printf("Graph doesn't contain end vertex \"%s\"\n", road);
			return;
		}

		graph.get(road).printPath();
		System.out.println();
	}
	/** Prints the path from the source to every vertex (output order is not guaranteed) */
	public void printAllPaths() {
		for (Vertex v : graph.values()) {
			v.printPath();
			System.out.println();
		}
	System.out.println("Paths found: " + Graph.count);
	}
}