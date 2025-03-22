package TestChattingPro;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
    private static final int PORT = 12345;
    private static Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("서버가 " + PORT + " 포트에서 대기 중입니다...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("새 클라이언트 연결됨: " + socket.getInetAddress());

                ClientHandler clientHandler = new ClientHandler(socket);
                clients.add(clientHandler);
                clientHandler.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public static void sendFile(byte[] fileData, String fileName, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendFile(fileData, fileName);
                }
            }
        }
    }

    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }
}

class ClientHandler extends Thread {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private String nickname;
    private DataOutputStream fileOut;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            fileOut = new DataOutputStream(socket.getOutputStream());

            out.println("닉네임을 입력하세요:");
            nickname = in.readLine();
            System.out.println(nickname + " 님이 연결되었습니다.");

            ChatServer.broadcast(nickname + " 님이 입장했습니다.", this);

            String message;
            while ((message = in.readLine()) != null) {
                if (message.startsWith("/file")) {
                    receiveFile();
                } else {
                    ChatServer.broadcast(nickname + ": " + message, this);
                }
            }
        } catch (IOException e) {
            System.out.println(nickname + " 님이 퇴장하였습니다.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ChatServer.removeClient(this);
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendFile(byte[] fileData, String fileName) {
        try {
            fileOut.writeUTF("/file " + fileName);
            fileOut.writeInt(fileData.length);
            fileOut.write(fileData);
            fileOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveFile() {
        try {
            DataInputStream fileIn = new DataInputStream(socket.getInputStream());
            String fileName = fileIn.readUTF();
            int fileSize = fileIn.readInt();
            byte[] fileData = new byte[fileSize];
            fileIn.readFully(fileData);
            System.out.println(nickname + " 님이 파일을 보냈습니다: " + fileName);

            ChatServer.sendFile(fileData, fileName, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
