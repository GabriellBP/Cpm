package br.ufal.ic;

import br.ufal.ic.lexic.Lexic;
import br.ufal.ic.syntactic.slr.SLRTable;
import br.ufal.ic.syntactic.slr.Grammar;
import br.ufal.ic.syntactic.Syntactic;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Lexic lexic = new Lexic(args[0]);

            Grammar grammar = new Grammar(args[1]);

            assert args[2].length() != 0;

            SLRTable slrTableAction = new SLRTable(args[2]);
            SLRTable slrTableTransition = new SLRTable(args[3]);

            assert slrTableAction != null;
            assert slrTableTransition != null;

            Syntactic syntactic = new Syntactic(lexic, grammar, slrTableAction, slrTableTransition);
            syntactic.analyze();

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
