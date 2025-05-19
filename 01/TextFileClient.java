import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class TextFileClient {
    private static final int NUM_TOP_WORDS = 5;

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.out.println("Usage: java TextFileClient <host1> <port1> <host2> <port2>");
            return;
        }

        String host1 = args[0];
        int port1 = Integer.parseInt(args[1]);
        String host2 = args[2];
        int port2 = Integer.parseInt(args[3]);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Future<WordCounter> future1 = executor.submit(() -> readFromServer(host1, port1));
        Future<WordCounter> future2 = executor.submit(() -> readFromServer(host2, port2));

        WordCounter totalCounter = new WordCounter();
        totalCounter.merge(future1.get());
        totalCounter.merge(future2.get());

        executor.shutdown();

        System.out.println("Top " + NUM_TOP_WORDS + " most common words:");
        for (Map.Entry<String, Integer> entry : totalCounter.getTopWords(NUM_TOP_WORDS)) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static WordCounter readFromServer(String host, int port) {
        WordCounter counter = new WordCounter();

        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String line;
            while ((line = reader.readLine()) != null) {
                counter.processLine(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading from server " + host + ":" + port + " - " + e.getMessage());
        }

        return counter;
    }
}
