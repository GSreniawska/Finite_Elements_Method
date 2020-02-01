import java.awt.geom.Point2D;
import java.util.ArrayList;

public class UniversalElement {
    private int size;
    private int jacobiSize;
    private Grid grid;

    private Point2D.Double[] localPoints;
    private double[][] shapeFunctions;
    private double[][] ksiArray;
    private double[][] etaArray;
    private double[][] jacobiArray;
    private Node[] tempNodes;
    private double[][] dN_dXArray;
    private double[][] dN_dYArray;
    private double[][] H_Matrix;
    private double[][] C_Matrix;
    private  double[][] local_C_matrix;



    public UniversalElement(Grid grid) {
        this.size=4;
        this.jacobiSize=2;
        this.grid=grid;
        this.shapeFunctions =new double[size][size];
        this.etaArray=new double[size][size];
        this.ksiArray=new double[size][size];
        this.localPoints =new Point2D.Double[size];
        this.localPoints =new Point2D.Double[size];
        this.jacobiArray=new double[jacobiSize][jacobiSize];
        this.tempNodes=new Node[size];
        this.dN_dXArray=new double[size][size];
        this.dN_dYArray =new double[size][size];
        this.H_Matrix=new double[size][size];
        this.C_Matrix=new double[size][size];
        this.local_C_matrix=new double[size][size];


       // calcGlobalPoints();
        calcLocalPoints();
        calcShapeFunctions();

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

        return calc_dN1_dKsi(localPoint)* tempNodes[0].getX()+calc_dN2_dKsi(localPoint)* tempNodes[1].getX()+calc_dN3_dKsi(localPoint)* tempNodes[2].getX()+calc_dN4_dKsi(localPoint)* tempNodes[3].getX();
    }
    public double dx_dEta(Point2D.Double localPoint) {
        return calc_dN1_dEta(localPoint)* tempNodes[0].getX()+calc_dN2_dEta(localPoint)* tempNodes[1].getX()+calc_dN3_dEta(localPoint)* tempNodes[2].getX()+calc_dN4_dEta(localPoint)* tempNodes[3].getX();
    }
    public double dy_dEta(Point2D.Double localPoint) {
        return calc_dN1_dEta(localPoint)* tempNodes[0].getY()+calc_dN2_dEta(localPoint)* tempNodes[1].getY()+calc_dN3_dEta(localPoint)* tempNodes[2].getY()+calc_dN4_dEta(localPoint)* tempNodes[3].getY();
    }
    public double dy_dKsi(Point2D.Double localPoint) {
        return calc_dN1_dKsi(localPoint)* tempNodes[0].getY()+calc_dN2_dKsi(localPoint)* tempNodes[1].getY()+calc_dN3_dKsi(localPoint)* tempNodes[2].getY()+calc_dN4_dKsi(localPoint)* tempNodes[3].getY();
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

    //------------------------------Macierz H-----------------------------------------------------------
    public double[][] calc_dN_dX(int integralPointNumber) {


        for (int i = 0; i <size ; i++) { //i -numer funkcji ksztaltu;
            dN_dXArray[integralPointNumber][i]=(1.0/calcDetJ(integralPointNumber))*((calcJacobi(integralPointNumber)[1][0]*ksiArray[integralPointNumber][i])-calcJacobi(integralPointNumber)[1][1]*etaArray[integralPointNumber][i]);

        }
        return dN_dXArray;
    }
    public double[][] calc_dN_dY(int integralPointNumber){

        for (int i = 0; i <size ; i++) {

                dN_dYArray[integralPointNumber][i]= (1.0/calcDetJ(integralPointNumber) )* (calcJacobi(integralPointNumber)[0][0]*etaArray[integralPointNumber][i]-calcJacobi(integralPointNumber)[0][1]*ksiArray[integralPointNumber][i]);
        }
        return dN_dYArray;

    }

    public double[][]  calcJacobi(int integralPointNumber){
        jacobiArray[0][0]=dx_dKsi( localPoints[integralPointNumber]);  //integralPointNumber - numer punku calkowania [0,1,2,3]
        jacobiArray[0][1]=dx_dEta(localPoints[integralPointNumber]);
        jacobiArray[1][0]=dy_dEta( localPoints[integralPointNumber]);
        jacobiArray[1][1]=dy_dKsi( localPoints[integralPointNumber]);
       // print2DArray(jacobiArray,jacobiSize);
        return jacobiArray;
    }
    public double calcDetJ(int integralPointNumber){
        return dx_dKsi(localPoints[integralPointNumber]) * dy_dEta(localPoints[integralPointNumber]) - dy_dKsi(localPoints[integralPointNumber]) * dx_dEta(localPoints[integralPointNumber]);
    }
    public double[][] calcLocal_H_matrix(int integralPointNumber, int k) { //k-conductivity
        double[][] local_H_matrix=new double[size][size];
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                local_H_matrix[i][j]=calcDetJ(integralPointNumber)*(calc_dN_dX(integralPointNumber)[integralPointNumber][i]*calc_dN_dX(integralPointNumber)[integralPointNumber][j]+calc_dN_dY(integralPointNumber)[integralPointNumber][i]*calc_dN_dY(integralPointNumber)[integralPointNumber][j]);
            }
        }
        return local_H_matrix;
    }
    public double[][] calc_H_Matrix(int k,Node[] tempNodes) {
        this.tempNodes=tempNodes;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                H_Matrix[i][j] =k*((this.calcLocal_H_matrix(0, k)[i][j]) + (this.calcLocal_H_matrix(1, k)[i][j]) + (this.calcLocal_H_matrix(2, k)[i][j]) + (this.calcLocal_H_matrix(3, k)[i][j])) ;
            }
        }

       // System.out.println("H Matrix");
        //print2DArray(H_Matrix, size);
        return H_Matrix;
    }
    //------------------------------Macierz C-----------------------------------------------------------
    public void  calcShapeFunctions(){
        for (int i = 0; i < size; i++) {

            shapeFunctions[i][0]=0.25 * (1 - localPoints[i].getX()) * (1 - localPoints[i].getY());
            shapeFunctions[i][1]=0.25 * (1 + localPoints[i].getX()) * (1 - localPoints[i].getY());
            shapeFunctions[i][2]=0.25 * (1 + localPoints[i].getX()) * (1 + localPoints[i].getY());
            shapeFunctions[i][3]=0.25 * (1 - localPoints[i].getX()) * (1 + localPoints[i].getY());
        }
        System.out.println("Shape funs:");
        print2DArray(shapeFunctions,size);
    }
    public double[][] calc_C_Matrix(int c,int ro,Node[] tempNodes){
        this.tempNodes=tempNodes;

        C_Matrix=new double[size][size];

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    C_Matrix[i][j] =c*ro*((shapeFunctions[0][i] * shapeFunctions[0][j]*calcDetJ(0))+(shapeFunctions[1][i]* shapeFunctions[1][j]*calcDetJ(1))+(shapeFunctions[2][i]* shapeFunctions[2][j]*calcDetJ(2))+(shapeFunctions[3][i]* shapeFunctions[3][j]*calcDetJ(3)));
                }
            }


    return C_Matrix;
    }
    //------------------------------Macierz H_BC--------------------------------------------------------
    public double calcDetJ1D(double l){
        return Math.abs(0.5*l);
    }
    public double[][] calcLocal_H_BC_matrix(int alfa, int numberOfPlane,Node[] tempNodes) {
        ArrayList<Point2D.Double> listOfPoints = new ArrayList<>();
        double[][] shapeArray1 = new double[size][size];
        double[][] shapeArray2 = new double[size][size];
        double l;
        double detJ1D;
        double[][] sumPC_H_BCArray = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size ; j++) {

                sumPC_H_BCArray[i][j]=0;
            }
        }


        if (tempNodes[0].isBC() && tempNodes[1].isBC()&&numberOfPlane==1) {
            l = calcL(0, 1,tempNodes);
            detJ1D = calcDetJ1D(l);
            Point2D.Double pc1B = new Point2D.Double((-1 / Math.sqrt(3)), -1.);
            Point2D.Double pc2B = new Point2D.Double((1 / Math.sqrt(3)), -1.);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeArray1[i][j] = 0;
                    shapeArray2[i][j] = 0;
                    sumPC_H_BCArray[i][j]=0;
                }
            }
            shapeArray1[0][0] = alfa * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY());
            shapeArray1[0][1] = alfa * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY());
            shapeArray1[1][0] = alfa * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY());
            shapeArray1[1][1] = alfa * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY());
         //   print2DArray(shapeArray1, size);

            shapeArray2[0][0] = alfa * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY());
            shapeArray2[0][1] = alfa * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY());
            shapeArray2[1][0] = alfa * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY());
            shapeArray2[1][1] = alfa * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY());
         //   print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] = (shapeArray1[i][j] + shapeArray2[i][j]) * detJ1D;
                }
            }

            return sumPC_H_BCArray;

        }
        if (tempNodes[1].isBC() && tempNodes[2].isBC()&&numberOfPlane==2) {
            Point2D.Double pc1R = new Point2D.Double(1, -1 / Math.sqrt(3));
            Point2D.Double pc2R = new Point2D.Double(1, 1 / Math.sqrt(3));
            l = calcL(1, 2,tempNodes);
            detJ1D = calcDetJ1D(l);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeArray1[i][j] = 0;
                    shapeArray2[i][j] = 0;

                }
            }
            shapeArray1[1][1] = alfa * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY());
            shapeArray1[1][2] = alfa * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY());
            shapeArray1[2][1] = alfa * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY());
            shapeArray1[2][2] = alfa * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY());
           // print2DArray(shapeArray1, size);

            shapeArray2[1][1] = alfa * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY());
            shapeArray2[1][2] = alfa * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY());
            shapeArray2[2][1] = alfa * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY());
            shapeArray2[2][2] = alfa * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY());
           // print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] = (shapeArray1[i][j] + shapeArray2[i][j]) * detJ1D;
                }
            }

            return sumPC_H_BCArray;
        }
        if (tempNodes[2].isBC() && tempNodes[3].isBC()&&numberOfPlane==3) {
            Point2D.Double pc1U = new Point2D.Double(-1 / Math.sqrt(3), 1);
            Point2D.Double pc2U = new Point2D.Double(1 / Math.sqrt(3), 1);
            l = calcL(2, 3,tempNodes);
            detJ1D = calcDetJ1D(l);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeArray1[i][j] = 0;
                    shapeArray2[i][j] = 0;

                }
            }
            shapeArray1[2][2] = alfa * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY());
            shapeArray1[2][3] = alfa * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY());
            shapeArray1[3][2] = alfa * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY());
            shapeArray1[3][3] = alfa * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY());
         //   print2DArray(shapeArray1, size);

            shapeArray2[0][0] = alfa * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY());
            shapeArray2[0][1] = alfa * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY());
            shapeArray2[1][0] = alfa * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY());
            shapeArray2[1][1] = alfa * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY());
           // print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] = (shapeArray1[i][j] + shapeArray2[i][j]) * detJ1D;
                }
            }

            return sumPC_H_BCArray;
        }
        if (tempNodes[3].isBC() && tempNodes[0].isBC()&&(numberOfPlane==4)) {
            Point2D.Double pc1L = new Point2D.Double(-1, -1 / Math.sqrt(3));
            Point2D.Double pc2L = new Point2D.Double(-1, 1 / Math.sqrt(3));
            l = calcL(3, 1,tempNodes);
            detJ1D = calcDetJ1D(l);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeArray1[i][j] = 0;
                    shapeArray2[i][j] = 0;

                }
            }
            shapeArray1[0][0] = alfa * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY());
            shapeArray1[0][3] = alfa * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY());
            shapeArray1[3][0] = alfa * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY());
            shapeArray1[3][3] = alfa * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY());
       //     print2DArray(shapeArray1, size);

            shapeArray2[0][0] = alfa * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY());
            shapeArray2[0][3] = alfa * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY());
            shapeArray2[3][0] = alfa * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY());
            shapeArray2[3][3] = alfa * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY());
          //  print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] = (shapeArray1[i][j] + shapeArray2[i][j]) * detJ1D;
                }
            }

            return sumPC_H_BCArray;
        }


      return sumPC_H_BCArray;
    }
    public double calcL(int i1,int i2,Node[] tempNodes){
        //pzryjmujemy ze mamy kwadratowe elementy;
        return Math.sqrt(Math.pow(tempNodes[i1].getX()-tempNodes[i2].getX(),2)+Math.pow(tempNodes[i1].getY()-tempNodes[i2].getY(),2));
    }
    public void calc_H_BC_Matrix(int alfa,Node[] tempNodes) {


        double[][] H_BC_matrix=new double[size][size];
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                for (int k = 1; k <=size ; k++) {

                    H_BC_matrix[i][j]+= calcLocal_H_BC_matrix(alfa,k,tempNodes)[i][j];
                }
            }

        }
        System.out.println("H_BC Matrix:");
        print2DArray(H_BC_matrix,size);
    }
    //-------------------------------Wektor P -----------------------------------------------------------
    public double[] calcLocal_P_vector(int numberOfPlane,int alfa,Node[] tempNodes){
        double[] pLocalVector=new double[size];
        double[] shapeVector1=new double[size];
        double[] shapeVector2=new double[size];
        double[] sumP_Vectors=new double[size];

        double l=0,detJ1D=0;

        if (tempNodes[0].isBC() && tempNodes[1].isBC()&&numberOfPlane==1) {
            l = calcL(0, 1,tempNodes);
            detJ1D = calcDetJ1D(l);
            Point2D.Double pc1B = new Point2D.Double((-1 / Math.sqrt(3)), -1.);
            Point2D.Double pc2B = new Point2D.Double((1 / Math.sqrt(3)), -1.);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeVector1[i] = 0;
                    shapeVector2[i] = 0;
                    sumP_Vectors[i]=0;
                }
            }
            shapeVector1[0] = -alfa * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY());
            shapeVector1[1] = -alfa * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY());
            shapeVector1[2] = -alfa * 0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY());
            shapeVector1[3] = -alfa * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY()) * 0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY());
            //   print2DArray(shapeArray1, size);

            shapeVector2[0] = -alfa * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY());
            shapeVector2[1] = -alfa * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY());
            shapeVector2[2] = -alfa * 0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY());
            shapeVector2[3]= -alfa * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY()) * 0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY());
            //   print2DArray(shapeArray2, size);
            for (int i = 0; i < size; i++) {
                    sumP_Vectors[i] = (shapeVector1[i] + shapeVector2[i]) * detJ1D;
            }
            return sumP_Vectors;
        }
        if (tempNodes[1].isBC() && tempNodes[2].isBC()&&numberOfPlane==2) {
            Point2D.Double pc1R = new Point2D.Double(1, -1 / Math.sqrt(3));
            Point2D.Double pc2R = new Point2D.Double(1, 1 / Math.sqrt(3));
            l = calcL(1, 2,tempNodes);
            detJ1D = calcDetJ1D(l);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeVector1[i] = 0;
                    shapeVector2[i] = 0;

                }
            }
            shapeVector1[0] = -alfa * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY());
            shapeVector1[1] = -alfa * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY());
            shapeVector1[2] = -alfa * 0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY());
            shapeVector1[3] = -alfa * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY()) * 0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY());
            // print2DArray(shapeArray1, size);

            shapeVector2[0] = -alfa * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY());
            shapeVector2[1] = -alfa * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY());
            shapeVector2[2] = -alfa * 0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY());
            shapeVector2[3] =- alfa * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY()) * 0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY());
            // print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {
                sumP_Vectors[i] = (shapeVector1[i] + shapeVector2[i]) * detJ1D;
            }
            return sumP_Vectors;
        }
        if (tempNodes[2].isBC() && tempNodes[3].isBC()&&numberOfPlane==3) {
            Point2D.Double pc1U = new Point2D.Double(-1 / Math.sqrt(3), 1);
            Point2D.Double pc2U = new Point2D.Double(1 / Math.sqrt(3), 1);
            l = calcL(2, 3,tempNodes);
            detJ1D = calcDetJ1D(l);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeVector1[i] = 0;
                    shapeVector2[i] = 0;

                }
            }
            shapeVector1[0]= -alfa * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY());
            shapeVector1[1]= -alfa * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY());
            shapeVector1[2] = -alfa * 0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY());
            shapeVector1[3] = -alfa * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY()) * 0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY());
            //   print2DArray(shapeArray1, size);

            shapeVector2[0] =- alfa * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY());
            shapeVector2[1]= -alfa * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY());
            shapeVector2[2] = -alfa * 0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY());
            shapeVector2[3] = -alfa * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY()) * 0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY());
            // print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {

                    sumP_Vectors[i] = (shapeVector1[i] + shapeVector2[i]) * detJ1D;

            }

            return sumP_Vectors;
        }
        if (tempNodes[3].isBC() && tempNodes[0].isBC()&&(numberOfPlane==4)) {
            Point2D.Double pc1L = new Point2D.Double(-1, -1 / Math.sqrt(3));
            Point2D.Double pc2L = new Point2D.Double(-1, 1 / Math.sqrt(3));
            l = calcL(3, 1,tempNodes);
            detJ1D = calcDetJ1D(l);

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    shapeVector1[i] = 0;
                    shapeVector2[i] = 0;

                }
            }
            shapeVector1[0] = -alfa * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY());
            shapeVector1[1] = -alfa * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY());
            shapeVector1[2] = -alfa * 0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY());
            shapeVector1[3]=- alfa * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY()) * 0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY());
            //     print2DArray(shapeArray1, size);

            shapeVector2[0] = -alfa * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY());
            shapeVector2[1] = -alfa * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY());
            shapeVector2[2] =- alfa * 0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY());
            shapeVector2[3] = -alfa * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY()) * 0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY());
            //  print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {

                    sumP_Vectors[i] = (shapeVector1[i] + shapeVector2[i]) * detJ1D;

            }

            return sumP_Vectors;
        }
return sumP_Vectors;
    }
    public double[] calc_P_Vector(int alfa,int numberOfElement,Node[] tempNodes){
        double[] P_Vector=new double[size];
        for (int i = 0; i < size; i++) {
                P_Vector[i]+=calcLocal_P_vector(i,alfa,tempNodes)[i];
        }
        for (int i = 0; i <size ; i++) {
        //    System.out.print(P_Vector[i]+"    ::    ");
        }
        return P_Vector;
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
