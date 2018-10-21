package br.ufal.ic;

import br.ufal.ic.lexic.Lexic;
import br.ufal.ic.syntatic.Grammar;
import br.ufal.ic.syntatic.Syntatic;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Lexic lexic = new Lexic(args[0]);

            Grammar grammar = new Grammar(args[1]);
            Syntatic syntatic = new Syntatic(lexic, grammar);
            syntatic.analyze();
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
