package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadThread extends Thread {

    Socket socket ;
    InputStream inputStream;
    InputStreamReader reader ;
    BufferedReader br ;

    public ReadThread(Socket socket) throws IOException{
        this.socket = socket ;
        this.inputStream = this.socket.getInputStream() ;
        this.reader = new InputStreamReader(inputStream,"UTF-8") ;
        this.br = new BufferedReader(reader) ;

    }

    @Override
    public void run(){
        String result ;
        while(true){
            try {
                result = br.readLine();
                System.out.println();
                System.out.println("받은 메세지" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
