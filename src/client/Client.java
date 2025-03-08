package client;

import java.io.IOException;
import java.net.Socket;

public class Client {
        public static void main(String[] args){
            Socket clientSocket ;

            try{
                clientSocket = new Socket("172.30.1.16",80);

                ReadThread readThread = new ReadThread(clientSocket);
                WriteThread writeThread = new WriteThread(clientSocket);

                readThread.start();
                writeThread.start();




            }catch (IOException e){
                throw new RuntimeException(e);
            }


        }

}
