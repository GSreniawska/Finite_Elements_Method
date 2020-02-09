

import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.linear.GaussianSolver;
import org.la4j.linear.LinearSystemSolver;

import java.text.DecimalFormat;
import java.util.Scanner;


public class FEM implements Runnable {
    private static DecimalFormat df2 = new DecimalFormat("#.##");
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
        String simData="";
        int numberOfSimulation=-1;
        System.out.println("Which simulation (1 or 2) or wall of industrial oven (3) ?");
        try {
            Scanner input = new Scanner(System.in);
             numberOfSimulation= input.nextInt();
        } catch (Exception e) {
            e.getMessage();
        }
            switch (numberOfSimulation) {
                case 1:
                    simData = "data.txt";
                    break;
                case 2:
                    simData = "data2.txt";
                    break;
                case 3:
                    simData = "data3.txt";
                    break;
            }

        GlobalData globalData = new GlobalData(simData,numberOfSimulation);
        System.out.println(globalData.toStringExtra_Project());
        UniversalElement universalElement = new UniversalElement(); //tworzymy element
        Grid grid = new Grid(globalData, universalElement,numberOfSimulation); //tworzymy siatke
        if(numberOfSimulation==3){
            simulationExtra_Project(grid, globalData);
        }
        else{
            simulationBasic(grid,globalData);
        }

    }
    public void simulationBasic(Grid grid,GlobalData globalData){
        finalSize = globalData.getnH() * globalData.getnW();
        H_Matrix = new double[finalSize][finalSize];
        C_Matrix = new double[finalSize][finalSize];
        P_Vector=new double[finalSize];
        double[] tArray = new double[globalData.getnN()];

        Vector INITIAL_TEMP_VECTOR=Vector.fromArray(tArray);
        for (int i = 0; i <finalSize ; i++) {
            INITIAL_TEMP_VECTOR.set(i,globalData.getTInitial());
        }
        int k=1;

        H_Matrix=grid.getH_Global_Matrix();
        C_Matrix=grid.getC_Global_Matrix();
        P_Vector=grid.getP_Global_Vector();
        Vector temps;
        double tAvg=globalData.getAmbientTemp();
        System.out.println("\nTime[s]\tMaxTemp[C]\t\tMinTemp[C]");

        for (int i=0;i<globalData.getSimTime()/globalData.getSimStepTime();i++){
            Matrix H_FINAL_MATRIX = Matrix.from2DArray(H_Matrix);

            Matrix C_FINAL_MATRIX = Matrix.from2DArray(C_Matrix);
            C_FINAL_MATRIX = C_FINAL_MATRIX.divide(globalData.getSimStepTime());

            Vector P_FINAL_VECTOR = Vector.fromArray(P_Vector).multiply(-1);
            H_FINAL_MATRIX = H_FINAL_MATRIX.add(C_FINAL_MATRIX);
            Vector tmpVec = C_FINAL_MATRIX.multiply(INITIAL_TEMP_VECTOR);
            P_FINAL_VECTOR = P_FINAL_VECTOR.add(tmpVec);

            LinearSystemSolver gaussianSolver = new GaussianSolver(H_FINAL_MATRIX);
            temps = gaussianSolver.solve(P_FINAL_VECTOR);


            double tMax = temps.max();
            double tMin = temps.min();

            System.out.println((int) (k * globalData.getSimStepTime()) + "\t\t" + tMax+"\t\t"+tMin);

            k++;
            INITIAL_TEMP_VECTOR = temps;
        }
    }

    public void simulationExtra_Project(Grid grid, GlobalData globalData) {
        finalSize = globalData.getnH() * globalData.getnW();
        H_Matrix = new double[finalSize][finalSize];
        C_Matrix = new double[finalSize][finalSize];
        P_Vector=new double[finalSize];
        double[] tArray = new double[globalData.getnN()];

        Vector INITIAL_TEMP_VECTOR=Vector.fromArray(tArray);
        for (int i = 0; i <finalSize ; i++) {
            INITIAL_TEMP_VECTOR.set(i,globalData.getTInitial());
        }
        int k=1;

        H_Matrix=grid.getH_Global_Matrix();
        C_Matrix=grid.getC_Global_Matrix();
        P_Vector=grid.getP_Global_Vector();
        Vector temps;
        double tAvg=globalData.getAmbientTemp();
        System.out.println("\nTime[s]\tAvgTemp[C]");

            for (double tInit=globalData.getTInitial();tInit<globalData.getAmbientTemp();){
                Matrix H_FINAL_MATRIX = Matrix.from2DArray(H_Matrix);

                Matrix C_FINAL_MATRIX = Matrix.from2DArray(C_Matrix);
                C_FINAL_MATRIX = C_FINAL_MATRIX.divide(globalData.getSimStepTime());

                Vector P_FINAL_VECTOR = Vector.fromArray(P_Vector).multiply(-1);
                H_FINAL_MATRIX = H_FINAL_MATRIX.add(C_FINAL_MATRIX);
                Vector tmpVec = C_FINAL_MATRIX.multiply(INITIAL_TEMP_VECTOR);
                P_FINAL_VECTOR = P_FINAL_VECTOR.add(tmpVec);

                LinearSystemSolver gaussianSolver = new GaussianSolver(H_FINAL_MATRIX);
                temps = gaussianSolver.solve(P_FINAL_VECTOR);


                double tMax = temps.max();
                double tMin = temps.min();
                tInit = (tMin + tMax) / 2;

                System.out.println((int) (k * globalData.getSimStepTime()) + "\t\t" + tInit);

                k++;
                INITIAL_TEMP_VECTOR = temps;
            }


    }

}

