package br.ufal.ic.syntactic;

import br.ufal.ic.lexic.Lexic;
import br.ufal.ic.syntactic.slr.Grammar;
import br.ufal.ic.syntactic.slr.SLRTableMaker;

public class Syntactic {
    private Lexic lexic;
    private Grammar grammar;

    public Syntactic(Lexic lexic, Grammar grammar, SLRTableMaker slrTableAction, SLRTableMaker slrTableTransition) {
        this.lexic = lexic;
        this.grammar = grammar;
    }

    public void analyze() {
        while(lexic.hasNextToken()) {
            System.out.println(lexic.nextToken());
        }
    }
}
