package br.ufal.ic.lexic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Lexic {
    private Token token;
    private String currentLineContent;
    private int currentLine, currentColumn;
    private BufferedReader buffer;

    public Lexic(String filepath) throws FileNotFoundException {
        this.currentLine = currentColumn = -1;
        this.buffer = new BufferedReader(new FileReader(filepath));
    }

    private boolean setNextLine() {
        String line = null;

        try {
            line = buffer.readLine();
        } catch(Exception e) {
            e.printStackTrace();
        }

        if (line != null) {
            currentLineContent = line;
            return true;
        }
        return false;
    }

    public boolean hasNextToken() {
//        if (this.currentLine == 0 && this.currentColumn == 0) { // file not red yet
//            if (!setNextLine()) { // file is empty
//                return false; // there is no more tokens (the file is empty)
//            }
//        }

        while (setNextLine()) { // iterates until reaches the end of the file
            currentLine++;
            currentColumn = 0;

            if (!currentLineContent.matches("[^ \t]")) {
                return true;
            }
        }
        return false;
    }

    public Token nextToken() {
        char c = currentLineContent.charAt(currentColumn);
        while (c == ' ' || c == '\t') {
            c = nextChar();
        }

        Token token;
        int tLine = currentLine;
        int tColumn = currentColumn;
        String tValue = "";


        return token;
    }

    private char nextChar() {
        currentColumn++;
        if (currentColumn < currentLineContent.length()) {
            return currentLineContent.charAt(currentColumn);
        }
        return '\n';
    }
}
