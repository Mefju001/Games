package org.Mefju;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class PlayerHandler implements Runnable{
    private Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private PlayerHandler opponent;
    private final int playerId;
    private int strike = 0;
    private int[][] grid;
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
    public PlayerHandler(Socket socket, int playerId) throws IOException {
        this.socket = socket;
        this.playerId = playerId;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        out.println(STR."WELCOME Player \{playerId}");
    }
    public void setGrid(int[][] grid){
        this.grid = grid;
    }
    public void setOpponent(PlayerHandler opponent) {
        this.opponent = opponent;
    }
    public void send(String message) {
        out.println(message);
    }
    private boolean isHit(int row,int col)
    {
        if(grid[row][col] == 1) {//1 to ze jest 0 ze nie 2 ze trafiony i -1 pudło
            grid[row][col] = 2;
            return true;
        } else if (grid[row][col] == 0) {
            grid[row][col] = -1;
        }
        return false;
    }
    private boolean gameOver()
    {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == 1) {
                    return false;
                }
            }
        }
        return true;
    }
    private int[]parseMove(String move) {
        move = move.toUpperCase();
        char col = move.charAt(0);
        int row = Integer.parseInt(move.substring(1))-1;
        int colIndex = index.getOrDefault(col,-1);
        if(colIndex == -1||row<0||row>=10){
            throw new IllegalArgumentException(STR."Invalid move \{move}");
        }
        return new int[]{row,colIndex};
    }
    private boolean placeShipFromInput(String input, int size)
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
    @Override
    public void run() {
        try {
            String move;
            while ((move = in.readLine()) != null) {
                System.out.println("DEBUG: Otrzymano od gracza: " + move);  // <- dodaj to
                if (move.startsWith("Place")) {
                    String[] parts = move.split(" ");
                    // parts[0] == "Place"
                    String place = parts[1];  // np. "A1"
                    String direction = parts[2]; // np. "H"
                    int size = Integer.parseInt(parts[3]);
                    String input = place + " " + direction;
                    boolean wynik = placeShipFromInput(input, size);
                    /*if(wynik)
                        out.println("placed ok");
                    else
                        out.println("error");*/
                } else if(move.startsWith("Move")){
                    System.out.println(STR."Player \{playerId}: \{move}");
                    if (opponent != null) {
                        opponent.send(STR."OPPONENT_MOVE \{move}");
                        String[] parts = move.split(" ");
                        // parts[0] == "Place"
                        String place = parts[1];  // np. "A1"
                        int[] coords = parseMove(place);
                        int row = coords[0];
                        int col = coords[1];

                        boolean hit = opponent.isHit(row, col);

                        if (hit) {
                            out.println("Trafiony!");
                            strike++;
                            opponent.out.println("OPPONENT_MOVE " + move + " Trafiony!");
                        } else {
                            out.println("Pudło!");
                            opponent.out.println("OPPONENT_MOVE " + move + " Pudło!");
                        }
                        boolean gameFinish = opponent.gameOver();
                        if(gameFinish) {
                            out.println("Wygrałeś!");
                            opponent.out.println("Przegrałeś!");
                            return;
                        }
                    }
                }
            }
        } catch(IOException e){
            System.out.println("Player " + playerId + " disconnected.");
        }
    }
}
