package org.Mefju;

import java.util.Set;

public class HangmanGame {
    private String secretWord;
    Set<Character> guessedLetters;
    Set<Character> missedLetters;
    int remainingLives=4;
    public String getSecretWord() {
        return secretWord;
    }
    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord;
    }
    public boolean guessLetter(char letter)
    {
        if (guessedLetters.contains(letter) || missedLetters.contains(letter)) {
            return false;
        }
        boolean found = false;
        for(char word:secretWord.toCharArray())
        {
            if(word == letter)
            {
                found = true;
            }
        }
        if(found)guessedLetters.add(letter);
        else missedLetters.add(letter);
        return found;
    }
    public String getMaskedWord()
    {
        StringBuilder masked = new StringBuilder();
        for(char c:secretWord.toCharArray())
        {
            if(guessedLetters.contains(c))
            {
                masked.append(c).append(" ");
            }
            else{
                masked.append(" _ ");
            }
        }
        return masked.toString().trim();
    }
}
