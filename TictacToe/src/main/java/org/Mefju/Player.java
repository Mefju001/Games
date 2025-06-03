package org.Mefju;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Scanner;

public class Player {
    private static final Object lock = new Object();

    public static void main(String[] args) throws IOException {
        try {
            Socket socket = new Socket("localhost", 5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);

            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println("SERVER: " + line);
                    }
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();

            while (true) {
                System.out.print("Enter your move (e.g., A1): ");
                String move = scanner.nextLine().toUpperCase().trim();
                if (move.matches("^[A-C][1-3]$")) {
                    out.println("MOVE " + move);
                } else {
                    System.out.println("Invalid move format. Use format like A1, B2, etc.");
                }
            }

        } catch (IOException e) {
            System.out.println("Could not connect to server: " + e.getMessage());
        }
    }
}
