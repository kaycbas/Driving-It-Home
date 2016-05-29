package group60.perception;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.ai.interfaces.PerceptionResponse;
import com.unimelb.swen30006.partc.core.objects.Car;

import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;

import static java.lang.System.in;

/**
 * Created by Kevin on 5/29/2016.
 */
public class TestMain {

    public static void main(String [] args) {
        int dim =100;
        boolean[][] spaceMap = new boolean[dim][dim];
        Color[][] colorMap = new Color[dim][dim];
        Vector2[][] velMap = new Vector2[dim][dim];

        for (int row=0; row<dim; row++) {
            for (int col=0; col<dim; col++) {
                spaceMap[row][col] = false;
                colorMap[row][col] = Color.BLACK;
                velMap[row][col] = new Vector2(0, 0);
            }
        }

        spaceMap[5][5] = true;
        colorMap[5][5] = new Color(1, 1, 0, 0);

        //spaceMap[8][8] = true;
        KnownObjectHandler koh = new KnownObjectHandler();
        Color roadMarkingColor = null;
        for (KnownObject ko : koh.getKnownObjects()) {
            if (ko.classification.equals(PerceptionResponse.Classification.RoadMarking)) {
                roadMarkingColor = ko.colour;
                //System.out.println(roadMarkingColor.toString());
                break;
            }
        }
        colorMap[8][8] = new Color(roadMarkingColor);
        colorMap[9][8] = new Color(roadMarkingColor);
        colorMap[10][8] = new Color(roadMarkingColor);

        for (int w=0+10; w<70+10; w++) {
            for (int h=0+10; h<80+10; h++) {
                spaceMap[h][w] = true;
                colorMap[h][w] = new Color(1, 1, 1, 1);
            }
        }

        /*
        //add objects to spaceMap
        spaceMap[2][2] = true;
        spaceMap[2][3] = true;
        spaceMap[2][4] = true;
        spaceMap[3][2] = true;
        spaceMap[3][3] = true;
        spaceMap[3][4] = true;
        spaceMap[4][2] = true;
        spaceMap[4][3] = true;
        spaceMap[4][4] = true;

        spaceMap[5][6] = true;
        spaceMap[5][7] = true;
        spaceMap[6][6] = true;
        spaceMap[6][7] = true;

        spaceMap[12][12] = true;
        spaceMap[12][13] = true;
        spaceMap[12][14] = true;
        spaceMap[13][12] = true;
        spaceMap[13][13] = true;
        spaceMap[13][14] = true;
        spaceMap[14][12] = true;
        spaceMap[14][13] = true;
        spaceMap[14][14] = true;
        spaceMap[15][12] = true;
        spaceMap[15][13] = true;
        spaceMap[15][14] = true;
        spaceMap[16][12] = true;
        spaceMap[16][13] = true;
        spaceMap[16][14] = true;
        spaceMap[17][12] = true;
        spaceMap[17][13] = true;
        spaceMap[17][14] = true;



        //set object colors
        colorMap[2][2] = Color.BLUE;
        colorMap[2][3] = Color.BLUE;
        colorMap[2][4] = Color.BLUE;
        colorMap[3][2] = Color.BLUE;
        colorMap[3][3] = Color.BLUE;
        colorMap[3][4] = Color.BLUE;
        colorMap[4][2] = Color.BLUE;
        colorMap[4][3] = Color.BLUE;
        colorMap[4][4] = Color.BLUE;

        colorMap[5][6] = Color.CORAL;
        colorMap[5][7] = Color.CORAL;
        colorMap[6][6] = Color.CORAL;
        colorMap[6][7] = Color.CORAL;

        colorMap[12][12] = Color.CHARTREUSE;
        colorMap[12][13] = Color.CHARTREUSE;
        colorMap[12][14] = Color.CHARTREUSE;
        colorMap[13][12] = Color.CHARTREUSE;
        colorMap[13][13] = Color.CHARTREUSE;
        colorMap[13][14] = Color.CHARTREUSE;
        colorMap[14][12] = Color.CHARTREUSE;
        colorMap[14][13] = Color.CHARTREUSE;
        colorMap[14][14] = Color.CHARTREUSE;
        colorMap[15][12] = Color.CHARTREUSE;
        colorMap[15][13] = Color.CHARTREUSE;
        colorMap[15][14] = Color.CHARTREUSE;
        colorMap[16][12] = Color.CHARTREUSE;
        colorMap[16][13] = Color.CHARTREUSE;
        colorMap[16][14] = Color.CHARTREUSE;
        colorMap[17][12] = Color.CHARTREUSE;
        colorMap[17][13] = Color.CHARTREUSE;
        colorMap[17][14] = Color.CHARTREUSE;

        */


        Car fakeCar = new Car(new Point2D.Double(5, 0), 1, 2, Color.BROWN, 0, 2, 2);

        PerceptionController pc = new PerceptionController(fakeCar, 5);
        MultiMap mp = pc.combineMaps(spaceMap, colorMap, velMap);


        System.out.println("MultiMap...");
        printMultiMap(mp);
        System.out.println();


        ArrayList<Blob> blobs = pc.detectBlobs(mp);
        System.out.println("num blobs detected: " + blobs.size());
        for (Blob b : blobs) {
            System.out.println("blob pos: (" + b.getPosition().getY() + ", " + b.getPosition().getX()
                    + ") blob height: " + b.getLength() + " & width: " + b.getWidth() + " & velocity: " + 
            		b.getAbVel() + " & collidable: " + b.isColliable());
        }

/*
        Blob blob1 = blobs.get(0);
        System.out.println("b1 height: " + blob1.getLength());
        System.out.println("b1 width: " + blob1.getWidth());
        //System.out.println("blob1 height: " + blob1.getLength() + " , width: " + blob1.getWidth()
                //+ " , pos: (" + blob1.getPosition().getY() + ", " + blob1.getPosition().getX() + ")");
        Blob blob2 = blobs.get(1);
        System.out.println("b2 height: " + blob2.getLength());
        System.out.println("b2 height: " + blob2.getWidth());
        //System.out.println("blob2 height: " + blob2.getLength() + " , width: " + blob2.getWidth()
                //+ " , pos: (" + blob2.getPosition().getY() + ", " + blob2.getPosition().getX() + ")");


*/
        ArrayList<ClassifiedObject> objs = pc.classifyBlobs(blobs);
        for (ClassifiedObject co : objs) {
            System.out.println("Classified object: " + co.classification + " Position: " + co.getPosition());
        }


        PerceptionResponse[] pResponses = pc.createPerceptionResponses(objs);
        //PerceptionResponse[] pResponses = pc.analyseSurroundings(spaceMap, colorMap, velMap);
        for (PerceptionResponse p : pResponses) {
            System.out.println(p.objectType);
            System.out.println(p.distance);
            System.out.println(p.timeToCollision);
            System.out.println(p.information.toString());
        }

    }

