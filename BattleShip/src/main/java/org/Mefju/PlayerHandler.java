package org.Mefju;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerHandler implements Runnable{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private PlayerHandler opponent;
    private final int playerId;
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

    @Override
    public void run() {
        try {
            String move;
            while ((move = in.readLine()) != null) {
                System.out.println("Player " + playerId + ": " + move);
                if (opponent != null) {
                    opponent.send("OPPONENT_MOVE " + move);
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + playerId + " disconnected.");
        }
    }
}
