package org.Mefju;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        BattleshipServer server = new BattleshipServer();
        server.start(5000);
    }
}