    private static void printMultiMap(MultiMap mm) {
        int dim = mm.getDim();
        for (int row=0; row<dim; row++) {
            for (int col=0; col<dim; col++) {
                if (mm.getCell(row, col).isOccupied()) {
                    System.out.print("+ ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.print("\n");
        }
    }

    private boolean [][] readSpaceMap(File filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            int dim = 10;
            boolean[][] spaceMap = new boolean[dim][dim];

            String val = br.readLine();
            String [] tokens = val.split(" ");
            int i = 0;
            for (int row=0; row<dim; row++) {
                for (int col=0; col<dim; col++) {
                    spaceMap[row][col] = tokens[i].equals("1") ? true : false;
                }
            }
            return spaceMap;
        } catch (IOException ioe) {
            return null;
        }

    }

    private boolean [][] readColorMap(File filename) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            int dim = 10;
            boolean[][] colorMap = new boolean[dim][dim];

            String val = br.readLine();
            String [] tokens = val.split(" ");
            int i = 0;
            for (int row=0; row<dim; row++) {
                for (int col=0; col<dim; col++) {
                    //Color color = new Color(tokens[i]);
                    //spaceMap[row][col] = tokens[i].equals("1") ? true : false;
                }
            }
            return null;
        } catch (IOException ioe) {
            return null;
        }

    }

}


