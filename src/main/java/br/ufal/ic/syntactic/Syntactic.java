package br.ufal.ic.syntactic;

import br.ufal.ic.lexic.Lexic;
import br.ufal.ic.lexic.Token;
import br.ufal.ic.syntactic.slr.Grammar;
import br.ufal.ic.syntactic.slr.Production;
import br.ufal.ic.syntactic.slr.SLRTable;

import java.util.Stack;

public class Syntactic {
    private Lexic lexic;
    private Grammar grammar;
    private SLRTable slrTableAction, slrTableTransition;
    private Stack<Tuple> stack;

    public Syntactic(Lexic lexic, Grammar grammar, SLRTable slrTableAction, SLRTable slrTableTransition) {
        this.lexic = lexic;
        this.grammar = grammar;
        this.slrTableAction = slrTableAction;
        this.slrTableTransition = slrTableTransition;
        this.stack = new Stack<>();
    }

    public boolean analyze() {
        this.stack.push(new Tuple(0));
        int state, tokenColumn;
        String action;
        while(lexic.hasNextToken()) {
            Token token = lexic.nextToken();
            System.out.println(token);

            state = this.stack.peek().state;
            System.out.println(this.slrTableAction.getTableHeader().keySet());
            System.out.println(this.slrTableAction.getTableHeader().values());
            System.out.println(this.slrTableAction.getTableHeader().get("id"));
            assert this.slrTableAction != null;
            assert this.slrTableAction.getTableHeader() != null;
            assert token != null;
            assert token.getCategory() != null;
            assert this.slrTableAction.getTableHeader().get(token.getCategory().toString()) != null;

            tokenColumn = this.slrTableAction.getTableHeader().get(token.getCategory().toString());

             action = this.slrTableAction.getTableContent()[state][tokenColumn];

             if (action.startsWith("s")) {
                this.stack.push(new Tuple(Integer.valueOf(action.substring(1)), token.getCategory().toString(), true));
             } else if (action.startsWith("r")) {
                 int prod = Integer.valueOf(action.substring(1));
                 Production production = this.grammar.getProductions().get(prod);
                 for (int i = production.getSize(); i > 0; i--)
                     this.stack.pop();
                 tokenColumn = this.slrTableTransition.getTableHeader().get(production.getLeft());
                 state = Integer.valueOf(this.slrTableTransition.getTableContent()[this.stack.peek().state][tokenColumn]);
                 stack.push(new Tuple(state, production.getLeft(), false));
             }
        }
        state = this.stack.peek().state;
        action = slrTableAction.getTableContent()[state][slrTableAction.getTableContentNumColumns() - 1];

        if (action.equals("acc")) {
            System.out.println("ACEITO!");
            return true;
        }
        System.out.println("FALHOU!");
        return false;
    }

    private class Tuple {
        int state;
        String element;
        boolean isToken;

        Tuple(int state) {
            this.state = state;
        }

        Tuple(int state, String element, boolean isToken) {
            this.state = state;
            this.element = element;
            this.isToken = isToken;
        }
    }
}
