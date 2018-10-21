package br.ufal.ic;

import br.ufal.ic.lexic.Lexic;
import br.ufal.ic.syntactic.slr.SLRTableMaker;
import br.ufal.ic.syntactic.slr.Grammar;
import br.ufal.ic.syntactic.Syntactic;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Lexic lexic = new Lexic(args[0]);

            Grammar grammar = new Grammar(args[1]);
            SLRTableMaker slrTableAction = new SLRTableMaker(args[2]);
            SLRTableMaker slrTableTransition = new SLRTableMaker(args[3]);
            Syntactic syntactic = new Syntactic(lexic, grammar, slrTableAction, slrTableTransition);
            syntactic.analyze();

        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
