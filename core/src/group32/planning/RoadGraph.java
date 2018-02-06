package group32.planning;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.print.Doc;
import javax.xml.parsers.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;

public class RoadGraph {
	private static final float MAX_ROAD_DISTANCE = 50f;
	
	private ArrayList<GRoad> roads;
	private ArrayList<GIntersection> intersections;
	private String filename; 

	public RoadGraph(String filename) {
		this.filename = filename;
		this.roads = new ArrayList<GRoad>();
		this.intersections = new ArrayList<GIntersection>();
	}

	public void readCourse() {
		try {

			//String file = this.filename;
			FileHandle file = Gdx.files.internal(this.filename);			
			XmlReader reader = new XmlReader();
			Element root = reader.parse(file);

			Element roads = root.getChildByName("roads");
			Array<Element> roadList = roads.getChildrenByName("road");

			for(Element road : roadList) {
				float startX = road.getFloat("start_x");
				float startY = road.getFloat("start_y");
				float endX = road.getFloat("end_x");
				float endY = road.getFloat("end_y");
				float width = road.getFloat("width");
				int numLanes = road.getInt("num_lanes");

				Point2D.Double startPos = new Point2D.Double(startX, startY);
				Point2D.Double endPos = new Point2D.Double(endX, endY);

				Road r = new Road(startPos, endPos, width, numLanes, new int[]{0,0});

				String startId = null;
				String endId = null;
				String startDirection = null;
				String endDirection = null;
				Element intersection = road.getChildByName("intersections");
				if(intersection != null) {
					Element start = intersection.getChildByName("start");
					Element end = intersection.getChildByName("end");

					if(start != null) {
						startId = start.get("id");
						startDirection = start.get("direction");
					}

					if(end != null) {
						endId = end.get("id");
						endDirection = end.get("direction");
					}
				}


				this.roads.add(new GRoad(r, startId, endId, startDirection, endDirection));
			}
			
			Element intersections = root.getChildByName("intersections");
			Array<Element> intersectionList = intersections.getChildrenByName("intersection");
			for(Element i : intersectionList) {
				String intersectionId = i.get("intersection_id");
				float startX = i.getFloat("start_x");
				float startY = i.getFloat("start_y");
				float width = i.getFloat("width");
				float height = i.getFloat("height");
				
				Intersection inter = new Intersection(new Point2D.Double(startX, startY), width, height);

				this.intersections.add(new GIntersection(inter, intersectionId));
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buildIntersections() {
		for(GRoad road : this.roads) {
			for(GIntersection inter : this.intersections) {
				if(road.startId != null && road.startId.equals(inter.id)) {
					inter.intersection.registerRoad(this.getDirection(road.startDirection), road.road);
				}
				if(road.endId != null && road.endId.equals(inter.id)) {
					inter.intersection.registerRoad(this.getDirection(road.endDirection), road.road);
				}
			}
		}
	}
	
	public ArrayList<Intersection> getIntersections() {
		ArrayList<Intersection> intersections = new ArrayList<Intersection>();
		for(GIntersection i : this.intersections) {
			intersections.add(i.intersection);
		}
		return intersections;
	}
	
	private Intersection.Direction getDirection(String s){
		if(s.equals("north")){ 
			return Intersection.Direction.North;
		} else if(s.equals("south")){
			return Intersection.Direction.South;
		} else if(s.equals("west")){
			return Intersection.Direction.West;
		} else if(s.equals("east")){
			return Intersection.Direction.East;
		}
		return null;
	}
	
	public Road closestRoad(Point2D.Double pos){
		float minDist = Float.MAX_VALUE;
		Road minRoad = null;
		for(GRoad r: this.roads){
			float tmpDist = r.road.minDistanceTo(pos);
			if(tmpDist < minDist){
				minDist = tmpDist;
				minRoad = r.road;
			}
		}
		return (minDist < MAX_ROAD_DISTANCE) ? minRoad : null;
	}
	
	public Road roadAtPoint(Point2D.Double pos){
		for(GRoad r: this.roads){
			if(r.road.containsPoint(pos)){
				return r.road;
			}
		}
		return null;
	}

	public Graph.Edge[] createGraph() {
		ArrayList<Graph.Edge> listGraph = new ArrayList<Graph.Edge>();
		for(GRoad r : this.roads) {
			for(GRoad r2: this.roads) {
				if(!r.equals(r2)) {
					if(r.startId != null && r2.startId != null && r.startId.equals(r2.startId)) {
						listGraph.add(new Graph.Edge(r.road, r2.road, r.road.getLength()));
						//listGraph.add(new Graph.Edge(r2.road, r.road, r2.road.getLength()));
					}
					else if(r.startId != null && r2.endId != null && r.startId.equals(r2.endId)) {
						listGraph.add(new Graph.Edge(r.road, r2.road, r.road.getLength()));
						//listGraph.add(new Graph.Edge(r2.road, r.road, r2.road.getLength()));
					}
					else if(r.endId != null && r2.endId != null && r.endId.equals(r2.endId)) {
						listGraph.add(new Graph.Edge(r.road, r2.road, r.road.getLength()));
						//listGraph.add(new Graph.Edge(r2.road, r.road, r2.road.getLength()));
					}
//					if(r2.endId != null && r.endId != null && r2.endId.equals(r.endId)) {
//						listGraph.add(new Graph.Edge(r2.road, r.road, r2.road.getLength()));
//					}
//					if(r2.startId != null && r.endId != null && r2.endId.equals(r.startId)) {
//						listGraph.add(new Graph.Edge(r2.road, r.road, r2.road.getLength()));
//					}
//					if(r2.startId != null && r.startId != null && r2.startId.equals(r.startId)) {
//						listGraph.add(new Graph.Edge(r2.road, r.road, r2.road.getLength()));
//					}
				}
			}
		}
		
		Graph.Edge[] graph = new Graph.Edge[listGraph.size()];
		for(int i = 0; i<listGraph.size(); i++) {
			graph[i] = listGraph.get(i);
		}
		
		return graph;

	}
	
}

class GRoad {

	public Road road;
	public String startId;
	public String endId;
	public String startDirection;
	public String endDirection;

	public GRoad(Road r, String sid, String eid, String startDirection, String endDirection) {
		this.road = r;
		this.startId = sid;
		this.endId = eid;
		this.startDirection = startDirection;
		this.endDirection = endDirection;
	}
}

class GIntersection {
	public Intersection intersection;
	public String id;
	
	public GIntersection(Intersection i, String id) {
		this.intersection = i;
		this.id = id;
	}
}
