package Lesson_7;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 * Обслуживает клиента (отвечает за связь между клиентом и сервером)
 */
public class ClientHandler {

    private MyServer server;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;

    private String name;

    public String getName() {
        return name;
    }

//    long startTime = System.currentTimeMillis();
//    long elapsedTime = 0L;



    public ClientHandler(MyServer server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            this.inputStream = new DataInputStream(socket.getInputStream());
            this.outputStream = new DataOutputStream(socket.getOutputStream());
            this.name = "";
            new Thread(() -> {
                try {
                  //  while (elapsedTime <0.5 * 60 * 1000) {
                        authentification();
                   //     break;
                   // }

                    readMessages();

                    //иначе brake
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }

            }).start();
        } catch (IOException ex) {
            System.out.println("Проблема при создании клиента");
        }
    }

    private void readMessages() throws IOException {
        while (true) {
            String messageFromClient = inputStream.readUTF();
            System.out.println("от " + name + ": " + messageFromClient);
            if (messageFromClient.equals(ChatConstans.STOP_WORD)) {
                return;
            } else if (messageFromClient.startsWith(ChatConstans.SEND_TO_LIST)) {
                String[] splittedStr = messageFromClient.split("\\s+");
                List<String> nicknames = new ArrayList<>();
                for (int i = 1; i < splittedStr.length - 1; i++) {
                    nicknames.add(splittedStr[i]);
                }
            } else if (messageFromClient.startsWith(ChatConstans.CLIENTS_LIST)) {
                server.broadcastClients();
            } else {
                server.broadcastMessage("[" + name + "]: " + messageFromClient);
            }

        }
    }

    // /auth login pass
    private void authentification() throws IOException {
        while (true) {
            String message = inputStream.readUTF();
            if (message.startsWith(ChatConstans.AUTH_COMMAND)) {
                String[] parts = message.split("\\s+");
                Optional<String> nick = server.getAuthService().getNickByLoginAndPass(parts[1], parts[2]);
                if (nick.isPresent()) {
                    //проверим, что такого нет
                    if (!server.isNickBusy(nick.get())) {
                        sendMsg(ChatConstans.AUTH_OK + " " + nick);
                        name = nick.get();
                        server.subscribe(this);
                        server.broadcastMessage(name + " вошел в чат");
                        return;
                    } else {
                        sendMsg("Ник уже используется");
                    }
                } else {
                    sendMsg("Неверные логин/пароль");
                }
            }
        }
    }

    public void sendMsg(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        server.unsubscribe(this);
        server.broadcastMessage(name + " вышел из чата");
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}