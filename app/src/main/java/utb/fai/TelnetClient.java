package utb.fai;

import java.io.*;
import java.net.*;

public class TelnetClient {

    private String serverIp;
    private int port;

    public TelnetClient(String serverIp, int port) {
        this.serverIp = serverIp;
        this.port = port;
    }

    public void run() {
        try  {
            Socket socket = new Socket(serverIp, port);

            Thread sendThread = new Thread(new Sender(socket));
            Thread receiveThread = new Thread(new Receiver(socket));

            sendThread.start();
            receiveThread.start();

            sendThread.join();
            receiveThread.join();

        } catch (IOException | InterruptedException e) {
            System.out.println("Spojení bylo uzavřeno.");
            e.printStackTrace();
        }
    }

    private class Sender implements Runnable {
        private Socket socket;

        public Sender(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                BufferedWriter socketOut = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String line;
                while ((line = userInput.readLine()) != null) {
                    if (line.equals("/QUIT")) {
                        socket.close();
                        System.exit(0);
                    }
                    socketOut.write(line);
                    socketOut.newLine();
                    socketOut.flush();
                }
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private class Receiver implements Runnable {
        private Socket socket;

        public Receiver(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream in = socket.getInputStream();
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(in));

                while (!socket.isClosed()) {
                    if (in.available() > 0) {
                        String line = socketIn.readLine();
                        if (line != null) {
                            System.out.println(line);
                        }
                    } else {
                        Thread.sleep(20);
                    }
                }
            } catch (IOException | InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
