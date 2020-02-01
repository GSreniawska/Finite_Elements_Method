import java.awt.geom.Point2D;

public class FEM implements Runnable {
    private int size;



    public FEM() {
        this.size=4;

    }

    @Override
    public void run() {
        GlobalData globalData=new GlobalData();
        System.out.println(globalData.toString());
        Grid grid=new Grid(globalData); //tworzymy siatke
        UniversalElement universalElement=new UniversalElement(grid); //tworzymy element
        grid.setLocalValuesForElements(globalData,universalElement,(int)globalData.getAlfa(),(int)globalData.getC(),(int)globalData.getRo(),(int)globalData.getK());
        grid.aggregation(globalData,grid,universalElement);
    }

}
