package pl.lodz.p.it.sise;

import pl.lodz.p.it.sise.logic.Graph;
import pl.lodz.p.it.sise.logic.Strategies;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Program {
    private int x, y;
    private int[][] board;

    public int[][] getBoard() {
        return this.board;
    }

    public void setBoard(int[][] temp) {
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp.length; j++) {
                this.board[i][j] = temp[i][j];
            }
        }
    }

    public void readBoard(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("[^0-9]+");
        List<Integer> list = new ArrayList<Integer>();
        while (sc.hasNext()) {
            list.add(Integer.valueOf(sc.next()));
        }
        this.x = list.get(0);
        this.y = list.get(1);
        this.board = new int[x][y];
        int k = 1;
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                k++;
                this.board[i][j] = list.get(k);
            }
        }
    }

    public void runProgram(String strategy, String order, int[][] puzzleToSolve, String solutionFileName, String additionalFileName) {
        Graph graph = new Graph();
        Strategies strategies = new Strategies();
        if (Objects.equals(strategy, "bfs")) {
            strategies.bfs(graph, puzzleToSolve, order, solutionFileName, additionalFileName);
        } else if (Objects.equals(strategy, "dfs")) {
            strategies.dfs(graph, puzzleToSolve, order, solutionFileName, additionalFileName);
        } else if (Objects.equals(strategy, "astr")) {
            strategies.astr(graph, puzzleToSolve, order, solutionFileName, additionalFileName);
        } else {
            throw new IllegalArgumentException("Wrong strategy");
        }
    }

    public static void main(String[] args) {
        Program program = new Program();
        try {
            program.readBoard(args[2]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        program.runProgram(args[0], args[1], program.getBoard(), args[3], args[4]);
    }
}
