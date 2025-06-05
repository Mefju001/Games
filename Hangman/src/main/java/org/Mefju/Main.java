package org.Mefju;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello world!");
        HangmanServer server = new HangmanServer();
        server.start(5000);
    }
}