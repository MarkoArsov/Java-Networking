package Server;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.chrono.ChronoLocalDateTime;
import java.util.Date;
import java.util.Scanner;

public class Worker extends Thread {
    Socket socket;

    public Worker(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        File defaultFolder = new File("default");
        if (!defaultFolder.exists()) {
            defaultFolder.mkdir();
        }
        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String line = null;
            while (!(line = reader.readLine()).isEmpty()) {
                writer.write(line + "\n");
                writer.flush();
                String[] tmp = line.split(" ");
                boolean operation = false;
                if (tmp[0].equals("CREATE")) {
                    operation = this.createFile(tmp[1]);
                }
                if (tmp[0].equals("COPY")) {
                    String from = tmp[1];
                    String to = tmp[2];
                    operation = this.copy(from, to);
                }
                if (tmp[0].equals("LIST")) {
                    operation = this.list(writer);
                }
                if (tmp[0].equals("DELETE")) {
                    operation = this.delete(tmp[1]);
                }
                if (tmp[0].equals("DOWNLOAD")) {
                    operation = this.downloadFile(tmp[1], writer);
                }
                String output = "Failed";
                if (operation) {
                    output = "Success";
                }
                System.out.println(output);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized boolean createFile(String fileName) throws IOException {
        File file = new File("default\\" + fileName + ".txt");
        if (!file.exists()){
            file.createNewFile();
            return true;
        }
        return false;

    }

    public synchronized boolean copy(String file1, String file2) throws IOException {
        File from = new File("default\\" + file1 + ".txt");
        if (!from.exists()) return false;
        File to = new File("default\\" + file2 + ".txt");
        if (!to.exists()) {
            to.createNewFile();
        }
        BufferedReader reader = new BufferedReader(new FileReader(from));
        BufferedWriter writer = new BufferedWriter(new FileWriter(to));
        String line = null;
        while ((line = reader.readLine()) != null) {
            writer.write(line + "\n");
            writer.flush();
        }
        writer.close();
        reader.close();
        return true;
    }

    public synchronized boolean list(BufferedWriter writer) throws IOException {
        File folder = new File("default");
        if (!folder.exists() || !folder.isDirectory()) {
            return false;
        }
        recList(folder, writer);
        return true;
    }

    public synchronized void recList(File folder, BufferedWriter writer) throws IOException {
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                recList(files[i], writer);
            }
            String date = new Date(files[i].lastModified()).toString();
            System.out.println(files[i].getName() + " " + files[i].length() + " " + date);
            writer.write(files[i].getName() + " " + files[i].length() + " " + date + "\n");
            writer.flush();
        }
    }

    public synchronized boolean downloadFile(String fileName, BufferedWriter writer) throws IOException {
        File file = new File("default\\" + fileName + ".txt");
        if (!file.exists()) return false;
        writer.write("DOWNLOAD STARTED"+"\n");
        writer.flush();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        while ((line = reader.readLine()) != null) {
            writer.write(line + "\n");
            writer.flush();
        }
        writer.write("DOWNLOAD FINISHED" +"\n");
        writer.flush();
        return true;
    }

    public synchronized boolean delete(String fileName) {
        File file = new File("default\\" + fileName + ".txt");
        if (!file.exists()) {
            return false;
        }
        file.delete();
        return true;
    }

}
