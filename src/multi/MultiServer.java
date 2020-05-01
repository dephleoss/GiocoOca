package multi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MultiServer {
    private ArrayList<Server> prova = new ArrayList<Server>();
    public void start(){
        try{
            ServerSocket ss = new ServerSocket(2000);
            while (true){
                System.out.println("Server in attesa...");
                Socket socket = ss.accept();
                System.out.println("Server Socket:" + socket);
                Server sThread = new Server(socket);
                prova.add(sThread);
                sThread.start();
                sendMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void sendMessage(){
        for (Server element :prova
             )
        {
            element.inviaMessage("prova");
        }
    }
}
