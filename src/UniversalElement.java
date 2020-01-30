import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class UniversalElement {
    private int size;
    private int jacobiSize;

    private Point2D.Double[] localPoints;
    private Point2D.Double[] globalPoints;
    private double[][] shapeFunctions;
    private double[][] ksiArray;
    private double[][] etaArray;
    private double[][] jacobiArray;
    private double[][] reverseJacobiArray;
    private Node[] tempNodes;
    private double[][] dN_dXArray;
    private double[][] dN_dYArray;
    private double[][] H_Matrix;
    private double[][] C_Matrix;



    public UniversalElement() {
        this.size=4;
        this.jacobiSize=2;

        this.shapeFunctions=new double[size][size];
        this.etaArray=new double[size][size];
        this.ksiArray=new double[size][size];
        this.localPoints =new Point2D.Double[size];
        this.globalPoints=new Point2D.Double[size];
        this.jacobiArray=new double[jacobiSize][jacobiSize];
        this.tempNodes=new Node[size];
        this.reverseJacobiArray=new double[jacobiSize][jacobiSize];
        this.dN_dXArray=new double[size][size];
        this.dN_dYArray =new double[size][size];
        this.H_Matrix=new double[size][size];
        this.C_Matrix=new double[size][size];

        calcLocalPoints();
        System.out.println("\nShape Funcions Array");
       // calcShapeFunctions();
        System.out.println("\ndN_dKsi Array");
        calcKsiArray();
        System.out.println("\ndN_dEta Array");
        calcEtaArray();

    }
    //--------------funkcje do ustawienia poczatkowych wartosci lub stalych--------------------
    public void calcLocalPoints() {
        this.localPoints[0]=new Point2D.Double(-1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3));
        this.localPoints[1]=new Point2D.Double(1.0 / Math.sqrt(3), -1.0 / Math.sqrt(3));
        this.localPoints[2]=new Point2D.Double(1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3));
        this.localPoints[3]=new Point2D.Double(-1.0 / Math.sqrt(3), 1.0 / Math.sqrt(3));

    }
    public void calcGlobalPoints(Grid grid,int numberOfElement){
        for (int i = 0; i <grid.getElements()[numberOfElement].getIdOfNodes().length ; i++) {

            tempNodes[i]=grid.getNodes()[grid.getElements()[numberOfElement].getIdOfNodes()[i]];
        }
        //--------------------testowe globalpoint-------------------------------------
        this.globalPoints[0]=new Point2D.Double(0.0,0.0);
        this.globalPoints[1]=new Point2D.Double(0.025,0.0);
        this.globalPoints[2]=new Point2D.Double(0.025,0.025);
        this.globalPoints[3]=new Point2D.Double(0,0.025);

//        this.globalPoints[0]=new Point2D.Double(tempNodes[0].getX(),tempNodes[0].getY());
//        this.globalPoints[1]=new Point2D.Double(tempNodes[1].getX(),tempNodes[1].getY());
//        this.globalPoints[2]=new Point2D.Double(tempNodes[2].getX(),tempNodes[2].getY());
//        this.globalPoints[3]=new Point2D.Double(tempNodes[3].getX(),tempNodes[3].getY());


    }


    //---------------funkcje dodadkowe (do sprawdzenia poprawnosci)----------------------------
    public void calcKsiArray(){
        for (int i = 0; i <size ; i++) {
          ksiArray[i][0]=calc_dN1_dKsi(localPoints[i]);
          ksiArray[i][1]=calc_dN2_dKsi(localPoints[i]);
          ksiArray[i][2]=calc_dN3_dKsi(localPoints[i]);
          ksiArray[i][3]=calc_dN4_dKsi(localPoints[i]);
        }
        print2DArray(ksiArray,size);
    }
    public void calcEtaArray(){

        for (int i = 0; i <size ; i++) {
            etaArray[i][0]=calc_dN1_dEta(localPoints[i]);
            etaArray[i][1]=calc_dN2_dEta(localPoints[i]);
            etaArray[i][2]=calc_dN3_dEta(localPoints[i]);
            etaArray[i][3]=calc_dN4_dEta(localPoints[i]);
        }
        print2DArray(etaArray,size);
    }
    public void print2DArray(double[][] array2D,int size){
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                System.out.print(array2D[i][j]+"  ");
            }
            System.out.println("");
        }
    }

    //-------------------------funkcje pomocnicze---------------------------------------------
    public double dx_dKsi(Point2D.Double localPoint) {

        return calc_dN1_dKsi(localPoint)*globalPoints[0].getX()+calc_dN2_dKsi(localPoint)*globalPoints[1].getX()+calc_dN3_dKsi(localPoint)*globalPoints[2].getX()+calc_dN4_dKsi(localPoint)*globalPoints[3].getX();
    }
    public double dx_dEta(Point2D.Double localPoint) {
        return calc_dN1_dEta(localPoint)*globalPoints[0].getX()+calc_dN2_dEta(localPoint)*globalPoints[1].getX()+calc_dN3_dEta(localPoint)*globalPoints[2].getX()+calc_dN4_dEta(localPoint)*globalPoints[3].getX();
    }
    public double dy_dEta(Point2D.Double localPoint) {
        return calc_dN1_dEta(localPoint)*globalPoints[0].getY()+calc_dN2_dEta(localPoint)*globalPoints[1].getY()+calc_dN3_dEta(localPoint)*globalPoints[2].getY()+calc_dN4_dEta(localPoint)*globalPoints[3].getY();
    }
    public double dy_dKsi(Point2D.Double localPoint) {
        return calc_dN1_dKsi(localPoint)*globalPoints[0].getY()+calc_dN2_dKsi(localPoint)*globalPoints[1].getY()+calc_dN3_dKsi(localPoint)*globalPoints[2].getY()+calc_dN4_dKsi(localPoint)*globalPoints[3].getY();
    }

    public double calc_dN1_dKsi(Point2D.Double point) {
        return -0.25 * (1 - point.getY());
    }
    public double calc_dN2_dKsi(Point2D.Double point) {
        return 0.25 * (1 - point.getY());
    }
    public double calc_dN3_dKsi(Point2D.Double point) {
        return 0.25 * (1 + point.getY());
    }
    public double calc_dN4_dKsi(Point2D.Double point) {
        return -0.25 * (1 + point.getY());
    }

    public double calc_dN1_dEta(Point2D.Double point) {
        return -0.25 * (1 - point.getX());
    }
    public double calc_dN2_dEta(Point2D.Double point) {
        return -0.25 * (1 + point.getX());
    }
    public double calc_dN3_dEta(Point2D.Double point) {
        return 0.25 * (1 + point.getX());
    }
    public double calc_dN4_dEta(Point2D.Double point) {
        return 0.25 * (1 - point.getX());
    }

    //--------------------------funkcje potrzebne do policzenia macierzy H lokalnej-------------------
    public double[][] calc_dN_dX(int integralPointNumber) {

        for (int i = 0; i <size ; i++) { //i -numer funkcji ksztaltu;
            dN_dXArray[integralPointNumber][i]=(1.0/calcDetJ(integralPointNumber))*((calcJacobi(integralPointNumber)[1][1]*ksiArray[integralPointNumber][i])-calcJacobi(integralPointNumber)[0][1]*etaArray[integralPointNumber][i]);
        //    System.out.print(dN_dXArray[integralPointNumber][i]+"     ");
        }
       // System.out.println("");
        return dN_dXArray;
    }
    public double[][] calc_dN_dY(int integralPointNumber){

        for (int i = 0; i <size ; i++) {

                dN_dYArray[integralPointNumber][i]= (1.0/calcDetJ(integralPointNumber) )* (calcJacobi(integralPointNumber)[0][0]*etaArray[integralPointNumber][i]-calcJacobi(integralPointNumber)[1][0]*ksiArray[integralPointNumber][i]);
             //   System.out.print(dN_dYArray[integralPointNumber][i]+"     ");
        }
      //  System.out.println("");
        return dN_dYArray;

    }
    public double[][]  calcJacobi(int integralPointNumber){
        jacobiArray[0][0]=dx_dKsi( localPoints[integralPointNumber]);  //integralPointNumber - numer punku calkowania [0,1,2,3]
        jacobiArray[0][1]=dx_dEta(localPoints[integralPointNumber]);
        jacobiArray[1][0]=dy_dKsi( localPoints[integralPointNumber]);
        jacobiArray[1][1]=dy_dEta( localPoints[integralPointNumber]);
       // print2DArray(jacobiArray,jacobiSize);
        return jacobiArray;
    }
    public double calcDetJ(int integralPointNumber){
        return calcJacobi(integralPointNumber)[0][0] * calcJacobi(integralPointNumber)[1][1] - calcJacobi(integralPointNumber)[0][1] * calcJacobi(integralPointNumber)[1][0];
    }
    public void calcReverseJacobi(int integralPointNumber) {
        for (int i = 0; i <jacobiSize ; i++) {
            for (int j = 0; j <jacobiSize ; j++) {
                reverseJacobiArray[i][j]=jacobiArray[i][j]/calcDetJ(integralPointNumber);
                System.out.print(reverseJacobiArray[i][j]);
            }
            System.out.println("");
        }
    }
    //    public double[][] calc_dN_dX_T_detJ(int integralPointNumber,int c){
