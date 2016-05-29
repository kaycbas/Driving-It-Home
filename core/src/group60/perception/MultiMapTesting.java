package group60.perception;

import java.awt.geom.Point2D;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class MultiMapTesting {
	public static void main(String args[]) {
		// generate spacemap, colormap, velmap
		int dim = 10;
		boolean[][] sm = new boolean[dim][dim];
		Color[][] cm = new Color[dim][dim];
		Vector2[][] vm = new Vector2[dim][dim];
		Point2D.Double[][] pm = new Point2D.Double[dim][dim];
		MultiMap mm;
		
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				sm[i][j] = true;
				cm[i][j] = new Color((float)0.5, (float)0.5, (float)0.5, (float)1.0);
				vm[i][j] = new Vector2((float)0.5, (float)0.5);
				pm[i][j] = new Point2D.Double(i+0.1, j+0.1);
			}
		}
		mm = new MultiMap(sm, cm, vm, pm);
		System.out.println(mm.getCell(2, 3));
	}

}
