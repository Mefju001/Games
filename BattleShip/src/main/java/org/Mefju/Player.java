package org.Mefju;
import java.io.*;
import java.net.*;
import java.util.*;

public class Player {
    private static final Object lock = new Object();

    private static final Map<Character, Integer> index = Map.of(
            'A', 0,
            'B', 1,
            'C', 2,
            'D', 3,
            'E', 4,
            'F', 5,
            'G', 6,
            'H', 7,
            'I', 8,
            'J', 9
    );
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 5000);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in);
        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    synchronized (lock) {
                        System.out.println(STR."SERVER: \{response}");
                    }
                }
            } catch (IOException e) {
                System.out.println("Disconnected from server.");
            }
        }).start();
        int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        for (int i = 0; i < shipSizes.length; i++) {
            boolean placed = false;
            while (!placed) {
                System.out.println(STR."Umieść statek o długości \{shipSizes[i]} (np. A1 H||A1 V):");
                String place = scanner.nextLine();
                out.println("Place " + place + " " + shipSizes[i]);
                placed = true;
                //String response = in.readLine();

               /* System.out.println("DEBUG: otrzymano od serwera: [" + response + "]");
                if(response.equals("placed ok"))
                    placed =true;
                else
                    System.out.println("Błąd przy dodawaniu statku, spróbuj ponownie.");
            */}
        }
        while (true) {
            synchronized (lock) {
                System.out.println();
                System.out.print("Your move (e.g. A5): ");
            }
            String move = scanner.nextLine();
            out.println("Move "+move);
        }
    }

}

