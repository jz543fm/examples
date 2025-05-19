import java.io.*;
import java.net.*;

public class TextFileServer {
    private int port;
    private String filePath;

    public TextFileServer(int port, String filePath) {
        this.port = port;
        this.filePath = filePath;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Serving file: " + filePath + " on port " + port);
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                     BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                    writer.flush();
                } catch (IOException e) {
                    System.err.println("Error handling client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Could not start server on port " + port + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java TextFileServer <port> <filePath>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        String filePath = args[1];
        new TextFileServer(port, filePath).start();
    }
}
