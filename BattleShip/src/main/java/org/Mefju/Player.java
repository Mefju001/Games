package org.Mefju;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Player {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Scanner scanner = new Scanner(System.in);

        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    System.out.println("SERVER: " + response);
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        }).start();
        
        while (true) {
            System.out.print("Your move (e.g. A5): ");
            String move = scanner.nextLine();
            out.println(move);
        }
    }
}

