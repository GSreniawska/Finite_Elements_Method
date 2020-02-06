import org.la4j.Vector;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Grid {
    DecimalFormat formatter = new DecimalFormat("#0.000");
    private Node[] nodes;
    private Element[] elements;
    private int size=4;
    private double[] P_Global_Vector;
    private double[][] H_Global_Matrix;
    private double[][] C_Global_Matrix;
    private int finalsize=16;

    public Grid(GlobalData globalData,UniversalElement universalElement) {
        this.nodes=new Node[globalData.getnN()];
        this.elements=new Element[globalData.getnE()];
        elements=calcElements(globalData);
        nodes=calcNodes(globalData);
        P_Global_Vector=new double[globalData.getnN()];
        H_Global_Matrix=new double[globalData.getnN()][globalData.getnN()];
        C_Global_Matrix=new double[globalData.getnN()][globalData.getnN()];

        printElements(globalData);
        printNodes(globalData);
        setLocalValuesForElements(globalData,universalElement,(int)globalData.getAlfa(),(int)globalData.getcMetalSheet(),(int)globalData.getRoMetalSheet(),(int)globalData.getkMetalSheet(),globalData.getAmbientTemp());
        aggregation(globalData,universalElement);
    }
    public Element[] calcElements(GlobalData globalData){
        int[] arrOfNodesIndexes;
        int counter=0;
        Element tempElement;

        for (int i = 0; i < globalData.getnE(); i++) //dla każdego elementu:
        {
            if((i%(globalData.getnH()-1)==0)&& (i!=0)){
                counter++;
            }
            arrOfNodesIndexes=new int[4];
            arrOfNodesIndexes[0] = i + counter;
            arrOfNodesIndexes[1] = i + globalData.getnH() + counter;
            arrOfNodesIndexes[2] = i + globalData.getnH() + 1 + counter;
            arrOfNodesIndexes[3] = i + 1 + counter;
            tempElement=new Element(arrOfNodesIndexes);
            elements[i] = tempElement;
        }
        return elements;
    }
    public void printElements(GlobalData globalData) {
      for (int i=0;i<globalData.getnE();i++)
      {
          System.out.println(elements[i].toString());
      }
    }
    public Node[] calcNodes(GlobalData globalData){
        double x=0;
        double y=0;
        double deltaX=globalData.getW()/(globalData.getnW()-1);
        double deltaY=globalData.getH()/(globalData.getnH()-1);
        System.out.println(deltaX);
        System.out.println(deltaY);
        Node tempNode;
        int k=0;

            for (int i = 0; i < globalData.getnW(); i++) {
                for (int j = 0; j < globalData.getnH(); j++) {
                    tempNode = new Node(k, (double)Math.round(i*deltaX * 100000d) / 100000d
                            ,(double)Math.round(j*deltaY * 100000d) / 100000d
                            , globalData.getTInitial());

                    if (tempNode.getX() == 0 || tempNode.getY() == 0 || tempNode.getX() == globalData.getW() || tempNode.getY() == globalData.getH()) {
                        tempNode.setBC(true);
                    } else {
                        tempNode.setBC(false);
                    }
                    nodes[k]=tempNode;
                    k++;
                }
            }
            return  nodes;
    }
    public void setLocalValuesForElements(GlobalData globalData,UniversalElement universalElement,int alfa,int c,int ro,int k,double tAmbient){
        double deltaX=globalData.getW()/(globalData.getnW()-1);
        /*
        *
        *   szerokosc siatki = 425mm
        *   ilosc elementow na szerokosc = 85   (425mm/5mm)
        *   szerokosc 1 warstwy - blachy - rowna jest 5mm wiec zajmuje 1 element na szerokosc
        *   szerokosc 2 warstwy - ziemi okrzemkowej - rowna jest 40mm wiec zajmuje 8 elementow na szerokosc
        *   szerokosc 3 warstwy - kamieni szamotowych - rowna jest 380mm wiec zajmuje 76 elementow na szerokosc
        *   deltaX - szerokosć jednego elementu
        *
        */

        for (int i = 0; i < globalData.getnE(); i++) {
            Node[] tempNodes = new Node[size];
            for (int j = 0; j < size; j++) {
                tempNodes[j] = nodes[elements[i].getIdOfNodes()[j]];
                if (tempNodes[j].getX() <=deltaX) { //1 element na szerokosc

                    k = (int) globalData.getkMetalSheet();
                    c = (int) globalData.getcMetalSheet();
                    ro = (int) globalData.getRoMetalSheet();

                } else if (tempNodes[j].getX() <= 9*deltaX && tempNodes[j].getX() >= deltaX) { //od 1 do 9 elementu na szerokosc

                    k = (int) globalData.getkDiatEarth();
                    c = (int) globalData.getcDiatEarth();
                    ro = (int) globalData.getRoDiatEarth();

                } else if (tempNodes[j].getX() >= 9*deltaX) {        //od 9 elementu na szerokosc

                    k = (int) globalData.getkFireclay();
                    c = (int) globalData.getcFireclay();
                    ro = (int) globalData.getRoFireClay();
                }
            }

            elements[i].setLocal_H_Matrix(universalElement.calc_H_Hbc_Matrix(k,tempNodes,alfa));
            elements[i].setLocal_C_Matrix(universalElement.calc_C_Matrix(c,ro,tempNodes));
            elements[i].setLocal_P_Vector(universalElement.calc_P_Vector(alfa,tempNodes,tAmbient));

        }
    }
    public void aggregation(GlobalData globalData,UniversalElement universalElement){

        for (int i = 0; i < globalData.getnE(); i++) {
            int[] id = new int[size];
            for (int j = 0; j < size; j++) {
                id[j] = elements[i].getIdOfNodes()[j];
            }
            for (int j = 0; j < size; j++) {
                P_Global_Vector[id[j]] += elements[i].getLocal_P_Vector()[j];
            }
            for (int j = 0; j <size ; j++) {

                for (int k = 0; k < size; k++) {
                    H_Global_Matrix[id[j]][id[k]] += elements[i].getLocal_H_Matrix()[j][k];
                    C_Global_Matrix[id[j]][id[k]] += elements[i].getLocal_C_Matrix()[j][k];
                }
            }
        }


    }
    public void printNodes(GlobalData globalData){
        for (int i = 0; i <globalData.getnN() ; i++) {
            System.out.println(nodes[i]);
        }
    }

    public Node[] getNodes() {
        return nodes;
    }

    public void setNodes(Node[] nodes) {
        this.nodes = nodes;
    }

    public Element[] getElements() {
        return elements;
    }

    public void setElements(Element[] elements) {
        this.elements = elements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double[] getP_Global_Vector() {
        return P_Global_Vector;
    }

    public void setP_Global_Vector(double[] p_Global_Vector) {
        P_Global_Vector = p_Global_Vector;
    }

    public double[][] getH_Global_Matrix() {
        return H_Global_Matrix;
    }

    public void setH_Global_Matrix(double[][] h_Global_Matrix) {
        H_Global_Matrix = h_Global_Matrix;
    }

    public double[][] getC_Global_Matrix() {
        return C_Global_Matrix;
    }

    public void setC_Global_Matrix(double[][] c_Global_Matrix) {
        C_Global_Matrix = c_Global_Matrix;
    }
}
