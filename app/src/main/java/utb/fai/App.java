package utb.fai;

public class App {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Použití: java App <ip_adresa> <port>");
            return;
        }

        String serverIp = args[0];
        int port;

        try {
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.out.println("Neplatné číslo portu.");
            return;
        }

        TelnetClient telnetClient = new TelnetClient(serverIp, port);
        telnetClient.run();
    }
}
