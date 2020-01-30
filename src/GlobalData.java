import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class GlobalData {
    private double H;   //wysokosc/szerokosc
    private double W;

    private int nH;     //liczba wezlow pionowo/poziomo
    private int nW;

    private int nE;     //liczba elementow/wezlow w siatce
    private int nN;

    private double alfa;    //dane materialowe
    private double k;
    private double c;
    private double ro;

    private double tInitial;    //temperatura poczatkowa
    private double simTime;
    private double simStepTime;
    private double ambientTemp;

    public GlobalData() {
        readDataFromFile();
    }

    //wczytanie danych z pliku

    public void readDataFromFile() {

        try {
            File file = new File("F:\\Java\\Projects\\Sreniawska_Gabriela_MES\\data.txt");
            BufferedReader br = new BufferedReader(new FileReader((file)));
            String[] tempArr=new String[2];
            HashMap<String,String> mapOfData=new HashMap<>();

            String line;
            System.out.println("Reading data from file ...");
            while (((line = br.readLine()) != null)) {
                tempArr=line.split("=");
                mapOfData.put(tempArr[0],tempArr[1]);
            }
            setParams(mapOfData);
            printData(mapOfData);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //ustawienie danych z test case do mapy
    public  void setParams(HashMap<String,String> mapOfData) {
        setH(Double.parseDouble(mapOfData.get("H")));
        setW(Double.parseDouble(mapOfData.get("W")));
        setnH(Integer.parseInt(mapOfData.get("nH")));
        setnW(Integer.parseInt(mapOfData.get("nW")));
        setnE((nH-1)*(nW-1));
        setnN(nH*nW);
        setAlfa(Double.parseDouble(mapOfData.get("alfa")));
        setK(Double.parseDouble(mapOfData.get("k")));
        setC(Double.parseDouble(mapOfData.get("c")));
        setRo(Double.parseDouble(mapOfData.get("ro")));
        setTInitial(Double.parseDouble(mapOfData.get("tInitial")));
        setAmbientTemp(Double.parseDouble(mapOfData.get("ambientTemp")));
        setSimTime(Double.parseDouble(mapOfData.get("simTime")));
        setSimStepTime(Double.parseDouble(mapOfData.get("simStepTime")));
    }
    //wyswietlanie danych
    public  void printData(HashMap<String,String> mapOfData){
        mapOfData.forEach((k,v)-> System.out.println("Key : "+k+"    Value : "+v));
    }
    public double getH() {
        return H;
    }

    public void setH(double h) {
        H = h;
    }

    public double getW() {
        return W;
    }

    public void setW(double w) {
        W = w;
    }

    public int getnH() {
        return nH;
    }

    public void setnH(int nH) {
        this.nH = nH;
    }

    public int getnW() {
        return nW;
    }

    public void setnW(int nW) {
        this.nW = nW;
    }

    public int getnE() {
        return nE;
    }

    public void setnE(int nE) {
        this.nE = nE;
    }

    public int getnN() {
        return nN;
    }

    public void setnN(int nN) {
        this.nN = nN;
    }

    public double getAlfa() {
        return alfa;
    }

    public void setAlfa(double alfa) {
        this.alfa = alfa;
    }

    public double getK() {
        return k;
    }

    public void setK(double k) {
        this.k = k;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public double getRo() {
        return ro;
    }

    public void setRo(double ro) {
        this.ro = ro;
    }

    public double getTInitial() {
        return tInitial;
    }

    public void setTInitial(double tInitial) {
        this.tInitial = tInitial;
    }

    public double gettInitial() {
        return tInitial;
    }

    public void settInitial(double tInitial) {
        this.tInitial = tInitial;
    }

    public double getSimTime() {
        return simTime;
    }

    public void setSimTime(double simTime) {
        this.simTime = simTime;
    }

    public double getSimStepTime() {
        return simStepTime;
    }

    public void setSimStepTime(double simStepTime) {
        this.simStepTime = simStepTime;
    }

    public double getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(double ambientTemp) {
        this.ambientTemp = ambientTemp;
    }

    @Override
    public String toString() {
        return "GlobalData{" +
                "H=" + H +
                ", W=" + W +
                ", nH=" + nH +
                ", nW=" + nW +
                ", nE=" + nE +
                ", nN=" + nN +
                ", alfa=" + alfa +
                ", k=" + k +
                ", c=" + c +
                ", ro=" + ro +
                ", tInitial=" + tInitial +
                '}';
    }
}
