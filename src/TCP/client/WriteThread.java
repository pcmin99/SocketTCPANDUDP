package TCP.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class WriteThread extends Thread{
    Socket socket;
    OutputStream outputStream;
    PrintStream printStream;

    public WriteThread(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = socket.getOutputStream();
        this.printStream = new PrintStream(outputStream);
    }

    @Override
    public void run() {
        String message;
        Scanner sc;
        sc = new Scanner(System.in);

        while(true){
            try {
                System.out.print("메시지 입력 : ");
                message = sc.nextLine();
                printStream.println(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}