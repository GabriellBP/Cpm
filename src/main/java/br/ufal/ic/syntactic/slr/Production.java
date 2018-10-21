package br.ufal.ic.syntactic.slr;

public class Production {
    private int size;
    private String left;
    private String[] right;

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

    public int getSize() {
        return size;
    }

    public String getLeft() {
        return left;
    }

    public String[] getRight() {
        return right;
    }
}
