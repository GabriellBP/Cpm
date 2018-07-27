package br.ufal.ic.lexic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Lexic {
    private Token previousToken;
    private Token currentToken;
    private String currentLineContent;
    private int currentLine, currentColumn;
    private BufferedReader buffer;

    public Lexic(String filepath) throws FileNotFoundException {
        this.currentLine = currentColumn = -1;
        this.buffer = new BufferedReader(new FileReader(filepath));
    }

    public boolean hasNextToken() {
        String line = null;
        try {
            do {
                line = buffer.readLine();
                currentLine++;
                currentColumn = 0;
            } while (line != null && line.matches("[^\\s]\\n?"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (line != null) {
            currentLineContent = line;
            return true;
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
        StringBuilder tValue = new StringBuilder();

        if (Character.toString(c).matches("\\d")) {
            boolean floatDetected = false;
            while (Character.toString(c).matches("\\d") || (!floatDetected && c == '.')) {
                if (c == '.') {
                    floatDetected = true;
                }

                tValue.append(c);
                c = nextChar();
            }
        } else {
            while (!LexicalTable.tokenEndings.contains(c)) {
                tValue.append(c);
                c = nextChar();
            }
        }

        if (tValue.length() == 0) {
            if (c == '"') { // begin of a string
                tValue.append(c);
                c = nextChar();
                while (c != '"') { // reads until finds "
                    tValue.append(c);
                    c = nextChar();
                }
                tValue.append(c);
                currentColumn++;
            } else if (c == '\'') {
                tValue.append(c);
                c = nextChar();
                if (c == '\\') {
                    c = nextChar();
                }
                tValue.append(c);
                c = nextChar();
                if (c == '\'') {
                    tValue.append(c);
                    currentColumn++;
                }
            } else if (c == '<' || c == '=' || c == '>') {
                tValue.append(c);
                c = nextChar();
                if (c == '=') {
                    tValue.append(c);
                    currentColumn++;
                }
            } else if (c == '&') {
                tValue.append(c);
                c = nextChar();
                if (c == '&') {
                    tValue.append(c);
                    currentColumn++;
                }
            } else if (c == '|') {
                tValue.append(c);
                c = nextChar();
                if (c == '|') {
                    tValue.append(c);
                    currentColumn++;
                }
            } else {
                tValue.append(c);
                currentColumn++;
            }
        }
        String value = tValue.toString().trim();
        token = new Token(value, tLine, tColumn, findTokenCategory(value));
        previousToken = currentToken;
        currentToken = token;
        return token;
    }

    private char nextChar() {
        currentColumn++;
        if (currentColumn < currentLineContent.length()) {
            return currentLineContent.charAt(currentColumn);
        }
        return '\n';
    }

    private TokenCategory findTokenCategory(String value) {
        if(value.equals("-") && isUnaryNegative()){
            return TokenCategory.opUnMinus;
        } else if(LexicalTable.keywords.containsKey(value)) {
            return LexicalTable.keywords.get(value);
        } else if(LexicalTable.separators.containsKey(value)) {
            return LexicalTable.separators.get(value);
        } else if(LexicalTable.operators.containsKey(value)) {
            return LexicalTable.operators.get(value);
        }
        return consOrId(value);
    }

    private TokenCategory consOrId(String tokenValue) {
        if(tokenValue.matches("\\d+")) {
            return TokenCategory.consNumInt;
        } else if(tokenValue.matches("(\\d)+\\.(\\d)+")) {
            return TokenCategory.consNumFlo;
        } else if(tokenValue.startsWith("\"") && tokenValue.length() > 1 && tokenValue.endsWith("\"")) {
           return TokenCategory.consString;
        } else if(tokenValue.startsWith("\'") && tokenValue.length() == 1 && tokenValue.endsWith("\'")) {
            return TokenCategory.consChar;
        } else if(tokenValue.equals("true") || tokenValue.equals("false")) {
            return TokenCategory.consBool;
        } else if(tokenValue.matches("[a-z_A-Z](\\w)*")) {
            return TokenCategory.id;
        }

        return TokenCategory.unknown;
    }
}
