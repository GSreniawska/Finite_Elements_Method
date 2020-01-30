public class FEM implements Runnable {
    private int size;
    private double[] P_Global_Vector;
    private double[][] H_Global_Matrix;
    private double[][] C_Global_Matrix;


    public FEM() {
        this.size=4;

    }

    @Override
    public void run() {
        GlobalData globalData=new GlobalData();
        System.out.println(globalData.toString());
        Grid grid=new Grid(globalData);
        UniversalElement universalElement=new UniversalElement();
        //Point2D.Double globalPoint=new Point2D.Double(grid.getNodes()[1].getX(),grid.getNodes()[1].getY());
        universalElement.calcGlobalPoints(grid,0);
        for (int i = 0; i <size ; i++) {
            System.out.println("Jacobi array for "+i+" integral point");
            universalElement.calcJacobi(i);
            universalElement.calcDetJ(i);
        }
        aggregation(globalData,grid,universalElement);
//        universalElement.calcReverseJacobi(1);
//        double[][] dN_dXArray = new double[size][size];
//        double[][] dN_dYArray = new double[size][size];
//
//        for (int j = 0; j < size; j++) {
//
//            //System.out.println("H matrix for "+i+" integral point");
//            universalElement.calc_dN_dX(j);
//
//        }
//        System.out.println("");
//        for (int i = 0; i < size; i++) {
//            universalElement.calc_dN_dY(i);
//        }
//        System.out.println("");
//
//            universalElement.calc_H_Matrix((int)globalData.getK());
//            universalElement.calc_C_Matrix((int)globalData.getC(),(int)globalData.getRo());
//      // for (int i = 0; i <8 ; i++) {
//            universalElement.calc_H_BC_Matrix(25);
//            universalElement.calc_P_Vector((int)globalData.getAlfa());


        //}





    }
    public void aggregation(GlobalData globalData,Grid grid,UniversalElement universalElement){
        P_Global_Vector=new double[globalData.getnE()];
        H_Global_Matrix=new double[globalData.getnE()][globalData.getnE()];
        C_Global_Matrix=new double[globalData.getnE()][globalData.getnE()];
        for (int i = 0; i < globalData.getnE(); i++) {
            int[] id=new int[size];
            for (int j = 0; j <size ; j++) {
                id[j]=grid.getElements()[i].getIdOfNodes()[j];
            }
            for (int j = 0; j <size ; j++) {
                P_Global_Vector[id[j]]+=universalElement.calc_P_Vector((int)globalData.getAlfa())[j];
                for (int k = 0; k <size ; k++) {
                    H_Global_Matrix[id[j]][id[k]]+=universalElement.calc_H_Matrix((int)globalData.getK())[j][k];
                    C_Global_Matrix[id[j]][id[k]]+=universalElement.calc_C_Matrix((int)globalData.getC(),(int)globalData.getRo())[j][k];
                }           
            }
            
        }
        System.out.println("P Vector:");
        for (int i = 0; i <globalData.getnE() ; i++) {
            System.out.print(P_Global_Vector[i]+"     ");
        }
        System.out.println("H Matrix:");
        universalElement.print2DArray(H_Global_Matrix,globalData.getnE());
        System.out.println("C Matrix:");
        universalElement.print2DArray(C_Global_Matrix,globalData.getnE());
    }
}
