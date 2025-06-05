package org.Mefju;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Player {
    public static void main(String[] args) {
        try {
            final Socket socket = new Socket("localhost",5000);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out  = new PrintWriter(socket.getOutputStream(),true);
            Scanner scanner =new Scanner(System.in);
            new Thread(() -> {
                try {
                    String line;
                    while ((line = in.readLine()) != null)
                        System.out.println("Server: " + line);
                } catch (IOException e) {
                    System.out.println("Disconnected from server.");
                }
            }).start();
            String secretWord = scanner.nextLine().toLowerCase().trim();
            out.println("WORD "+secretWord);
            while (true) {
                System.out.println("Enter your geuss (letter): ");
                String guess = scanner.nextLine().toLowerCase().trim();
                if(guess.length() == 1&& Character.isLetter(guess.charAt(0))){
                    out.println("GUESS "+guess);
                }else System.out.println("Invalid letter");

            }
        } catch (IOException e) {
            System.out.println("Could not connect to server: " + e.getMessage());
        }
    }
}
