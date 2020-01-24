public class FEM implements Runnable {
    private int size=2;
    private double[][] jacobiArray;
    private double[][] local_H_matrix;
//todo: H matrix i te sprawy
    public void calcH_Matrix(Grid grid,UniversalElement universalElement){
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                local_H_matrix[i][j]=0;

            }
        }

    }
    public void calc_dN_dX() {

    }
    public void calc_dN_dY(){

    }
    public void  calcJacobi(Grid grid,UniversalElement universalElement,GlobalData globalData){


    }

    @Override
    public void run() {
        GlobalData globalData=new GlobalData();
        Grid grid=new Grid(globalData);
        UniversalElement universalElement=new UniversalElement();
        jacobiArray =new double[size][size];
        calcJacobi(grid,universalElement,globalData);
    }
}
