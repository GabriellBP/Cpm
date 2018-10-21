package br.ufal.ic.syntatic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Grammar {
    private BufferedReader buffer;
    ArrayList<Production> productions = new ArrayList<Production>();

    public Grammar(String filepath) throws IOException {
        this.buffer = new BufferedReader(new FileReader(filepath));

        String line = buffer.readLine();
        while (line != null) {
            if (!line.equals("")) {
                productions.add(new Production(line));
//                System.out.println(productions.get(productions.size() - 1));
            }
            line = buffer.readLine();
        }
    }
}
