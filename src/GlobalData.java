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

    private double kMetalSheet;
    private double cMetalSheet;
    private double roMetalSheet;

    private double kDiatEarth;
    private double cDiatEarth;
    private double roDiatEarth;

    private double kFireclay;
    private double cFireclay;
    private double roFireClay;

    private double tInitial;    //temperatura poczatkowa
    private double simTime;
    private double simStepTime;
    private double ambientTemp;

    public GlobalData(String simulation) {
        readDataFromFile( simulation);
    }

    //wczytanie danych z pliku

    public void readDataFromFile(String simulation) {

        try {
            File file = new File("F:\\Java\\Projects\\Sreniawska_Gabriela_MES\\"+simulation);
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

        setkDiatEarth(Double.parseDouble(mapOfData.get("kDiatEarth")));
        setcDiatEarth(Double.parseDouble(mapOfData.get("cDiatEarth")));
        setRoDiatEarth(Double.parseDouble(mapOfData.get("roDiatEarth")));

        setkFireclay(Double.parseDouble(mapOfData.get("kFireclay")));
        setcFireclay(Double.parseDouble(mapOfData.get("cFireclay")));
        setRoFireClay(Double.parseDouble(mapOfData.get("roFireclay")));

        setkMetalSheet(Double.parseDouble(mapOfData.get("kMetalSheet")));
        setcMetalSheet(Double.parseDouble(mapOfData.get("cMetalSheet")));
        setRoMetalSheet(Double.parseDouble(mapOfData.get("roMetalSheet")));

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



    public double getkMetalSheet() {
        return kMetalSheet;
    }

    public void setkMetalSheet(double kMetalSheet) {
        this.kMetalSheet = kMetalSheet;
    }

    public double getcMetalSheet() {
        return cMetalSheet;
    }

    public void setcMetalSheet(double cMetalSheet) {
        this.cMetalSheet = cMetalSheet;
    }

    public double getRoMetalSheet() {
        return roMetalSheet;
    }

    public void setRoMetalSheet(double roMetalSheet) {
        this.roMetalSheet = roMetalSheet;
    }

    public double getkDiatEarth() {
        return kDiatEarth;
    }

    public void setkDiatEarth(double kDiatEarth) {
        this.kDiatEarth = kDiatEarth;
    }

    public double getcDiatEarth() {
        return cDiatEarth;
    }

    public void setcDiatEarth(double cDiatEarth) {
        this.cDiatEarth = cDiatEarth;
    }

    public double getRoDiatEarth() {
        return roDiatEarth;
    }

    public void setRoDiatEarth(double roDiatEarth) {
        this.roDiatEarth = roDiatEarth;
    }

    public double getkFireclay() {
        return kFireclay;
    }

    public void setkFireclay(double kFireclay) {
        this.kFireclay = kFireclay;
    }

    public double getcFireclay() {
        return cFireclay;
    }

    public void setcFireclay(double cFireclay) {
        this.cFireclay = cFireclay;
    }

    public double getRoFireClay() {
        return roFireClay;
    }

    public void setRoFireClay(double roFireClay) {
        this.roFireClay = roFireClay;
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
                ", kMetalSheet=" + kMetalSheet +
                ", cMetalSheet=" + cMetalSheet +
                ", roMetalSheet=" + roMetalSheet +
                ", kDiatEart=" + kDiatEarth +
                ", cDiatEarth=" + cDiatEarth +
                ", roDiatEarth=" + roDiatEarth +
                ", kFireclay=" + kFireclay +
                ", cFireclay=" + cFireclay +
                ", roFireClay=" + roFireClay +
                ", tInitial=" + tInitial +
                ", simTime=" + simTime +
                ", simStepTime=" + simStepTime +
                ", ambientTemp=" + ambientTemp +
                '}';
    }
}
