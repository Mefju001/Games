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
        PlayerHandler player1 = new PlayerHandler(player1socket,1);

        Socket player2socket = serverSocket.accept();
        System.out.println("Player 2 connected");
        PlayerHandler player2 = new PlayerHandler(player2socket,2);

        int[][] playerGrid = new int[3][3];
        System.out.println("Dodano tabelke");

        player1.setOpponent(player2);
        player2.setOpponent(player1);

        player1.setGrid(playerGrid);
        player2.setGrid(playerGrid);

        new Thread(player1).start();
        new Thread(player2).start();

    }
}
