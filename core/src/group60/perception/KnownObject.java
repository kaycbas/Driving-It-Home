/* KnownObject.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: A KnownObject contains information about
 * a WorldObject.
 * 
 */

package group60.perception;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse.Classification;

public class KnownObject {

	/*
	 * Comment!
	 */
	public static final String NULL_STRING = "-";
	public static final String KEY_DELIMETER = ":";
	public Classification classification;
	public Color colour;
	public double length;
	public double width;
	public boolean staticFlag;
	public boolean collidableFlag;
	public HashMap<String, Object> information;

	/**
	 * comments :P
	 * 
	 * @param classification
	 * @param colour
	 * @param length
	 * @param width
	 * @param isStatic
	 * @param isCollidable
	 * @param info
	 */

	public KnownObject(Classification c, Color colour, double length, double width, boolean staticFlag,
			boolean collidableFlag, HashMap<String, Object> info) {
		this.classification = c;
		this.colour = colour;
		this.length = length;
		this.width = width;
		this.staticFlag = staticFlag;
		this.collidableFlag = collidableFlag;
		this.information = info;
	}
	
	/* matching logic */
	/*
	private boolean matchBuilding(Blob blob) {
		
	}
	
	private boolean matchTrafficLight(Blob blob) {
		
	}
	
	private boolean matchCar(Blob blob) {
		
	}
	
	private boolean matchRoadMarking(Blob blob) {
		
	}
	
	private boolean matchLaneMarking(Blob blob) {
		
	}
	
	private boolean matchSign(Blob blob) {
		
	}
	
	private boolean matchStreetLight(Blob blob) {
		
	}
	
	private boolean matchByWidth(Blob blob) {
		
	}
	
	private boolean matchByLength(Blob blob) {
		
	}
	*/
	public float getColourDifference(Blob blob) {
		Color bc = blob.getColour();
		float sub_r = this.colour.r - bc.r;
		float sub_g = this.colour.g - bc.g;
		float sub_b = this.colour.b - bc.b;
		return Math.abs(sub_r) + Math.abs(sub_g) + Math.abs(sub_b);
	}
	
	public double getSizeDifferent(Blob blob) {
		double sd1 = Math.abs(this.length - blob.getLength()) + Math.abs(this.width - blob.getWidth());
		double sd2 = Math.abs(this.length - blob.getWidth()) + Math.abs(this.width - blob.getLength());
		return Math.min(sd1,  sd2);
	}
	/*
	public boolean isMatch(Blob blob) {
		boolean staticBlob;
		if (blob.getAbVel().len() < 0.01) {
			staticBlob = true;
		} else {
			staticBlob = false;
		}
		if (this.colour.equals(blob.getColour()) &&
			Math.abs(this.length - blob.getLength()) < 0.01 &&
			Math.abs(this.width - blob.getWidth()) < 0.01 &&
			this.collidableFlag == blob.isColliable() &&
			this.staticFlag == staticBlob) {
			return true;
		} else {
			return false;
		}
	}
	*/
	
	@Override
	public String toString() {
		return "KnownObject [classification=" + classification + ", colour=" + colour + ", length=" + length
				+ ", width=" + width + ", staticFlag=" + staticFlag + ", collidableFlag=" + collidableFlag
				+ ", information=" + information + "]";
	}
}
