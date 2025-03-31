package TestChattingPro;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class ChatClient {
    private static final String SERVER_IP = "172.30.1.98";
    private static final int PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private DataOutputStream fileOut;
    private String nickname;
    private JFrame frame;
    private JTextArea chatArea;
    private JTextField inputField;

    public ChatClient() {
        frame = new JFrame("채팅 클라이언트");
        chatArea = new JTextArea(20, 50);
        chatArea.setEditable(false);
        inputField = new JTextField(40);
        JButton sendButton = new JButton("전송");
        JButton fileButton = new JButton("파일 전송");

        JPanel panel = new JPanel();
        panel.add(inputField);
        panel.add(sendButton);
        panel.add(fileButton);

        frame.getContentPane().add(new JScrollPane(chatArea), BorderLayout.CENTER);
        frame.getContentPane().add(panel, BorderLayout.SOUTH);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        sendButton.addActionListener(e -> sendMessage());
        fileButton.addActionListener(e -> sendFile());
        inputField.addActionListener(e -> sendMessage());

        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket(SERVER_IP, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            fileOut = new DataOutputStream(socket.getOutputStream());

            nickname = JOptionPane.showInputDialog(frame, "닉네임을 입력하세요:");
            out.println(nickname);

            new Thread(() -> {
                try {
                    String message;
                    while ((message = in.readLine()) != null) {
                        chatArea.append(message + "\n");
                    }
                } catch (IOException e) {
                    chatArea.append("서버 연결 종료됨\n");
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage() {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            out.println(message);
            chatArea.append("[나] " + message + "\n"); // 내가 보낸 메시지를 채팅창에 추가
            inputField.setText("");
        }
    }

    private void sendFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                byte[] fileData = new byte[(int) file.length()];
                new FileInputStream(file).read(fileData);

                fileOut.writeUTF("/file " + file.getName());
                fileOut.writeInt(fileData.length);
                fileOut.write(fileData);
                fileOut.flush();

                chatArea.append("[파일 전송] " + file.getName() + "을(를) 보냈습니다.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClient::new);
    }
}
