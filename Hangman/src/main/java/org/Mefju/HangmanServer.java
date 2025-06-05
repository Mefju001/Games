package org.Mefju;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.spec.RSAOtherPrimeInfo;

public class HangmanServer {
    private ServerSocket serverSocket;
    public void start(int port)throws IOException
    {
        serverSocket = new ServerSocket(port);
        System.out.println("Serwer wystartował");

        Socket player1Socket =serverSocket.accept();
        System.out.println("Player 1 connected");
        PlayerHandler player1 = new PlayerHandler(player1Socket,1);
        Socket player2Socket =serverSocket.accept();
        System.out.println("Player 2 connected");
        PlayerHandler player2 = new PlayerHandler(player2Socket,2);
        HangmanGame game = new HangmanGame();
        player1.setGame(game);
        player2.setGame(game);
        player1.setOpponent(player2);
        player2.setOpponent(player1);
        player1.setWordSet(true);
        player2.setWordSet(false);
        System.out.println("Ustawiono przeciwników");

        new Thread(player1).start();
        new Thread(player2).start();
        System.out.println("Waiting for word");

    }
}
