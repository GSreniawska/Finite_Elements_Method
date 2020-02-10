

import com.opencsv.CSVWriter;
import org.la4j.LinearAlgebra;
import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.inversion.GaussJordanInverter;
import org.la4j.linear.GaussianSolver;
import org.la4j.linear.JacobiSolver;
import org.la4j.linear.LeastSquaresSolver;
import org.la4j.linear.LinearSystemSolver;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
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
        Scanner input = new Scanner(System.in);

        int numberOfSimulation=-1;
        while(numberOfSimulation!=4){
            System.out.println("Which simulation (1 or 2) or wall of industrial oven (3) or exit (4)?");
            try {
                numberOfSimulation = input.nextInt();
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

            GlobalData globalData = new GlobalData(simData, numberOfSimulation);
            UniversalElement universalElement = new UniversalElement();
            Grid grid = new Grid(globalData, universalElement, numberOfSimulation);
            if (numberOfSimulation == 3) {
                simulationExtra_Project(grid, globalData);
            } else {
                simulationBasic(grid, globalData);
            }
        }

    }
    public void simulationBasic(Grid grid,GlobalData globalData){
        finalSize = globalData.getnH() * globalData.getnW();
        H_Matrix = new double[finalSize][finalSize];
        C_Matrix = new double[finalSize][finalSize];
        P_Vector=new double[finalSize];
        double[] tArray = new double[globalData.getnN()];
        //ustawiamy wektor poczatkowy z temperaturą



        Vector INITIAL_TEMP_VECTOR=Vector.fromArray(tArray);
        for (int i = 0; i <finalSize ; i++) {
            INITIAL_TEMP_VECTOR.set(i,globalData.getTInitial());
        }
        int k=1;

        H_Matrix=grid.getH_Global_Matrix();
        C_Matrix=grid.getC_Global_Matrix();
        P_Vector=grid.getP_Global_Vector();

        System.out.println("\nTime[s]\tMinTemp[C]\tMaxTemp[C]");
        for (int i = 0; i <globalData.getSimTime()/globalData.getSimStepTime();  i++) {

            Matrix H_FINAL_MATRIX=Matrix.from2DArray(H_Matrix);

            Matrix C_FINAL_MATRIX=Matrix.from2DArray(C_Matrix);
            C_FINAL_MATRIX=C_FINAL_MATRIX.divide(globalData.getSimStepTime());

            Vector P_FINAL_VECTOR=Vector.fromArray(P_Vector).multiply(-1);
            H_FINAL_MATRIX=H_FINAL_MATRIX.add(C_FINAL_MATRIX);
            Vector tmpVec=C_FINAL_MATRIX.multiply(INITIAL_TEMP_VECTOR);
            P_FINAL_VECTOR=P_FINAL_VECTOR.add(tmpVec);

            LinearSystemSolver gaussianSolver=new GaussianSolver(H_FINAL_MATRIX) {
            };
            Vector temps=gaussianSolver.solve(P_FINAL_VECTOR);

            double tMax= temps.max();
            double tMin=temps.min();

            System.out.println((int)(k*globalData.getSimStepTime())+"\t\t"+df2.format(tMin)+"\t\t\t"+df2.format(tMax));
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
        HashMap<Integer,Double> mapOfTimeAndTemp=new HashMap<>();
        int temp;
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
                temp=(int) (k * globalData.getSimStepTime());
                mapOfTimeAndTemp.put(temp,tInit);

                System.out.println((int) (k * globalData.getSimStepTime()) + "\t\t" + tInit);

                k++;
                INITIAL_TEMP_VECTOR = temps;
            }
            writeDataToCSVFormat(mapOfTimeAndTemp);

    }
    public void writeDataToCSVFormat(HashMap<Integer,Double > mapOfTimeAndTemp) {
        File file = new File("F:\\Java\\Projects\\Sreniawska_Gabriela_MES\\dataCSV.csv");
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);

            mapOfTimeAndTemp.forEach((k, v) -> {
                String[] header = {k.toString(),v.toString().replace(".",",")};

                writer.writeNext(header);

            });


            writer.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

