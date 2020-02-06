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

    private double kGypsumPlaster;
    private double cGypsumPlaster;
    private double roGypsumPlaster;

    private double kSolidBrick;
    private double cSolidBrick;
    private double roSolidBrick;

    private double kStyrofoam;
    private double cStyrofoam;
    private double roStyrofoam;

    private double kPlaster;
    private double cPlaster;
    private double roPlaster;

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

        setkSolidBrick(Double.parseDouble(mapOfData.get("kSolidBrick")));
        setcSolidBrick(Double.parseDouble(mapOfData.get("cSolidBrick")));
        setRoSolidBrick(Double.parseDouble(mapOfData.get("roSolidBrick")));

        setkPlaster(Double.parseDouble(mapOfData.get("kPlaster")));
        setcPlaster(Double.parseDouble(mapOfData.get("cPlaster")));
        setRoPlaster(Double.parseDouble(mapOfData.get("roPlaster")));

        setkGypsumPlaster(Double.parseDouble(mapOfData.get("kGypsumPlaster")));
        setcGypsumPlaster(Double.parseDouble(mapOfData.get("cGypsumPlaster")));
        setRoGypsumPlaster(Double.parseDouble(mapOfData.get("roGypsumPlaster")));

        setkStyrofoam(Double.parseDouble(mapOfData.get("kStyrofoam")));
        setcStyrofoam(Double.parseDouble(mapOfData.get("cStyrofoam")));
        setRoStyrofoam(Double.parseDouble(mapOfData.get("roStyrofoam")));

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



    public double getkGypsumPlaster() {
        return kGypsumPlaster;
    }

    public void setkGypsumPlaster(double kGypsumPlaster) {
        this.kGypsumPlaster = kGypsumPlaster;
    }

    public double getcGypsumPlaster() {
        return cGypsumPlaster;
    }

    public void setcGypsumPlaster(double cGypsumPlaster) {
        this.cGypsumPlaster = cGypsumPlaster;
    }

    public double getRoGypsumPlaster() {
        return roGypsumPlaster;
    }

    public void setRoGypsumPlaster(double roGypsumPlaster) {
        this.roGypsumPlaster = roGypsumPlaster;
    }

    public double getkSolidBrick() {
        return kSolidBrick;
    }

    public void setkSolidBrick(double kSolidBrick) {
        this.kSolidBrick = kSolidBrick;
    }

    public double getcSolidBrick() {
        return cSolidBrick;
    }

    public void setcSolidBrick(double cSolidBrick) {
        this.cSolidBrick = cSolidBrick;
    }

    public double getRoSolidBrick() {
        return roSolidBrick;
    }

    public void setRoSolidBrick(double roSolidBrick) {
        this.roSolidBrick = roSolidBrick;
    }

    public double getkPlaster() {
        return kPlaster;
    }

    public void setkPlaster(double kPlaster) {
        this.kPlaster = kPlaster;
    }

    public double getcPlaster() {
        return cPlaster;
    }

    public void setcPlaster(double cPlaster) {
        this.cPlaster = cPlaster;
    }

    public double getRoPlaster() {
        return roPlaster;
    }

    public void setRoPlaster(double roPlaster) {
        this.roPlaster = roPlaster;
    }



    public double getkStyrofoam() {
        return kStyrofoam;
    }

    public void setkStyrofoam(double kStyrofoam) {
        this.kStyrofoam = kStyrofoam;
    }

    public double getcStyrofoam() {
        return cStyrofoam;
    }

    public void setcStyrofoam(double cStyrofoam) {
        this.cStyrofoam = cStyrofoam;
    }

    public double getRoStyrofoam() {
        return roStyrofoam;
    }

    public void setRoStyrofoam(double roStyrofoam) {
        this.roStyrofoam = roStyrofoam;
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
                ", kGypsumPlaster=" + kGypsumPlaster +
                ", cGypsumPlaster=" + cGypsumPlaster +
                ", roGypsumPlaster=" + roGypsumPlaster +
                ", kSolidBrick=" + kSolidBrick +
                ", cSolidBrick=" + cSolidBrick +
                ", roSolidBrick=" + roSolidBrick +
                ", kStyrofoam=" + kStyrofoam +
                ", cStyrofoam=" + cStyrofoam +
                ", roStyrofoam=" + roStyrofoam +
                ", kPlaster=" + kPlaster +
                ", cPlaster=" + cPlaster +
                ", roPlaster=" + roPlaster +
                ", tInitial=" + tInitial +
                ", simTime=" + simTime +
                ", simStepTime=" + simStepTime +
                ", ambientTemp=" + ambientTemp +
                '}';
    }
}
