package TCP.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerReadThread extends Thread {
    private Socket socket;
    private BufferedReader reader;

    public ServerReadThread(Socket socket) throws IOException {
        this.socket = socket;
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
    }

    @Override
    public void run() {
        String message;
        try {
            while ((message = reader.readLine()) != null) {
                System.out.println("클라이언트 메시지: " + message);
            }
        } catch (IOException e) {
            System.out.println("클라이언트 연결 종료");
        }
    }
}