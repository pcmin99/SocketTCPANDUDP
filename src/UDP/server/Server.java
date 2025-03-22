package UDP.server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class Server {

    public static  void main(String[] args){
        int port = 12345 ;

        try (DatagramSocket serverSocket = new DatagramSocket(port)){
            System.out.print("UDP 서버가 포트" + port + "대기중");

            byte[] receiveBuffer = new byte[1024];

            while(true){

                // 클라에서 보낸 데이터 수신
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocket.receive(receivePacket); //데이터 받을때까지 대기

                // 받은 데이터 출력
                String message = new String(receivePacket.getData(), 0 , receivePacket.getLength());
                System.out.println("클라 메세지 :" + message) ;

                // 응답 메세지 출력
                String response = "서버 응답: " + message.toUpperCase();
                byte[] sendBuffer = response.getBytes();

                // 클라에게 응답 전송
                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer, sendBuffer.length,receivePacket.getAddress(), receivePacket.getPort()
                );
                serverSocket.send(sendPacket);

            }

        } catch (Exception e) {
           e.printStackTrace();
        }
    }


}
