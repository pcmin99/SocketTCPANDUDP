package server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ServerWriteThread extends Thread {
    private Socket socket;
    private PrintStream printStream;

    public ServerWriteThread(Socket socket) throws IOException {
        this.socket = socket;
        this.printStream = new PrintStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        String message;

        while (true) {
            try {
                System.out.print("클라이언트에게 보낼 메시지: ");
                message = sc.nextLine();
                printStream.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}