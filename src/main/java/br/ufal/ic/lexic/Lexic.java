package br.ufal.ic.lexic;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

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
        this.currentLine = -1;
        this.currentColumn = 0;
        this.buffer = new BufferedReader(new FileReader(filepath));
    }

    public boolean hasNextToken() {
        String line = currentLineContent != null ? currentLineContent.substring(currentColumn) : null;
//        if (line != null) System.out.print("> " + line);
        try {
            while (line == null || !line.matches("[\\s]*[^\\s].*")) {
                line = buffer.readLine();
                currentLine++;
//                System.out.print(" (++) ");
                currentColumn = 0;

                if (line == null) {
//                    System.out.println(" FALSE");
                    return false;
                }
                currentLineContent = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(" TRUE");
        return true;
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
//            System.out.print(c);
            while (!LexicalTable.tokenEndings.contains(c)) {
                tValue.append(c);
                c = nextChar();
            }
//            System.out.println(" -->" + tValue + " (" + currentLine + ", " + currentColumn + ")");
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
        if (value.equals("-") && isUnaryNegative()) {
            return TokenCategory.opUnMinus;
        } else if (LexicalTable.keywords.containsKey(value)) {
            return LexicalTable.keywords.get(value);
        } else if (LexicalTable.separators.containsKey(value)) {
            return LexicalTable.separators.get(value);
        } else if (LexicalTable.operators.containsKey(value)) {
            return LexicalTable.operators.get(value);
        } else if (LexicalTable.delimiters.containsKey(value)) {
            return LexicalTable.delimiters.get(value);
        }
        return consOrId(value);
    }

    private boolean isUnaryNegative(){
        if (previousToken != null) {
            TokenCategory prevTokenCat = previousToken.getCategory();

            if (prevTokenCat == TokenCategory.consNumInt || prevTokenCat == TokenCategory.consNumFlo) {
                return false;
            }
            return prevTokenCat != TokenCategory.id && prevTokenCat != TokenCategory.paramEnd;
        }
        return true;
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
