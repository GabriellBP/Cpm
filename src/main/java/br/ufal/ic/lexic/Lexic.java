package br.ufal.ic.lexic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Lexic {
    private String currentLineContent;
    private int currentLine, currentColumn;
    private BufferedReader buffer;
    private boolean reachedEOF = false;

    public Lexic(String filepath) throws FileNotFoundException {
        this.currentLine = -1;
        this.currentColumn = 0;
        this.buffer = new BufferedReader(new FileReader(filepath));
    }

    public boolean hasNextToken() {
        if (this.reachedEOF) {
            return false;
        }

        String line = currentLineContent != null ? currentLineContent.substring(currentColumn) : null;
        try {
            while (line == null || !line.matches("[\\s]*[^\\s].*")) {
                line = buffer.readLine();
                currentLine++;
                currentColumn = 0;

                if (line == null) {
                    this.reachedEOF = true;
                    return false;
                }
                currentLineContent = line;
                System.out.printf("%4d  %s\n", currentLine + 1, currentLineContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

                    if (c == '\n') {
                        printError(tLine, currentColumn + 1, "caractere '\"' esperado");
                        return null;
                    }
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
                } else {
                    printError(tLine, currentColumn + 1, "caractere '\'' esperado");
                    return null;
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
                } else {
                    printError(tLine, currentColumn + 1, "caractere '&' esperado");
                    return null;
                }
            } else if (c == '|') {
                tValue.append(c);
                c = nextChar();
                if (c == '|') {
                    tValue.append(c);
                    currentColumn++;
                } else {
                    printError(tLine, currentColumn + 1, "caractere '|' esperado");
                    return null;
                }
            } else {
                tValue.append(c);
                currentColumn++;
            }
        }
        String value = tValue.toString().trim();
        token = new Token(value, tLine, tColumn, findTokenCategory(value));

        if (token.getCategory() == TokenCategory.unknown) {
            printError(token.getTokenLine(), token.getTokenColumn(), "'" + token.getValue() +"' inesperado");
        }
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
        if (LexicalTable.keywords.containsKey(value)) {
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
        } else if(tokenValue.matches("[A-Za-z][A-Za-z0-9]*")) {
            return TokenCategory.id;
        }

        return TokenCategory.unknown;
    }

    private void printError(int line, int column, String message) {
        System.err.printf("Erro! [Linha %d, coluna %d]: %s.", line, column, message);
    }
}
