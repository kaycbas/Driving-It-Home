/* MultiMapCell.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: A tuple of the entries from the maps
 * provided by the Sensing Interface.
 * 
 */

package group60.perception;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class MultiMapCell {
	private boolean occupied;
	private Color colour;
	private Vector2 abVel;
	private Point2D.Double abPos;
	private boolean isGrouped;
	private int row;
	private int col;

	/**
	 * Constructor for a MultiMapCell.
	 * Initializes this class' attributes to o, c, v.
	 * @param o Entry from getSpaceMap() from Sensing Interface.
	 * @param c Entry from getColourMap() from Sensing Interface.
	 * @param v Entry from getVelocityMap() from Sensing Interface.
	 */

	public MultiMapCell(boolean occupied, Color colour, Vector2 abVel, Point2D.Double abPos, int _row, int _col) {
		this.colour = colour;
		this.occupied = occupied;	
		this.abVel = abVel;
		this.abPos = abPos;
		this.isGrouped = false;
		this.row = _row;
		this.col = _col;
	}
	
	public boolean isOccupied() {
		return this.occupied;
	}
	
	public Color getColor() {
		return this.colour;
	}
	
	public Vector2 getAbsoluteVelocity() {
		return this.abVel;
	}
	
	public Point2D.Double getAbsolutePosition() {
		return this.abPos;
	}

	/**
	 * Compares the attributes isOccupied, colour and abVel
	 * of this MultiMapCell with m.
	 * @param m The MultiMapCell you want to compare with.
	 * @return True if the compared attributes are equal.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MultiMapCell other = (MultiMapCell) obj;
		if (abVel == null) {
			if (other.abVel != null)
				return false;
		} else if (!abVel.equals(other.abVel))
			return false;
		if (colour == null) {
			if (other.colour != null)
				return false;
		} else if (!colour.equals(other.colour))
			return false;
		if (occupied != other.occupied)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "MultiMapCell [occupied=" + occupied + ", colour=" + colour + ", abVel=" + abVel + ", abPos=" + abPos + "]";
	}

	public boolean isGrouped() { return isGrouped; }

	public void setGrouped() { isGrouped=true; }

	public int getRow() { return row; }

	public int getCol() { return col; }
	
}
