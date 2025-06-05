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
    private HangmanGame game;
    private boolean isWordSet = false;

    public void setGame(HangmanGame game) {
        this.game = game;
    }

    public void setWordSet(boolean wordSet) {
        isWordSet = wordSet;
    }

    public boolean isWordSet() {
        return isWordSet;
    }

    public PlayerHandler(Socket socket, int playerId) throws IOException {
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
        try {
            if (isWordSet) {
                send("Enter the secret word: ");
                String input = in.readLine();
                if (input.startsWith("WORD")) {
                    String secretWord = input.substring(5).toLowerCase();
                    game.setSecretWord(secretWord);
                    opponent.send("Start guessing!");
                    send("Word set. Waiting for opponent...");
                }
            } else {
                send("Start guessing by sending: GUESS <letter>");
                String input;
                while ((input = in.readLine()) != null || !game.isGameOver()) {
                    if (input.startsWith("GUESS")) {
                        String lowerCase = input.substring(6).toLowerCase();
                        if (lowerCase.isEmpty()) {
                            send("No letter provided");
                            continue;
                        }
                        char c = lowerCase.charAt(0);
                        boolean find = game.guessLetter(c);
                        if (find) send("Correct");
                        else send("Wrong " + game.getRemainingLives());
                        if (game.isWordGuessed()) {
                            send("You win!");
                            opponent.send("Opponent guess your word");
                            break;
                        }
                        if (game.getRemainingLives() <= 0) {
                            send("You lose word was: " + game.getSecretWord());
                            opponent.send("You win!");
                            break;
                        }
                        send("Current: " + game.getMaskedWord());
                    } else
                        send("Unknown command");
                }
            }
        } catch (IOException e) {
            System.out.println("Player " + playerId + " disconnected.");
        }
    }
    }
