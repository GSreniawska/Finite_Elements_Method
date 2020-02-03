import org.la4j.Matrix;
import org.la4j.Vector;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class UniversalElement {
    private int size;
    private int jacobiSize;
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

    public UniversalElement() {
        this.size=4;
        this.jacobiSize=2;
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
                local_H_matrix[i][j]=calc_dN_dX(integralPointNumber)[integralPointNumber][j]*calc_dN_dX(integralPointNumber)[integralPointNumber][i]+calc_dN_dY(integralPointNumber)[integralPointNumber][j]*calc_dN_dY(integralPointNumber)[integralPointNumber][i];
            }
        }
        return local_H_matrix;
    }
    public double[][] calc_H_Matrix(int k,Node[] tempNodes) {
        H_Matrix=new double[size][size];
        this.tempNodes=tempNodes;
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                H_Matrix[i][j]=0;
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                H_Matrix[i][j] =k*((calcDetJ(0)*calcLocal_H_matrix(0, k)[i][j]) + (calcDetJ(1)*calcLocal_H_matrix(1, k)[i][j]) + (calcDetJ(2)*calcLocal_H_matrix(2, k)[i][j]) + (calcDetJ(3)*calcLocal_H_matrix(3, k)[i][j])) ;
            }
        }


      print2DArray(H_Matrix, size);
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
                    C_Matrix[i][j] =((shapeFunctions[0][i] * shapeFunctions[0][j]*calcDetJ(0))+(shapeFunctions[1][i]* shapeFunctions[1][j]*calcDetJ(1))+(shapeFunctions[2][i]* shapeFunctions[2][j]*calcDetJ(2))+(shapeFunctions[3][i]* shapeFunctions[3][j]*calcDetJ(3)));
                }
            }
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                C_Matrix[i][j]*=c*ro;
            }
        }


    return C_Matrix;
    }
    //------------------------------Macierz H_BC--------------------------------------------------------
    public double calcDetJ1D(double l){
        return Math.abs(0.5*l);
    }
    public double[][] calcLocal_H_BC_matrix(int alfa,Node[] tempNodes) {
        double[] shapeArray1 = new double[size];
        double[] shapeArray2 = new double[size];
        double l;
        double detJ1D;
        double[][] sumPC_H_BCArray = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j <size ; j++) {

                sumPC_H_BCArray[i][j]=0;
            }
        }
        Matrix sumHBC=Matrix.from2DArray(sumPC_H_BCArray);


        if (tempNodes[0].isBC() && tempNodes[1].isBC()) {
            l = calcL(0, 1,tempNodes);

            detJ1D = calcDetJ1D(l);

            Point2D.Double pc1B = new Point2D.Double((-1 / Math.sqrt(3)), -1.);
            Point2D.Double pc2B = new Point2D.Double((1 / Math.sqrt(3)), -1.);

            Vector s1=Vector.fromArray(shapeArray1);
            s1.set(0,0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY()));
            s1.set(1,0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY()));
            s1.set(2,0.25 * (1 + pc1B.getX()) * (1 + pc1B.getY()));
            s1.set(3,0.25 * (1 - pc1B.getX()) * (1 + pc1B.getY()));

            Vector s2 = Vector.fromArray(shapeArray2);
            s2.set(0,0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY()));
            s2.set(1,0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY()));
            s2.set(2,0.25 * (1 + pc2B.getX()) * (1 + pc2B.getY()));
            s2.set(3,0.25 * (1 - pc2B.getX()) * (1 + pc2B.getY()));

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] += (s1.get(i)*s1.get(j)+(s2.get(i)*s2.get(j)))*(detJ1D) ;
                }
            }
        }
        if (tempNodes[1].isBC() && tempNodes[2].isBC()) {
            Point2D.Double pc1R = new Point2D.Double(1, -1 / Math.sqrt(3));
            Point2D.Double pc2R = new Point2D.Double(1, 1 / Math.sqrt(3));
            l = calcL(1, 2,tempNodes);
            detJ1D = calcDetJ1D(l);
            for (int i = 0; i <size ; i++) {
                shapeArray1[i]=0;
                shapeArray2[i]=0;
            }

            Vector s1=Vector.fromArray(shapeArray1);
            s1.set(0,0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY()));
            s1.set(1,0.25 * (1 + pc2R.getX()) * (1 - pc1R.getY()));
            s1.set(2,0.25 * (1 + pc1R.getX()) * (1 + pc1R.getY()));
            s1.set(3,0.25 * (1 - pc1R.getX()) * (1 + pc1R.getY()));

            Vector s2 = Vector.fromArray(shapeArray2);
            s2.set(0,0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY()));
            s2.set(1,0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY()));
            s2.set(2,0.25 * (1 + pc2R.getX()) * (1 + pc2R.getY()));
            s2.set(3,0.25 * (1 - pc2R.getX()) * (1 + pc2R.getY()));

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] += (s1.get(i)*s1.get(j)+(s2.get(i)*s2.get(j)))*(detJ1D) ;
                }
            }


        }
        if (tempNodes[2].isBC() && tempNodes[3].isBC()) {
            Point2D.Double pc1U = new Point2D.Double(-1 / Math.sqrt(3), 1);
            Point2D.Double pc2U = new Point2D.Double(1 / Math.sqrt(3), 1);
            l = calcL(2, 3,tempNodes);
            detJ1D = calcDetJ1D(l);
            for (int i = 0; i <size ; i++) {
                shapeArray1[i]=0;
                shapeArray2[i]=0;
            }

            Vector s1=Vector.fromArray(shapeArray1);
            s1.set(0,0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY()));
            s1.set(1,0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY()));
            s1.set(2,0.25 * (1 + pc1U.getX()) * (1 + pc1U.getY()));
            s1.set(3,0.25 * (1 - pc1U.getX()) * (1 + pc1U.getY()));

            Vector s2 = Vector.fromArray(shapeArray2);
            s2.set(0,0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY()));
            s2.set(1,0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY()));
            s2.set(2,0.25 * (1 + pc2U.getX()) * (1 + pc2U.getY()));
            s2.set(3,0.25 * (1 - pc2U.getX()) * (1 + pc2U.getY()));

            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] += (s1.get(i)*s1.get(j)+(s2.get(i)*s2.get(j)))*(detJ1D) ;
                }
            }
        }
        if (tempNodes[3].isBC() && tempNodes[0].isBC()) {
            Point2D.Double pc1L = new Point2D.Double(-1, -1 / Math.sqrt(3));
            Point2D.Double pc2L = new Point2D.Double(-1, 1 / Math.sqrt(3));
            l = calcL(3, 0,tempNodes);
            detJ1D = calcDetJ1D(l);
            for (int i = 0; i <size ; i++) {
                shapeArray1[i]=0;
                shapeArray2[i]=0;
            }
            Vector s1=Vector.zero(4);
            s1.set(0,0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY()));
            s1.set(1,0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY()));
            s1.set(2,0.25 * (1 + pc1L.getX()) * (1 + pc1L.getY()));
            s1.set(3,0.25 * (1 - pc1L.getX()) * (1 + pc1L.getY()));


            Vector s2=Vector.fromArray(shapeArray2);
            s2.set(0,0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY()));
            s2.set(1,0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY()));
            s2.set(2,0.25 * (1 + pc2L.getX()) * (1 + pc2L.getY()));
            s2.set(3,0.25 * (1 - pc2L.getX()) * (1 + pc2L.getY()));


            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    sumPC_H_BCArray[i][j] += (s1.get(i)*s1.get(j)+(s2.get(i)*s2.get(j)))*(detJ1D) ;
                }
            }

        }
        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                sumPC_H_BCArray[i][j]*=alfa;
            }
        }

      return sumPC_H_BCArray;
    }
    public double calcL(int i1,int i2,Node[] tempNodes){
        return Math.sqrt(Math.pow(tempNodes[i1].getX()-tempNodes[i2].getX(),2)+Math.pow(tempNodes[i1].getY()-tempNodes[i2].getY(),2));
    }
    public double[][] calc_H_Hbc_Matrix(int k,Node[] tempNodes,int alfa) {
        double[][] H_BC_matrix= calcLocal_H_BC_matrix(alfa,tempNodes);
        double[][] H_matrix= calc_H_Matrix(k,tempNodes);

        for (int i = 0; i <size ; i++) {
            for (int j = 0; j <size ; j++) {
                H_matrix[i][j]+=H_BC_matrix[i][j];
            }
        }

        return H_matrix;
    }
    //-------------------------------Wektor P -----------------------------------------------------------
    public double[] calc_P_Vector(int alfa,  Node[] tempNodes, double tAmbient){

        double[] shapeVector1=new double[size];
        double[] shapeVector2=new double[size];
        double[] sumP_Vectors=new double[size];

        double l=0,detJ1D=0;
        for (int i = 0; i < size; i++) {
                sumP_Vectors[i]=0;
        }

        if (tempNodes[0].isBC() && tempNodes[1].isBC()) {

            l = calcL(0, 1,tempNodes);
            detJ1D = calcDetJ1D(l);
            Point2D.Double pc1B = new Point2D.Double((-1 / Math.sqrt(3)), -1.);
            Point2D.Double pc2B = new Point2D.Double((1 / Math.sqrt(3)), -1.);
            Vector p1=Vector.fromArray(shapeVector1);

            p1.set(0,0.25 * (1 - pc1B.getX()) * (1 - pc1B.getY()));
            p1.set(1,0.25 * (1 + pc1B.getX()) * (1 - pc1B.getY()));
             p1.set(2,0.25 * (1 + pc1B.getX()) * (1 + pc1B.getY())) ;
            p1.set(3,0.25 * (1 - pc1B.getX()) * (1 + pc1B.getY()));
            //   print2DArray(shapeArray1, size);

           Vector p2=Vector.fromArray(shapeVector2);
            p2.set(0,0.25 * (1 - pc2B.getX()) * (1 - pc2B.getY())) ;
            p2.set(1,0.25 * (1 + pc2B.getX()) * (1 - pc2B.getY())) ;
            p2.set(2,0.25 * (1 + pc2B.getX()) * (1 + pc2B.getY()));
             p2.set(3,0.25 * (1 - pc2B.getX()) * (1 + pc2B.getY())) ;
            //   print2DArray(shapeArray2, size);
            for (int i = 0; i < size; i++) {
                    sumP_Vectors[i] += (p1.get(i) + p2.get(i)) * detJ1D;
            }

        }
        if (tempNodes[1].isBC() && tempNodes[2].isBC()) {

            Point2D.Double pc1R = new Point2D.Double(1, -1 / Math.sqrt(3));
            Point2D.Double pc2R = new Point2D.Double(1, 1 / Math.sqrt(3));
            l = calcL(1, 2,tempNodes);
            detJ1D = calcDetJ1D(l);

            Vector p1=Vector.fromArray(shapeVector1);

            p1.set(0,0.25 * (1 - pc1R.getX()) * (1 - pc1R.getY()));
            p1.set(1,0.25 * (1 + pc1R.getX()) * (1 - pc1R.getY()));
            p1.set(2,0.25 * (1 + pc1R.getX()) * (1 + pc1R.getY())) ;
            p1.set(3,0.25 * (1 - pc1R.getX()) * (1 + pc1R.getY()));
            //   print2DArray(shapeArray1, size);

            Vector p2=Vector.fromArray(shapeVector2);
            p2.set(0,0.25 * (1 - pc2R.getX()) * (1 - pc2R.getY())) ;
            p2.set(1,0.25 * (1 + pc2R.getX()) * (1 - pc2R.getY())) ;
            p2.set(2,0.25 * (1 + pc2R.getX()) * (1 + pc2R.getY()));
            p2.set(3,0.25 * (1 - pc2R.getX()) * (1 + pc2R.getY())) ;
            // print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {
                sumP_Vectors[i] += (p1.get(i) + p2.get(i)) * detJ1D;
            }
        }
        if (tempNodes[2].isBC() && tempNodes[3].isBC()) {
            Point2D.Double pc1U = new Point2D.Double(-1 / Math.sqrt(3), 1);
            Point2D.Double pc2U = new Point2D.Double(1 / Math.sqrt(3), 1);
            l = calcL(2, 3,tempNodes);
            detJ1D = calcDetJ1D(l);

            Vector p1=Vector.fromArray(shapeVector1);

            p1.set(0,0.25 * (1 - pc1U.getX()) * (1 - pc1U.getY()));
            p1.set(1,0.25 * (1 + pc1U.getX()) * (1 - pc1U.getY()));
            p1.set(2,0.25 * (1 + pc1U.getX()) * (1 + pc1U.getY())) ;
            p1.set(3,0.25 * (1 - pc1U.getX()) * (1 + pc1U.getY()));
            //   print2DArray(shapeArray1, size);

            Vector p2=Vector.fromArray(shapeVector2);
            p2.set(0,0.25 * (1 - pc2U.getX()) * (1 - pc2U.getY())) ;
            p2.set(1,0.25 * (1 + pc2U.getX()) * (1 - pc2U.getY())) ;
            p2.set(2,0.25 * (1 + pc2U.getX()) * (1 + pc2U.getY()));
            p2.set(3,0.25 * (1 - pc2U.getX()) * (1 + pc2U.getY())) ;

            for (int i = 0; i < size; i++) {
                    sumP_Vectors[i] += (p1.get(i)+p2.get(i)) * detJ1D;
            }
        }
        if (tempNodes[3].isBC() && tempNodes[0].isBC()) {
            Point2D.Double pc1L = new Point2D.Double(-1, -1 / Math.sqrt(3));
            Point2D.Double pc2L = new Point2D.Double(-1, 1 / Math.sqrt(3));
            l = calcL(3, 0,tempNodes);
            detJ1D = calcDetJ1D(l);

            Vector p1=Vector.fromArray(shapeVector1);

            p1.set(0,0.25 * (1 - pc1L.getX()) * (1 - pc1L.getY()));
            p1.set(1,0.25 * (1 + pc1L.getX()) * (1 - pc1L.getY()));
            p1.set(2,0.25 * (1 + pc1L.getX()) * (1 + pc1L.getY())) ;
            p1.set(3,0.25 * (1 - pc1L.getX()) * (1 + pc1L.getY()));
            //   print2DArray(shapeArray1, size);

            Vector p2=Vector.fromArray(shapeVector2);
            p2.set(0,0.25 * (1 - pc2L.getX()) * (1 - pc2L.getY())) ;
            p2.set(1,0.25 * (1 + pc2L.getX()) * (1 - pc2L.getY())) ;
            p2.set(2,0.25 * (1 + pc2L.getX()) * (1 + pc2L.getY()));
            p2.set(3,0.25 * (1 - pc2L.getX()) * (1 + pc2L.getY())) ;
            //  print2DArray(shapeArray2, size);

            for (int i = 0; i < size; i++) {
                    sumP_Vectors[i] += (p1.get(i)+p2.get(i)) * detJ1D;
            }
        }
        for (int i = 0; i <size ; i++) {

            sumP_Vectors[i]=sumP_Vectors[i]*tAmbient*(-alfa);
        }
        return sumP_Vectors;
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

    public int getJacobiSize() {
        return jacobiSize;
    }

    public void setJacobiSize(int jacobiSize) {
        this.jacobiSize = jacobiSize;
    }

    public double[][] getShapeFunctions() {
        return shapeFunctions;
    }

    public void setShapeFunctions(double[][] shapeFunctions) {
        this.shapeFunctions = shapeFunctions;
    }

    public double[][] getJacobiArray() {
        return jacobiArray;
    }

    public void setJacobiArray(double[][] jacobiArray) {
        this.jacobiArray = jacobiArray;
    }

    public Node[] getTempNodes() {
        return tempNodes;
    }

    public void setTempNodes(Node[] tempNodes) {
        this.tempNodes = tempNodes;
    }

    public double[][] getdN_dXArray() {
        return dN_dXArray;
    }

    public void setdN_dXArray(double[][] dN_dXArray) {
        this.dN_dXArray = dN_dXArray;
    }

    public double[][] getdN_dYArray() {
        return dN_dYArray;
    }

    public void setdN_dYArray(double[][] dN_dYArray) {
        this.dN_dYArray = dN_dYArray;
    }

    public double[][] getH_Matrix() {
        return H_Matrix;
    }

    public void setH_Matrix(double[][] h_Matrix) {
        H_Matrix = h_Matrix;
    }

    public double[][] getC_Matrix() {
        return C_Matrix;
    }

    public void setC_Matrix(double[][] c_Matrix) {
        C_Matrix = c_Matrix;
    }
}
