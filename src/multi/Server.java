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
    private  int aspetta=0;
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
        if(posizione<=63) {
            switch (posizione) {
                case 5:
                case 9:
                case 18:
                case 27:
                case 36:
                case 45:
                case 54:
                    posizione += dado;
                    inviaMessage("OCA! Ripeti il movimento la tua posizione è: "+posizione);
                    controllaPosizione();
                    break;
                case 6:
                    inviaMessage("PONTE! Ripeti il movimento");
                    posizione += dado;
                    controllaPosizione();
                    break;
                case 19:
                    inviaMessage("LOCANDA! Stai fermo per 3 turni");
                    aspetta = 3;
                    break;
                case 31:
                    inviaMessage("POZZO! rimani bloccato finchè non arriva un’altra pedina che prenderà il tuo posto");
                    break;
                case 52:
                    inviaMessage("PRIGIONE! rimani bloccato finchè non arriva un’altra pedina che prenderà il tuo posto");
                    break;
                case 42:
                    inviaMessage("LABIRINTO! Ritorna alla casella 33");
                    posizione = 33;
                    break;
                case 58:
                    inviaMessage("SCHELETRO! Ritorna alla casella 1");
                    posizione = 1;
                    break;
                case 63:
                    inviaMessage("HAI VINTO!");
                    break;
                default:
                    inviaMessage("La tua posizione è: "+posizione);

            }
        }else{
            System.out.println(posizione);
            int passiIndietro= posizione - 63;
            posizione=posizione-passiIndietro;
            inviaMessage("HAI SUPERATO LA CASELLA 63! Torni alla casella: "+posizione);
        }
    }
    public void calcolaPosizione(){
       if(aspetta==0){
            posizione= posizione+dado;
            controllaPosizione();
        }else{
            aspetta=aspetta-1;
            inviaMessage("Devi Aspettare ancora "+aspetta+" turni");
        }
    }

}
