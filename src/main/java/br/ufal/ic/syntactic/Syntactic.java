package br.ufal.ic.syntactic;

import br.ufal.ic.lexic.Lexic;
import br.ufal.ic.lexic.Token;
import br.ufal.ic.lexic.TokenCategory;
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
        this.stack = new Stack();
    }

    public boolean analyze() {
        this.stack.push(new Tuple(0));
        int state, tokenColumn;
        String action = "";

        Token token = null;
        if (lexic.hasNextToken()) {
            token = lexic.nextToken();
            System.out.println(token);
        }

        if (token == null) {
            System.err.println("Erro! Arquivo vazio.");
            return false;
        }

        while (!action.equals("acc")) {
            state = this.stack.peek().state;

            tokenColumn = this.slrTableAction.getTableHeader().get(token == null ? "$" : token.getCategory().toString());
            action = this.slrTableAction.getTableContent()[state][tokenColumn];

            if (action == null) {
                if (token == null) {
                    System.err.println("Código não reconhecido!");
                } else {
                    System.err.printf("Erro! [Linha %d, coluna %d]: (%s, '%s') inesperado", token.getTokenLine(), token.getTokenColumn(), token.getCategory().toString(), token.getValue());
                }
                return false;
            } else if (action.startsWith("s")) {
                this.stack.push(new Tuple(Integer.valueOf(action.substring(1)), token == null ? "$" : token.getCategory().toString(), true));

                if (lexic.hasNextToken()) {
                    token = lexic.nextToken();
                    System.out.println(token);

                    if (token == null || token.getCategory() == TokenCategory.unknown) { // erro léxico
                        return false;
                    }
                } else {
                    token = null;
                }

            } else if (action.startsWith("r")) {
                int prod = Integer.valueOf(action.substring(1));
                Production production = this.grammar.getProductions().get(prod);
                System.out.println(production.toString());

                for (int i = production.getSize(); i > 0; i--)
                    this.stack.pop();

                tokenColumn = this.slrTableTransition.getTableHeader().get(production.getLeft());
                state = Integer.valueOf(this.slrTableTransition.getTableContent()[this.stack.peek().state][tokenColumn]);
                stack.push(new Tuple(state, production.getLeft(), false));
            }
        }

        System.out.println("ACEITO!");
        return true;
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

        @Override
        public String toString() {
            return "(state: " + state + ", element: " + element + ", isToken: " + isToken + ")";
        }
    }
}
