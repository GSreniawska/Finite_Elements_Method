import java.util.Arrays;

public class Element {
    private int size=4;
    private int[] idOfNodes;
    private double[][] local_H_Matrix;
    private double[][] local_C_Matrix;
    private double[] local_P_Vector;

    public Element(int[] idOfNodes) {

        this.idOfNodes = idOfNodes;
        this.local_C_Matrix=new double[size][size];
        this.local_H_Matrix=new double[size][size];
        this.local_P_Vector=new double[size];
    }

    public Element() {
    }

    public int[] getIdOfNodes() {
        return idOfNodes;
    }

    public void setIdOfNodes(int[] idOfNodes) {
        this.idOfNodes = idOfNodes;
    }

    @Override
    public String toString() {
        return "Element{" +
                "idOfNodes=" + Arrays.toString(idOfNodes) +
                '}';
    }

    public double[][] getLocal_H_Matrix() {
        return local_H_Matrix;
    }

    public void setLocal_H_Matrix(double[][] local_H_Matrix) {
        this.local_H_Matrix = local_H_Matrix;
    }

    public double[][] getLocal_C_Matrix() {
        return local_C_Matrix;
    }

    public void setLocal_C_Matrix(double[][] local_C_Matrix) {
        this.local_C_Matrix = local_C_Matrix;
    }

    public double[] getLocal_P_Vector() {
        return local_P_Vector;
    }

    public void setLocal_P_Vector(double[] local_P_Vector) {
        this.local_P_Vector = local_P_Vector;
    }
}
