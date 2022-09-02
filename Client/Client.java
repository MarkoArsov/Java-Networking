package Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread {
    String serverIP;
    int serverPort;

    public Client(String serverIP, int serverPort) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(serverIP, serverPort);

            Sender sender = new Sender(socket);
            Receiver receiver = new Receiver(socket);
            sender.start();
            receiver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 9000);
        client.start();
    }

}

class Sender extends Thread {
    Socket socket;

    public Sender(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String line = scanner.nextLine();
                writer.write(line + "\n");
                writer.flush();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Receiver extends Thread {
    Socket socket;

    public Receiver(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while (true) {
                line = reader.readLine();
                if (line != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
