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
    private TicTacToeBoard grid;
    private static final Map<Character, Integer> index = Map.of(
            'A', 0,
            'B', 1,
            'C', 2
    );
    public PlayerHandler(Socket socket, int playerId) throws IOException {
        this.socket = socket;
        this.playerId = playerId;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        out.println(STR."WELCOME Player \{playerId}");
    }
    public void setOpponent(PlayerHandler opponent) {
        this.opponent = opponent;
    }
    public void send(String message) {
        out.println(message);
    }
    public void setBoard(TicTacToeBoard board) {
        this.grid = board;
    }

    @Override
    public void run() {
        try {
            String input;
            while ((input = in.readLine()) != null) {
                if(playerId != grid.getCurrentPlayer()) {
                    send("Wait");
                    continue;
                }
                if(input.startsWith("MOVE")) {
                    String move = input.substring(5).toUpperCase();
                    int row = move.charAt(1) - '1';
                    int col = move.charAt(0) - 'A';
                    if (!grid.makeMove(row, col, playerId)) {
                        send("Invalid Move");
                    } else {
                        opponent.send("OPPONENT MOVED " + move);
                        grid.printBoard();
                    }
                    int winner = grid.checkWinner();
                    if (winner == playerId) {
                        send("YOU WIN!");
                        opponent.send("YOU LOSE!");
                        return;
                    } else if (winner == 3) {
                        send("Draw");
                        opponent.send("Draw");
                        return;
                    } else {
                        grid.switchPlayer();
                        opponent.send("Your turn");
                    }
                }else {
                    send("Unknown command");
                }
            }
        } catch(IOException e){
            System.out.println("Player " + playerId + " disconnected.");
        }
    }
}
