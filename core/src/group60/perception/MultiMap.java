/* MultiMap.java
 * 
 * SWEN30006 Part C - Driving It Home
 * Semester 1, 2016
 * 
 * by Group 60
 * Adrian Cheung 636330
 * Shi Chang Zhang 695434
 * Kevin Bastoul 802847
 * 
 * Description: A MultiMap is a combination of all the maps
 * provided by the Sensing Interface.
 * 
 */

package group60.perception;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class MultiMap {
	/*
	 * Comments about attributes.
	 */
	private int dim;
	private MultiMapCell[][] grid;
	
	/**
	 * Comments.
	 * @param spaceMap
	 * @param colourMap
	 * @param velocityMap
	 */
	public MultiMap(boolean[][] spaceMap, Color[][] colorMap, 
			Vector2[][] velMap, Point2D.Double[][] posMap) {
		this.dim = spaceMap.length;
		this.grid = new MultiMapCell[dim][dim];
		for (int row = 0; row < this.dim; row++) {
			for (int col = 0; col < this.dim; col++) {
				grid[row][col] = new MultiMapCell(spaceMap[row][col], colorMap[row][col],
						velMap[row][col], posMap[row][col], row, col);
			}
		}
	}
	
	public MultiMapCell getCell(int row, int col) {
		return this.grid[row][col];
	}

	public int getDim() {
		return dim;
	}
}
