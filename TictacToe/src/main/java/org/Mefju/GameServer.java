package org.Mefju;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket serverSocket;
    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server start");

        Socket player1socket = serverSocket.accept();
        System.out.println("Player 1 connected");
        PlayerHandler player1 = new PlayerHandler(player1socket, 1);

        Socket player2socket = serverSocket.accept();
        System.out.println("Player 2 connected");
        PlayerHandler player2 = new PlayerHandler(player2socket, 2);

        TicTacToeBoard game = new TicTacToeBoard();

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        player1.setBoard(game);
        player2.setBoard(game);

        new Thread(player1).start();
        new Thread(player2).start();

        System.out.println("Game initialized. Waiting for moves...");
    }
}
