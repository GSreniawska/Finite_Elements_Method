import java.util.ArrayList;
import java.util.HashMap;

public class Grid {
    private Node[] nodes;
    private Element[] elements;
    private int size=4;
    private double[] P_Global_Vector;
    private double[][] H_Global_Matrix;
    private double[][] C_Global_Matrix;

    public Grid(GlobalData globalData) {
        this.nodes=new Node[globalData.getnN()];
        this.elements=new Element[globalData.getnE()];
        elements=calcElements(globalData);
        calcNodes(globalData);
        printElements(globalData);
        printNodes(globalData);
    }
    public Element[] calcElements(GlobalData globalData){
        int[] arrOfNodesIndexes;
        int counter=0;
        Element tempElement=new Element();

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
    public void calcNodes(GlobalData globalData){
        double x=0;
        double y=0;
        double deltaX=globalData.getW()/(globalData.getnW()-1);
        double deltaY=globalData.getH()/(globalData.getnH()-1);
        Node tempNode;
        int k=0;

            for (int i = 0; i < globalData.getnW(); i++) {
                for (int j = 0; j < globalData.getnH(); j++) {
                    tempNode = new Node(k, i * deltaX, j * deltaY, globalData.getTInitial());

                    if (tempNode.getX() == 0 || tempNode.getY() == 0 || tempNode.getX() == globalData.getW() || tempNode.getY() == globalData.getnH()) {
                        tempNode.setBC(true);
                    } else {
                        tempNode.setBC(false);
                    }

                    nodes[k]=tempNode;
                    k++;


                }
            }


    }
    public void setLocalValuesForElements(GlobalData globalData,UniversalElement universalElement,int alfa,int c,int ro,int k){
        Node[] tempNodes=new Node[size];
        for (int i = 0; i <globalData.getnE() ; i++) {
            for(int j=0;j<4;j++){
                tempNodes[j] = nodes[elements[i].getIdOfNodes()[j]];
            }
            elements[i].setLocal_H_Matrix(universalElement.calc_H_Matrix(k,tempNodes));
            elements[i].setLocal_C_Matrix(universalElement.calc_C_Matrix(c,ro,tempNodes));
            elements[i].setLocal_P_Vector(universalElement.calc_P_Vector(alfa,i,tempNodes));

        }
    }
    public void aggregation(GlobalData globalData,Grid grid,UniversalElement universalElement){
        System.out.println(globalData.getnE());
        System.out.println(globalData.getnE());
        P_Global_Vector=new double[globalData.getnN()];
        H_Global_Matrix=new double[globalData.getnN()][globalData.getnN()];
        C_Global_Matrix=new double[globalData.getnN()][globalData.getnN()];
        for (int i = 0; i < globalData.getnE(); i++) {


            int[] id=new int[size];
            for (int j = 0; j <size ; j++) {
                id[j]=grid.getElements()[i].getIdOfNodes()[j];
            }
            for (int j = 0; j <size ; j++) {
                P_Global_Vector[id[j]] += grid.getElements()[i].getLocal_P_Vector()[j];

                for (int k = 0; k <size ; k++) {
                    H_Global_Matrix[id[k]][id[j]]+=grid.getElements()[i].getLocal_H_Matrix()[k][j];
                    C_Global_Matrix[id[k]][id[j]]+=grid.getElements()[i].getLocal_C_Matrix()[k][j];
                }
            }
        }



        System.out.println("H Matrix:");
        universalElement.print2DArray(H_Global_Matrix,globalData.getnN());
        System.out.println("C Matrix:");
        universalElement.print2DArray(C_Global_Matrix,globalData.getnN());
        System.out.println("P Vector:");
        for (int i = 0; i <globalData.getnN() ; i++) {
            System.out.println(P_Global_Vector[i]+"     ");
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
}
