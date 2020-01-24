import java.util.ArrayList;
import java.util.HashMap;

public class Grid {
    private Node[] nodes;
    private Element[] elements;

    public Grid(GlobalData globalData) {
        nodes=new Node[globalData.getnN()];
        elements=new Element[globalData.getnE()];
    }
    public void calcElements(GlobalData globalData){
        int[] arrOfNodesIndexes=new int[4];

        for(int i=0;i<globalData.getnE();i++) //dla każdego elementu:
        {
            if((i!=0 && i%(globalData.getnH()-1)==0))   //wypelnienie tablicy indeksami nodow
            {
                arrOfNodesIndexes[0]=i;
                arrOfNodesIndexes[1]=i+globalData.getnH();
                arrOfNodesIndexes[2]=i+globalData.getnH()+1;
                arrOfNodesIndexes[3]=i+1;
                Element tempElement=new Element(arrOfNodesIndexes);
                this.elements[i]=tempElement;
            }
        }
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

        for(int i=0;i<globalData.getnN();i++) //przejscie po nodach
        {
            Node tempNode=new Node(i,x,y,globalData.getTInitial());
            if(tempNode.getX()==0||tempNode.getY()==0||tempNode.getX()==globalData.getW()||tempNode.getY()==globalData.getH()){
                tempNode.setBC(false);
            }else{
                tempNode.setBC(true);
            }
            nodes[i]=tempNode;
            x+=deltaX;
            y+=deltaY;
        }
    }
    public void printNodes(GlobalData globalData){
        for (int i = 0; i <globalData.getnN() ; i++) {
            System.out.println(nodes[i]);
        }
    }
}
