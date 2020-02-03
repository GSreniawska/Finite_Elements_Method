

import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.Vector;

import java.awt.geom.Point2D;
import java.util.HashMap;


public class FEM implements Runnable {
    private int size;
    private int finalSize;
    private double[][] H_Matrix;
    private double[][] C_Matrix;
    private double[] P_Vector;


    public FEM() {
        this.size = 4;
    }

    @Override
    public void run() {
        GlobalData globalData = new GlobalData();
        System.out.println(globalData.toString());
        UniversalElement universalElement = new UniversalElement(); //tworzymy element
        Grid grid = new Grid(globalData, universalElement); //tworzymy siatke
        simulation(grid,globalData);


    }

    public void simulation(Grid grid, GlobalData globalData) {
        finalSize = globalData.getnH() * globalData.getnW();
        H_Matrix = new double[finalSize][finalSize];
        C_Matrix = new double[finalSize][finalSize];
        P_Vector=new double[finalSize];

    //    Matrix H_samo=Matrix.from2DArray(grid.getH_Global_Matrix());
       // System.out.println("\nH_samo");
    //    System.out.println(H_samo);
        double[] t0Array = new double[finalSize];


        System.out.println("\n\nStep: "+globalData.getSimStepTime());

        for (int i = 0; i < finalSize; i++) {
            for (int j = 0; j < finalSize; j++) {
                H_Matrix[i][j] = grid.getH_Global_Matrix()[i][j] + (grid.getC_Global_Matrix()[i][j])/globalData.getSimStepTime();
                C_Matrix[i][j] = grid.getC_Global_Matrix()[i][j]/globalData.getSimStepTime() ;

            }
        }
        System.out.println("mjsjsdjfaejkkaehsfjdalklajdfsjbhjskdkjlaljsfdjdkjdsdldasnf");
        for (int i = 0; i <finalSize ; i++) {
            for (int j = 0; j <finalSize ; j++) {

                System.out.print(H_Matrix[i][j]+"   ");
            }
            System.out.println();
        }
        System.out.println("\nTime[s]\t\tMinTemp[C]\tMaxTemp[C]");
        for (int k = 0; k< 10; k++) {

            for (int j = 0; j < grid.getNodes().length; j++) {
                t0Array[j] = grid.getNodes()[j].getT();
            }

        }
    }

}