//        double[][] tempArr=new double[size][size];
//        for (int j = 0; j <size ; j++) {
//            for (int i = 0; i <size ; i++) {
//                tempArr[integralPointNumber][i]=calcDetJ(integralPointNumber)*(calc_dN_dX(integralPointNumber)[integralPointNumber][j]*calc_dN_dX(integralPointNumber)[integralPointNumber][i]);
//                System.out.print(tempArr[integralPointNumber][i]+"    ");
//            }
//            System.out.println();
//        }
//        System.out.println();
//
//        print2DArray(tempArr,size);
//        return tempArr;
//    }
//    public double[][] calc_dN_dY_T_detJ(int integralPointNumber)
//    {
//        double[][] tempArr=new double[size][size];
//        for (int j = 0; j <size ; j++) {
//            for (int i = 0; i <size ; i++) {
//                tempArr[integralPointNumber][i] = calcDetJ(integralPointNumber) * (calc_dN_dY(integralPointNumber)[integralPointNumber][j] * calc_dN_dY(integralPointNumber)[integralPointNumber][i]);
//            }
//        }
//        return tempArr;
//    }
    public double[][] calcLocal_H_matrix(int integralPointNumber, int k) { //k-conductivity
        double[][] local_H_matrix=new double[size][size];
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                local_H_matrix[i][j]=k*calcDetJ(integralPointNumber)*(calc_dN_dX(integralPointNumber)[integralPointNumber][i]*calc_dN_dX(integralPointNumber)[integralPointNumber][j]+calc_dN_dY(integralPointNumber)[integralPointNumber][i]*calc_dN_dY(integralPointNumber)[integralPointNumber][j]);
            }
        }
     //   print2DArray(local_H_matrix,size);
        return local_H_matrix;
    }
    public double[][] calc_H_Matrix(int k) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                H_Matrix[i][j] = (calcLocal_H_matrix(0, k)[i][j]) + (calcLocal_H_matrix(1, k)[i][j]) + (calcLocal_H_matrix(2, k)[i][j]) + (calcLocal_H_matrix(3, k)[i][j]);
            }
        }
        System.out.println("H Matrix");
        print2DArray(H_Matrix, size);
        return H_Matrix;
    }
    //------------------------------Macierz C-----------------------------------------------------------
    public double[][] calcShapeFunctions(int integralPointNumber){
        double[][] tempShapeFunctions=new double[size][size];

        tempShapeFunctions[integralPointNumber][0]=0.25 * (1 - localPoints[integralPointNumber].getX()) * (1 - localPoints[integralPointNumber].getY());
        tempShapeFunctions[integralPointNumber][1]=0.25 * (1 + localPoints[integralPointNumber].getX()) * (1 - localPoints[integralPointNumber].getY());
        tempShapeFunctions[integralPointNumber][2]=0.25 * (1 + localPoints[integralPointNumber].getX()) * (1 + localPoints[integralPointNumber].getY());
        tempShapeFunctions[integralPointNumber][3]=0.25 * (1 - localPoints[integralPointNumber].getX()) * (1 + localPoints[integralPointNumber].getY());

        // print2DArray(shapeFunctions,size);
        return tempShapeFunctions;
    }
    public double[][] calcLocal_C_matrix(int integralPointNumber,int c,int ro) {
        double[][] local_C_matrix=new double[size][size];
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                local_C_matrix[i][j]=(calcShapeFunctions(integralPointNumber)[0][i]*calcShapeFunctions(integralPointNumber)[0][j]*calcDetJ(integralPointNumber)*c*ro)+calcShapeFunctions(integralPointNumber)[1][i]*calcShapeFunctions(integralPointNumber)[1][j]*calcDetJ(integralPointNumber)*c*ro+calcShapeFunctions(integralPointNumber)[2][i]*calcShapeFunctions(integralPointNumber)[2][j]*calcDetJ(integralPointNumber)*c*ro+calcShapeFunctions(integralPointNumber)[3][i]*calcShapeFunctions(integralPointNumber)[2][j]*calcDetJ(integralPointNumber)*c*ro+calcShapeFunctions(integralPointNumber)[3][i]*calcShapeFunctions(integralPointNumber)[3][j]*calcDetJ(integralPointNumber)*c*ro;
            }
        }
        return local_C_matrix;
    }
    public double[][] calc_C_Matrix(int c,int ro){
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                C_Matrix[i][j]=calcLocal_C_matrix(0,c,ro)[i][j]+calcLocal_C_matrix(1,c,ro)[i][j]+calcLocal_C_matrix(2,c,ro)[i][j]+calcLocal_C_matrix(3,c,ro)[i][j];
            }
        }
        System.out.println("C Matrix :");
        print2DArray(C_Matrix,size);
    return C_Matrix;
    }
    //------------------------------Macierz H_BC--------------------------------------------------------
    public double calcDetJ1D(double l){
        return 0.5*l;
    }
    public Point2D.Double[] findPCsDependOnBcCondition(){
        ArrayList<Point2D.Double> listOfPoints=new ArrayList<>();


        if(tempNodes[0].isBC()&&tempNodes[1].isBC()){
            Point2D.Double pc1B=new Point2D.Double(-1/Math.sqrt(3),-1);
            Point2D.Double pc2B=new Point2D.Double(1/Math.sqrt(3),-1);
            listOfPoints.add(pc1B);
            listOfPoints.add(pc2B);
        }
        if(tempNodes[1].isBC()&&tempNodes[2].isBC()) {
            Point2D.Double pc1R = new Point2D.Double(1, -1 / Math.sqrt(3));
            Point2D.Double pc2R = new Point2D.Double(1, 1 / Math.sqrt(3));
            listOfPoints.add(pc1R);
            listOfPoints.add(pc2R);
        }
        if(tempNodes[2].isBC()&&tempNodes[3].isBC()){
            Point2D.Double pc1U=new Point2D.Double(-1/Math.sqrt(3),1);
            Point2D.Double pc2U=new Point2D.Double(1/Math.sqrt(3),1);
            listOfPoints.add(pc1U);
            listOfPoints.add(pc2U);
        }
        if(tempNodes[3].isBC()&&tempNodes[1].isBC()){
            Point2D.Double pc1L=new Point2D.Double(-1,-1/Math.sqrt(3));
            Point2D.Double pc2L=new Point2D.Double(-1,1/Math.sqrt(3));
            listOfPoints.add(pc1L);
            listOfPoints.add(pc2L);
        }
        Point2D.Double[] pcSArray=new Point2D.Double[listOfPoints.size()];
        for (int i = 0; i <listOfPoints.size() ; i++) {
            pcSArray[i]=listOfPoints.get(i);
        }
        return pcSArray;
    }
    public double calcL(){
        //pzryjmujemy ze mamy kwadratowe elementy;
        return tempNodes[0].getX()-tempNodes[1].getX();
    }
    public double[] calcShapeVector(Point2D.Double point){
        double[] shapeVector=new double[4];
        shapeVector[0]=0.25 * (1 - point.getX()) * (1 - point.getY());
        shapeVector[0]=0.25 * (1 + point.getX()) * (1 - point.getY());
        shapeVector[0]=0.25 * (1 + point.getX()) * (1 + point.getY());
        shapeVector[0]=0.25 * (1 - point.getX()) * (1 + point.getY());
        return shapeVector;
    }

    public void calcLocal_H_BC_Matrix(int integralPointNumber,int alfa) {
        double l=calcL();

        double[][] local_H_BC_matrix=new double[size][size];
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                for (int k = 0; k <findPCsDependOnBcCondition().length ; k++) {

                 //   local_H_BC_matrix[i][j] = calcShapeVector(findPCsDependOnBcCondition()[i])[i]*calcShapeFunctions(integralPointNumber)[k][j]+calcDetJ1D(l);
                }
            }
        }
      //  print2DArray(local_H_BC_matrix,size);
    }


    //-----------------------------gettery i settery-----------------------------------------------------
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Point2D.Double[] getLocalPoints() {
        return localPoints;
    }

    public void setLocalPoints(Point2D.Double[] localPoints) {
        this.localPoints = localPoints;
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
