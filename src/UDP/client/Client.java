package UDP.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        String serverIP = "127.0.0.1"; // 서버 IP
        int serverPort = 12345; // 서버 포트

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                // 메시지 입력
                System.out.print("보낼 메시지 입력: ");
                String message = scanner.nextLine();
                byte[] sendBuffer = message.getBytes();

                // 서버로 메시지 전송
                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer, sendBuffer.length, serverAddress, serverPort
                );
                clientSocket.send(sendPacket);

                // 서버 응답 수신
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                clientSocket.receive(receivePacket);

                // 응답 출력
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("서버 응답: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
