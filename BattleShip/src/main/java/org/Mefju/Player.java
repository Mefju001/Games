package org.Mefju;
import java.io.*;
import java.net.*;
import java.util.*;

public class Player {
    private static final int[][] grid = new int[10][10];
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

        while (true) {
            int[] shipSizes = {4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
            for (int i = 0; i < shipSizes.length; i++) {
                boolean placed = false;
                while (!placed) {
                    System.out.println(STR."Umieść statek o długości \{shipSizes[i]} (np. A1 H||A1 V):");
                    String input = scanner.nextLine();
                    placed = placeShipFromInput(input, shipSizes[i]);
                }
            }
            synchronized (lock) {
                System.out.println();
                System.out.print("Your move (e.g. A5): ");
            }
            String move = scanner.nextLine();
            out.println(move);
            int[]result = parseMove(move);
            System.out.println(Arrays.toString(result));
            if(isHit(result[0], result[1]))
                out.println("Trafiony!");
        }
    }
    private static boolean isHit(int row, int col)
    {
        if(grid[row][col] == 1) {//1 to ze jest 0 ze nie 2 ze trafiony i -1 pudło
            grid[row][col] = 2;
            return true;
        } else if (grid[row][col] == 0) {
            grid[row][col] = -1;
        }
        return false;
    }
    private static int[]parseMove(String move) {
        move = move.toUpperCase();
        char col = move.charAt(0);
        int row = Integer.parseInt(move.substring(1))-1;
        int colIndex = index.getOrDefault(col,-1);
        if(colIndex == -1||row<0||row>=10){
            throw new IllegalArgumentException(STR."Invalid move \{move}");
        }
        return new int[]{row,colIndex};
    }
    private static boolean placeShipFromInput(String input, int size)
    {
        String[] parts = input.trim().split("\\s+");
        if(parts.length<2) return false;
        int[]index = parseMove(parts[0]);
        char direction = parts[1].toUpperCase().charAt(0);
        boolean horizontal = (direction == 'H' );
        if(horizontal&&index[1] +size>10||!horizontal&&index[1] +size>10)
        {
            System.out.println("Statek wychodzi poza planszę!");
            return false;
        }
        for(int x = 0;x<size;x++)
        {
            int r=index[0]+(horizontal?0:x);
            int c =index[1]+(horizontal?x:0);
            if(grid[r][c]!=0){
                System.out.println("Znajduje się tam statek");
                return false;
            }
        }
        for(int x = 0;x<size;x++)
        {
            int r=index[0]+(horizontal?0:x);
            int c =index[1]+(horizontal?x:0);
            grid[r][c]=1;
        }
        return true;
    }
}

