package TCP.client;

import java.io.IOException;
import java.net.Socket;

public class Client {
        public static void main(String[] args){
            Socket clientSocket ;

            try{
                // 클라이언트가 서버에 연결 시도
                clientSocket = new Socket("172.30.1.64",80);

                ReadThread readThread = new ReadThread(clientSocket); // 서버에서 메시지를 읽음
                WriteThread writeThread = new WriteThread(clientSocket); // 서버로 메시지 보냄

                // 각각 실ㅂ
                readThread.start();
                writeThread.start();

            }catch (IOException e){
                throw new RuntimeException(e);
            }


        }

}
