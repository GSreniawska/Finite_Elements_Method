import java.util.Arrays;

public class Element {
    private int[] idOfNodes;

    public Element(int[] idOfNodes) {
        this.idOfNodes = idOfNodes;
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
}
