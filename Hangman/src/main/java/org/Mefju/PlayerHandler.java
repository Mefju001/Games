package org.Mefju;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerHandler implements Runnable{
    private Socket socket;
    private int playerId;
    private BufferedReader in;
    private PrintWriter out;
    private PlayerHandler opponent;

    public PlayerHandler(Socket socket,int playerId) throws IOException {
        this.socket = socket;
        this.playerId=playerId;
        setInOut();
        out.println("Welcome player:"+playerId);
    }
    private void setInOut() throws IOException {
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(),true );
    }
    public void setOpponent(PlayerHandler opponent){
        this.opponent = opponent;
    }
    public void send(String message)
    {
        out.println(message);
    }
    @Override
    public void run() {

    }
}
