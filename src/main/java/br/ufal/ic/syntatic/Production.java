package br.ufal.ic.syntatic;

public class Production {
    int size;
    String left;
    String[] right;

    Production(String prod) {
        String[] elements = prod.split("=");
        left = elements[0].trim();
        right = elements[1].trim().split(" ");
        size = right.length;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder("_" + left + "_ =");
        for (String el : right) {
            ret.append(" _").append(el).append("_");
        }

        return ret.toString();
    }
}
