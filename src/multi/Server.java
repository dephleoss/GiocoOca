package multi;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    ServerSocket server;
    public Socket client;
    DataInputStream inClient;
    DataOutputStream outClient;
    private int posizione;
    private int dado;
    public Server(Socket s){
        this.client = s;
    }
    @Override
    public void run() {
        try{
            comunica();
        }
        catch (Exception e){
            e.printStackTrace(System.out);
        }
    }
    public void comunica(){
        try {
            inClient = new DataInputStream(client.getInputStream());
            outClient = new DataOutputStream(client.getOutputStream());
            while (true){
                dado = inClient.readInt();
                if (dado!=0) {
                    System.out.println("Numero ricevuto:"+ dado);
                    calcolaPosizione();
                    inviaMessage("La tua posizione è: "+posizione);
                    /*if (stringaricevuta.equals("FINE")) {
                        System.out.println("Stringa ricevuta :" + stringaricevuta);
                        String stringam = stringaricevuta.toUpperCase();
                        outClient.writeBytes("Server in Chisura -->" + stringam + '\n');
                        System.out.println("Server In Chiusura...");
                        break;
                    } else {
                        System.out.println("Stringa ricevuta :" + stringaricevuta);
                        String stringam = stringaricevuta.toUpperCase();
                        System.out.println("invio stringa modificata");
                        outClient.writeBytes(stringam + '\n');
                    }*/
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                inClient.close();
                outClient.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    public void inviaMessage(String mex){
        try {
            outClient = new DataOutputStream(client.getOutputStream());
            outClient.writeBytes(mex+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String receiveMessage(){
        String mex=null;
        try {
            inClient = new DataInputStream(client.getInputStream());
            mex = inClient.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mex;
    }
    public void controllaPosizione(){
        switch(posizione){
            case 5:
            case 9:
            case 18:
            case 27:
            case 36:
            case 45:
            case 54:
                inviaMessage("OCA! Ripeti il movimento");
                posizione+=dado;
                controllaPosizione();
                break;
            case 6:
                inviaMessage("PONTE! Ripeti il movimento");
                posizione+=dado;
                controllaPosizione();
                break;
            case 19:
                inviaMessage("LOCANDA! Stai fermo per 3 turni");
                break;
            case 31:
                inviaMessage("POZZO! rimani bloccato finchè non arriva un’altra pedina che prenderà il tuo posto");
                break;
            case 52:
                inviaMessage("PRIGIONE! rimani bloccato finchè non arriva un’altra pedina che prenderà il tuo posto");
                break;
            case 42:
                inviaMessage("LABIRINTO! Ritorna alla casella 33");
                posizione=33;
                break;
            case 58:
                inviaMessage("SCHELETRO! Ritorna alla casella 1");
                posizione=1;
                break;
            case 63:
                System.out.println("HAI VINTO!");
                break;
        }
    }
    public void calcolaPosizione(){
        posizione= posizione+dado;
        if(posizione>63){
            posizione= posizione-dado;
            System.out.println("HAI SUPERATO LA CASELLA 63! Torni alla casella precedente");
        }else{
            controllaPosizione();
        }
    }

}
