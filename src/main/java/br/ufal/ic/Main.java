package br.ufal.ic;

import br.ufal.ic.lexic.Lexic;

import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        try {
            Lexic lexic = new Lexic(args[0]);

            while(lexic.hasNextToken()) {
                System.out.println(lexic.nextToken());
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
