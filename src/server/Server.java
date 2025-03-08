package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        int port = 80; // 클라이언트가 접속할 포트 번호 설정

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("서버가 포트 " + port + "에서 대기 중입니다...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트가 연결됨: " + socket.getInetAddress());

                // 클라이언트와 통신을 위한 쓰레드 실행
                new ServerReadThread(socket).start();
                new ServerWriteThread(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}