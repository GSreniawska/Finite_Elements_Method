

import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.linear.GaussianSolver;

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
        int numberOfSteps=(int)(globalData.getSimTime()/globalData.getSimStepTime());
        double[] tArray = new double[finalSize];
        //ustawiamy wektor poczatkowy z temperaturą



        Vector INITIAL_TEMP_VECTOR=Vector.fromArray(tArray);
        for (int i = 0; i <finalSize ; i++) {
            INITIAL_TEMP_VECTOR.set(i,globalData.getTInitial());
        }
        int k=1;

        H_Matrix=grid.getH_Global_Matrix();
        C_Matrix=grid.getC_Global_Matrix();
        P_Vector=grid.getP_Global_Vector();

        System.out.println("\nTime[s]\t\tMinTemp[C]\t\t\t\tMaxTemp[C]");
        for (int i = 0; i <globalData.getSimTime()/globalData.getSimStepTime();  i++) {


            Matrix H_FINAL_MATRIX=Matrix.from2DArray(H_Matrix);

            Matrix C_FINAL_MATRIX=Matrix.from2DArray(C_Matrix);
            C_FINAL_MATRIX=C_FINAL_MATRIX.divide(globalData.getSimStepTime());

            Vector P_FINAL_VECTOR=Vector.fromArray(P_Vector).multiply(-1);
            P_FINAL_VECTOR=P_FINAL_VECTOR.add(C_FINAL_MATRIX.multiply(INITIAL_TEMP_VECTOR));

            H_FINAL_MATRIX=H_FINAL_MATRIX.add(C_FINAL_MATRIX);

            GaussianSolver gaussianSolver=new GaussianSolver(H_FINAL_MATRIX);
            Vector temps=gaussianSolver.solve(P_FINAL_VECTOR);
         //   System.out.println(temps);
            double tMax= temps.max();
            double tMin=temps.min();

            System.out.println(k*globalData.getSimStepTime()+"\t\t"+tMin+"\t\t"+tMax);
            k++;

            INITIAL_TEMP_VECTOR=temps;
        }





    }

}

