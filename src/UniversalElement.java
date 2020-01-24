import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class UniversalElement {
    private int size=4;

    private Point2D.Double[] points;
    private double[][] shapeFunctions;
    private double[][] ksiArray;
    private double[][] etaArray;

    public UniversalElement() {
        this.shapeFunctions=new double[size][size];
        this.etaArray=new double[size][size];
        this.ksiArray=new double[size][size];
        this.points=new Point2D.Double[size];
        calcPoints();
        System.out.println("\nShape Funcions Array");
        calcShapeFunctions();
        System.out.println("\ndN_dKsi Array");
        calcKsiArray();
        System.out.println("\ndN_dEta Array");
        calcEtaArray();

    }
    public void calcPoints() {
        this.points[0]=new Point2D.Double(-1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3));
        this.points[1]=new Point2D.Double(1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3));
        this.points[2]=new Point2D.Double(1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3));
        this.points[3]=new Point2D.Double(-1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3));

    }
    public void calcShapeFunctions(){
        for (int i = 0; i <size ; i++) {
            shapeFunctions[i][0]=0.25 * (1 - points[i].getX()) * (1 - points[i].getY());
            shapeFunctions[i][1]=0.25 * (1 + points[i].getX()) * (1 - points[i].getY());
            shapeFunctions[i][2]=0.25 * (1 + points[i].getX()) * (1 + points[i].getY());
            shapeFunctions[i][3]=0.25 * (1 - points[i].getX()) * (1 + points[i].getY());
        }
        print2DArray(shapeFunctions);
    }
    public void calcKsiArray(){

        for (int i = 0; i <size ; i++) {
          ksiArray[i][0]=-0.25 * (1 - points[i].getY());
          ksiArray[i][1]=0.25 * (1 - points[i].getY());
          ksiArray[i][2]=0.25 * (1 + points[i].getY());
          ksiArray[i][0]=-0.25 * (1 + points[i].getY());
        }
        print2DArray(ksiArray);
    }
    public void calcEtaArray(){

        for (int i = 0; i <size ; i++) {
            etaArray[i][0]=-0.25 * (1 - points[i].getX());
            etaArray[i][1]=-0.25 * (1 + points[i].getX());
            etaArray[i][2]=0.25 * (1 + points[i].getX());
            etaArray[i][0]=0.25 * (1 - points[i].getX());
        }
        print2DArray(etaArray);
    }
    public void print2DArray(double[][] array2D){
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                System.out.print(array2D[i][j]+"  ");
            }
            System.out.println("");
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Point2D.Double[] getPoints() {
        return points;
    }

    public void setPoints(Point2D.Double[] points) {
        this.points = points;
    }

    public double[][] getShapeFunctions() {
        return shapeFunctions;
    }

    public void setShapeFunctions(double[][] shapeFunctions) {
        this.shapeFunctions = shapeFunctions;
    }

    public double[][] getKsiArray() {
        return ksiArray;
    }

    public void setKsiArray(double[][] ksiArray) {
        this.ksiArray = ksiArray;
    }

    public double[][] getEtaArray() {
        return etaArray;
    }

    public void setEtaArray(double[][] etaArray) {
        this.etaArray = etaArray;
    }
}
