package org.Mefju;

import java.util.HashSet;
import java.util.Set;

public class HangmanGame {
    private String secretWord = "";
    Set<Character> guessedLetters = new HashSet<>();
    Set<Character> missedLetters = new HashSet<>();
    int remainingLives=4;
    public String getSecretWord() {
        return secretWord;
    }

    public int getRemainingLives() {
        return remainingLives;
    }
    public boolean isGameOver(){
        return remainingLives<=0||isWordGuessed();
    }
    public void setSecretWord(String secretWord) {
        this.secretWord = secretWord.toLowerCase();
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
        else
        {
            missedLetters.add(letter);
            remainingLives--;
        }
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
    public boolean isWordGuessed(){
        if(secretWord == null)return false;
        for(char c: secretWord.toCharArray())
        {
            if (!guessedLetters.contains(c))return false;
        }
        return true;
    }
}
