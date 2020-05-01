package multi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Random;

public class Cliente {
    private Socket s;
    private BufferedReader tastiera;
    private String stringaOut;
    private String stringaServer;
    private String stringaletta="";
    private DataOutputStream out;
    private BufferedReader in;
    public Socket connetti(){
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            s = new Socket("localhost",2000);
            out = new DataOutputStream(s.getOutputStream());
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }
    public void comunica(){
        String stringaletta = null;
        while (true) {
            try {
                String prova = in.readLine();
                System.out.println(prova);
                System.out.println("Cosa vuoi fare?");
                stringaletta = tastiera.readLine();
                switch (stringaletta){
                    case "invia":{
                        inviaTiro();
                        break;
                    }
                    case "ESCI":{
                        out.writeBytes("Client in chiusura.....");
                        chiudi();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void inviaTiro(){
        Random rnd = new Random();
        int dado1 = rnd.nextInt(6)+1;
        int dado2 = rnd.nextInt(6)+1;
        System.out.println("Il primo dado ha fatto:"+dado1+"! \n Il secondo dado ha fatto:"+dado2);
        int tot = (int)dado1 + (int)dado2;
        try {
            out.writeInt(tot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void chiudi(){
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
