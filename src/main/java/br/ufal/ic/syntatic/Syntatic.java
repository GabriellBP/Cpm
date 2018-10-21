package br.ufal.ic.syntatic;

import br.ufal.ic.lexic.Lexic;

public class Syntatic {
    private Lexic lexic;
    private Grammar grammar;

    public Syntatic(Lexic lexic, Grammar grammar) {
        this.lexic = lexic;
        this.grammar = grammar;
    }

    public void analyze() {
        while(lexic.hasNextToken()) {
            System.out.println(lexic.nextToken());
        }
    }
}
