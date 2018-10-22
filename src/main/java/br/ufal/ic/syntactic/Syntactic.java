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
            System.out.println("Erro! Arquivo vazio.");
            return false;
        }

        while (!action.equals("acc")) {
//            System.out.println("\n" + stack.peek() + " " + (token == null ? "$" : token.getCategory()));

            state = this.stack.peek().state;

//            assert this.slrTableAction != null;
//            assert this.slrTableAction.getTableHeader() != null;
//            assert (token == null ? "$" : token.getCategory().toString()) != null;
//            if (this.slrTableAction.getTableHeader().get(token == null ? "$" : token.getCategory().toString()) == null) {
//                System.out.println(">>>>>>>> KEY ERROR: " + (token == null ? "$" : token.getCategory().toString()));
//            }

            tokenColumn = this.slrTableAction.getTableHeader().get(token == null ? "$" : token.getCategory().toString());
            action = this.slrTableAction.getTableContent()[state][tokenColumn];
//            System.out.println("ACTION: " + action);

            if (action == null) {
                System.out.println("Erro!");
                return false;
            } else if (action.startsWith("s")) {
                this.stack.push(new Tuple(Integer.valueOf(action.substring(1)), token == null ? "$" : token.getCategory().toString(), true));

                if (lexic.hasNextToken()) {
                    token = lexic.nextToken();
                    System.out.println(token);
                } else {
                    token = null;
                }

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
