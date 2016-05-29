/* KnownObjectHandler.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: This class as the name suggests is responsible
 * for storing the set of KnownObjects that we will use to
 * identify Blobs with.
 *  
 */

package group60.perception;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;
import com.unimelb.swen30006.partc.core.infrastructure.TrafficLight;

public class KnownObjectHandler {
	
	/*
	 * Some comments
	 */
	private HashMap<String, Classification> xmlToCl;
	private ArrayList<KnownObject> knownObjects = new ArrayList<KnownObject>();
	private static final String DATAFILE = "C:\\Users\\Kevin\\Documents\\Professional\\Academic\\UoM\\SWD\\Part C - Driving It Home\\SWEN30006 Part C - Driving It Home\\core\\src\\group60\\perception\\known_object_set.xml";
	private boolean initialised = false;

	/**
	 * Constructor + comments
	 */
	public KnownObjectHandler(){
		initHashMap();
	}
	
	public ArrayList<KnownObject> getKnownObjects() {
		if (!this.initialised) {
			parseKnownObjects();
		} 
		return this.knownObjects;
	}
	
	private void initHashMap() {
		xmlToCl = new HashMap<String, Classification>();
		xmlToCl.put("building", Classification.Building);
		xmlToCl.put("traffic_light", Classification.TrafficLight);
		xmlToCl.put("car", Classification.Car);
		xmlToCl.put("road_marking", Classification.RoadMarking);
		xmlToCl.put("lane_marking", Classification.LaneMarking);
		xmlToCl.put("sign", Classification.Sign);
		xmlToCl.put("street_light", Classification.StreetLight);
	}
	

	
	private void parseKnownObjects(){
		try {
	        File inputFile = new File(DATAFILE);
			
	        // Build the doc factory
	        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	        Document root = dBuilder.parse(inputFile);
	        root.getDocumentElement().normalize();
	        
	        for (String s : xmlToCl.keySet()) {
	        	NodeList nodeList = root.getElementsByTagName(s);
	        	int nlength = nodeList.getLength();
				for(int i = 0; i < nlength; i++){
		            Node nNode = nodeList.item(i);
		            Element e = (Element) nNode;
					this.knownObjects.add(processKnownObjectElement(xmlToCl.get(s), e));
				}
	        }
	        this.initialised = true;
		} catch (Exception e){
			e.printStackTrace();
			System.exit(0);
		}
	}

	private KnownObject processKnownObjectElement(Classification c, Element e) {
		Element colElement = (Element)e.getElementsByTagName("colour").item(0);
		Color col = extractColour(colElement);
		float width = Float.parseFloat(
				e.getElementsByTagName("width").item(0).getTextContent());
		float length = Float.parseFloat(
				e.getElementsByTagName("length").item(0).getTextContent());
		boolean static_flag = Boolean.parseBoolean(
				e.getElementsByTagName("static_flag").item(0).getTextContent());
		boolean collidable_flag = Boolean.parseBoolean(
				e.getElementsByTagName("collidable_flag").item(0).getTextContent());
		HashMap<String, Object> information = extractHashMap(
				e.getElementsByTagName("information").item(0).getTextContent());
		// fix to put a trafficlightstate as value
		if (c.equals(Classification.TrafficLight)) {
			TrafficLight.State tls;
			String ss = (String)information.get("State");
			if (ss.equals("red")) {
				tls = TrafficLight.State.Red;
			} else if (ss.equals("green")) {
				tls = TrafficLight.State.Green;
			} else if (ss.equals("yellow")) {
				tls = TrafficLight.State.Amber;
			} else {
				tls = TrafficLight.State.Off;
			}
			information.put("State", tls);
		}
		//System.out.println(col);
		return new KnownObject(c, col, length, width, static_flag, collidable_flag, information);
	}
	
	private HashMap<String, Object> extractHashMap(String hms) {
		HashMap<String, Object> info = new HashMap<String, Object>();
		String[] kvPairs = hms.split(",");
		String[] this_pair;
		if (kvPairs.length == 1) {
			if (kvPairs[0].equals("-")) {
				return info;
			}
		}
		for (String s : kvPairs) {
			this_pair = s.split(":");
			info.put(this_pair[0], this_pair[1]);
		}
		return info;
	}
	
	
	private Color extractColour(Element e){
		float red = Float.parseFloat(
				e.getElementsByTagName("red").item(0).getTextContent())/255f;
		float green = Float.parseFloat(
				e.getElementsByTagName("green").item(0).getTextContent())/255f;
		float blue =Float.parseFloat(
				e.getElementsByTagName("blue").item(0).getTextContent())/255f;
		Color col = new Color(red, green, blue, 1f);
		return new Color(red, green, blue, 1f);
	}
	
}
