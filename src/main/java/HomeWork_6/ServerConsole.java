package HomeWork_6;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerConsole {

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Socket socket = null;
        try (ServerSocket serverSocket = new ServerSocket(Constants.PORT)) {
            System.out.println("Сервер запущен, ожидаем подключения...");
            socket = serverSocket.accept();
            System.out.println("Клиент подключился");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread clientVoice = new Thread(() -> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.equals(Constants.STOP_WORD2)) {
                            break;
                        }
                        else {
                            out.writeUTF("Клиент: " + str);

                            System.out.println("Клиент: " + str);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.println("Клиент отключился от сервера");

            });

            Thread serverVoice = new Thread(() -> {
                try {
                    while (true) {
                        String serverMessage = scanner.nextLine();
                        if (serverMessage.equals(Constants.STOP_WORD2)) {
                            break;


                        } else {
                            out.writeUTF("Сервер: " + serverMessage);
                            //System.out.println("Ответ от сервера: ");
                            System.out.println("Сервер: " + serverMessage);
                        }
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

            clientVoice.start();
            serverVoice.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}