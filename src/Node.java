

public class Node {
    private int id;
    private double x;
    private double y;
    private double t; // temperatura
    private boolean BC; // warunek brzegowy

    public Node(int id, double x, double y, double t) {
        this.id=id;
        this.x = x;
        this.y = y;
        this.t = t;
        this.BC = false;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public boolean isBC() {
        return BC;
    }

    public void setBC(boolean BC) {
        this.BC = BC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", t=" + t +
                ", BC=" + BC +
                '}';
    }
}
