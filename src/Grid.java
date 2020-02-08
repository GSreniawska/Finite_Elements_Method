import java.text.DecimalFormat;

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
        setLocalValuesForElements(globalData,universalElement,1,1,1,1,globalData.getAmbientTemp());
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

                    if (tempNode.getX() == 0) {
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
        /*
         *
         *
         *   szerokosc siatki = 370mm
         *   ilosc elementow na szerokosc = 74   (370mm/5mm)
         *   szerokosc 1 warstwy - tynku - rowna jest 5mm wiec zajmuje jeden element na szerokosc
         *   szerokosc 2 warstwy - styropian - rowna jest 100mm wiec zajmuje 20 elementow na szerokosc
         *   szerokosc 3 warstwy - cegły - rowna jest 250mm wiec zajmuje 50 elementow na szerokosc
         *   szerokosc 4 warstwy - tynk gipsowy - rowna jest 15mm wiec zajmuje 3 elementy na szerokosc
         *
         *
         *   Dane Materiałowe Źródło: http://www.certyfikat-energetyczny.powiat.pl/CE_P/wspolczynniki_przewodzenia.html
         *
         */
        int eH=globalData.getnH()-1;

        for (int i = 0; i < globalData.getnE(); i++) {
            Node[] tempNodes = new Node[size];
            for (int j = 0; j < size; j++) {
                tempNodes[j] = nodes[elements[i].getIdOfNodes()[j]];

            }
            if (i <= 1 * eH) { //1 element na szerokosc
                System.out.println("1 warstwa");
                k = (int) globalData.getkPlaster();
                c = (int) globalData.getcPlaster();
                ro = (int) globalData.getRoPlaster();

            } else if (i >= (1 * eH + 1) && i < (21 * eH)) { //od 2 do 21 wlacznie elementu na szerokosc

                System.out.println("2 warstwa");
                k = (int) globalData.getkStyrofoam();
                c = (int) globalData.getcStyrofoam();
                ro = (int) globalData.getRoStyrofoam();

            } else if (i >= (21 * eH + 1) && i < (71 * eH)) { //22 do 71 wlacznie elementu na szerokosc
                System.out.println("3 warstwa");
                k = (int) globalData.getkSolidBrick();
                c = (int) globalData.getcSolidBrick();
                ro = (int) globalData.getRoSolidBrick();

            } else if (i >= (71 * eH + 1) && i < (74 * eH)) {        // od 72 do 74 wlacznie elementu na szerokosc
                System.out.println("4 warstwa");
                k = (int) globalData.getkGypsumPlaster();
                c = (int) globalData.getcGypsumPlaster();
                ro = (int) globalData.getRoGypsumPlaster();
            }
            System.out.println("Element " + (i + 1) + "\n\n");

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
