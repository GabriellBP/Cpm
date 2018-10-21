package br.ufal.ic.lexic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LexicalTable {
    public static Map<String, TokenCategory> keywords = new HashMap<String, TokenCategory>();
    public static Map<String, TokenCategory> delimiters = new HashMap<String, TokenCategory>();
    public static Map<String, TokenCategory> separators = new HashMap<String, TokenCategory>();
    public static Map<String, TokenCategory> operators = new HashMap<String, TokenCategory>();
    public static List<Character> tokenEndings = new ArrayList();

    static {
        keywords.put("main", TokenCategory.main);
        keywords.put("int", TokenCategory.tInt);
        keywords.put("float", TokenCategory.tFloat);
        keywords.put("bool", TokenCategory.tBool);
        keywords.put("char", TokenCategory.tChar);
        keywords.put("string", TokenCategory.tString);
        keywords.put("if", TokenCategory.rwIf);
        keywords.put("elif", TokenCategory.rwElif);
        keywords.put("else", TokenCategory.rwElse);
        keywords.put("for", TokenCategory.rwFor);
        keywords.put("while", TokenCategory.rwWhile);
        keywords.put("print", TokenCategory.rwPrint);
        keywords.put("read", TokenCategory.rwRead);
        keywords.put("length", TokenCategory.rwLength);
        keywords.put("return", TokenCategory.rwReturn);
        keywords.put("void", TokenCategory.rwVoid);

        delimiters.put(";", TokenCategory.lineEnd);
        delimiters.put("[", TokenCategory.arrayBeg);
        delimiters.put("]", TokenCategory.arrayEnd);
        delimiters.put("(", TokenCategory.paramBeg);
        delimiters.put(")", TokenCategory.paramEnd);
        delimiters.put("{", TokenCategory.scopeBeg);
        delimiters.put("}", TokenCategory.scopeEnd);

        separators.put(",", TokenCategory.commaSep);
        separators.put(":", TokenCategory.colonSep);

        operators.put("!", TokenCategory.opNot);
        operators.put("=", TokenCategory.opAssign);
        operators.put("||", TokenCategory.opLogic);
        operators.put("&&", TokenCategory.opLogic);
        operators.put("+", TokenCategory.opPlus);
        operators.put("-", TokenCategory.opMinus);
        operators.put("*", TokenCategory.opMult);
        operators.put("/", TokenCategory.opMult);
        operators.put("^", TokenCategory.opExp);
        operators.put("==", TokenCategory.opEquals);
        operators.put("!=", TokenCategory.opEquals);
        operators.put(">", TokenCategory.opRel);
        operators.put("<", TokenCategory.opRel);
        operators.put(">=", TokenCategory.opRel);
        operators.put("<=", TokenCategory.opRel);

        tokenEndings.add(' ');
        tokenEndings.add('\t');
        tokenEndings.add('\n');
        tokenEndings.add(',');
        tokenEndings.add(';');
        tokenEndings.add('&');
        tokenEndings.add('|');
        tokenEndings.add('+');
        tokenEndings.add('-');
        tokenEndings.add('*');
        tokenEndings.add('/');
        tokenEndings.add('^');
        tokenEndings.add('!');
        tokenEndings.add('<');
        tokenEndings.add('>');
        tokenEndings.add('=');
        tokenEndings.add('(');
        tokenEndings.add(')');
        tokenEndings.add('[');
        tokenEndings.add(']');
        tokenEndings.add('{');
        tokenEndings.add('}');
        tokenEndings.add('\'');
        tokenEndings.add('"');
        tokenEndings.add(':');
    }

//    NOT USED: id, consNumInt, consNumFlo, consBool, consChar, consString,
}
