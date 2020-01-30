public class FEM implements Runnable {
    private int size;


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
        universalElement.calcReverseJacobi(1);
        double[][] dN_dXArray = new double[size][size];
        double[][] dN_dYArray = new double[size][size];

        for (int j = 0; j < size; j++) {

            //System.out.println("H matrix for "+i+" integral point");
            universalElement.calc_dN_dX(j);

        }
        System.out.println("");
        for (int i = 0; i < size; i++) {
            universalElement.calc_dN_dY(i);
        }
        System.out.println("");

            universalElement.calc_H_Matrix((int)globalData.getK());
            universalElement.calc_C_Matrix((int)globalData.getC(),(int)globalData.getRo());
        for (int i = 0; i <8 ; i++) {
            universalElement.calcLocal_H_BC_Matrix(i,25);
        }





    }
}
