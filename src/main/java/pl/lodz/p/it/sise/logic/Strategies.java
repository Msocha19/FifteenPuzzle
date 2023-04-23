package pl.lodz.p.it.sise.logic;

import pl.lodz.p.it.sise.logic.Graph;
import pl.lodz.p.it.sise.logic.State;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.lang.Math;

public class Strategies {

    private int[][] goal = new int[4][4];

    public Strategies() {
        int k = 1;
        for (int i = 0; i < this.goal.length; i++) {
            for (int j = 0; j < this.goal.length; j++) {
                if (k == 16) {
                    this.goal[i][j] = 0;
                } else {
                    this.goal[i][j] = k;
                    k++;
                }
            }
        }
    }

    public void writeToFileSolution(String parentOperators, Boolean isSolution, String solutionName) {
            try {
                File file = new File(solutionName);
                if (file.exists()) {
                    file.delete();
                    file.createNewFile();
                } else {
                    file.createNewFile();
                }
                PrintWriter printWriter = new PrintWriter(file.getName());
                if (isSolution) {
                    printWriter.println(parentOperators.length());
                    printWriter.println(parentOperators);
                } else {
                    printWriter.println(-1);
                }
                printWriter.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
    }

    public void writeToFileAdditional(int lenght, int visited, int computed, int maxDeph, double runTime, String additionalName) {
        try {
            File file = new File(additionalName);
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            } else {
                file.createNewFile();
            }
            PrintWriter printWriter = new PrintWriter(file.getName());
            printWriter.println(lenght);
            printWriter.println(visited);
            printWriter.println(computed);
            printWriter.println(maxDeph);
            printWriter.println(runTime);
            printWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private int maxDeph(String deph, int previousMax) {
        int check = deph.length();
        if (check < previousMax) {
            check = previousMax;
        }
        return check;
    }

    public boolean bfs(Graph graph, int[][] start, String order, String solutionFile, String additionalFile) {
        double startTime = System.nanoTime();
        double endTime;
        int maxDeph = 0;
        graph.setOrder(order);
        State state = new State(start);
        if (state.isGoal(this.goal)) {
            writeToFileSolution(state.getParentOperator(), true, solutionFile);
            endTime = System.nanoTime();
            writeToFileAdditional(0, 1, 1, 0, ((endTime - startTime)/1000000),additionalFile);
            return true;
        }
        Queue<State> queue = new LinkedList<>();
        Set<State> closed = new HashSet<>();
        queue.add(state);
        closed.add(state);
        while (!queue.isEmpty()) {
            State temp = queue.remove();
            if (temp.getParentOperator().length() <= 20) {
                for (State n : graph.neighbours(temp)) {
                    n.setParentOperator(temp.getParentOperator());
                    maxDeph = this.maxDeph(n.getParentOperator(), maxDeph);
                    if (n.isGoal(this.goal)) {
                        writeToFileSolution(n.getParentOperator(), true, solutionFile);
                        endTime = System.nanoTime();
                        writeToFileAdditional(n.getParentOperator().length(), closed.size(), closed.size() - queue.size(), maxDeph, ((endTime - startTime)/1000000.0),additionalFile);
                        return true;
                    }
                    if (!closed.contains(n)) {
                        queue.add(n);
                        closed.add(n);
                    }
                }
            }
        }
        writeToFileSolution(state.getParentOperator(), false,solutionFile);
        endTime = System.nanoTime();
        writeToFileAdditional(-1, closed.size(), closed.size() - queue.size(), maxDeph, ((endTime - startTime)/1000000),additionalFile);
        return false;
    }

    public boolean dfs(Graph graph, int[][] start, String order, String solutionFile, String additionalFile) {
        int computed = 0;
        double startTime = System.nanoTime();
        double endTime;
        int maxDeph = 0;
        graph.setOrder(order);
        State startState = new State(start);
        if (startState.isGoal(this.goal)) {
            writeToFileSolution(startState.getParentOperator(), true, solutionFile);
            endTime = System.nanoTime();
            writeToFileAdditional(0, 1, 1, 0, ((endTime - startTime)/1000000), additionalFile);
            return true;
        }
        Stack<State> S = new Stack<>();
        Set<State> T = new HashSet<>();
        S.push(startState);
        while (!S.isEmpty()) {
            State temp = S.pop();
            computed++;
            if (temp.getParentOperator().length() <= 20) {
                if (!T.contains(temp)) {
                    T.add(temp);
                    List<State> neighbours = graph.neighbours(temp);
                    Collections.reverse(neighbours);
                    for (State n : neighbours) {
                        n.setParentOperator(temp.getParentOperator());
                        maxDeph = this.maxDeph(n.getParentOperator(), maxDeph);
                        if (n.isGoal(this.goal)) {
                            writeToFileSolution(n.getParentOperator(), true, solutionFile);
                            endTime = System.nanoTime();
                            writeToFileAdditional(n.getParentOperator().length(), S.size() + computed, T.size(), maxDeph, ((endTime - startTime)/1000000),additionalFile);
                            return true;
                        }
                        S.push(n);
                     }
                }
            }
        }
        writeToFileSolution(startState.getParentOperator(), false,solutionFile);
        endTime = System.nanoTime();
        writeToFileAdditional(-1, S.size() + computed, T.size(), maxDeph, ((endTime - startTime)/1000000), additionalFile);
        return false;
    }

    int hamming(State state) {
        int count = 0;
        for (int i = 0; i < state.getState().length; i++) {
            for (int j = 0; j < state.getState().length; j++) {
                if (state.getState()[i][j] != this.goal[i][j] && state.getState()[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    int manhattan(State state) {
        int distance = 0;
        for (int i = 0; i < state.getState().length; i++) {
            for (int j = 0; j < state.getState().length; j++) {
                if (state.getState()[i][j] != this.goal[i][j] && state.getState()[i][j] != 0) {
                    for (int k = 0; k < state.getState().length; k++) {
                        for (int m = 0; m < state.getState().length; m++) {
                            if (this.goal[k][m] == state.getState()[i][j]) {
                                distance += Math.abs(k - i) + Math.abs(m - j);
                            }
                        }
                    }
                }
            }
        }
        return distance;
    }

    int heuristic(String name, State state) {
        if (Objects.equals(name, "hamm")) {
            return hamming(state);
        } else if (Objects.equals(name, "manh")) {
            return manhattan(state);
        }
        return 0;
    }

    public boolean astr(Graph G, int[][] start, String h, String solutionFile, String additionalFile) {
        int computed = 0;
        double startTime = System.nanoTime();
        double endTime;
        int maxDeph = 0;
        State startState = new State(start);
        if (startState.isGoal(this.goal)) {
            writeToFileSolution(startState.getParentOperator(), true, solutionFile);
            endTime = System.nanoTime();
            writeToFileAdditional(0, 1, 1, 0, ((endTime - startTime)/1000000),additionalFile);
            return true;
        }
        PriorityQueue<State> P = new PriorityQueue<>();
        Set<State> T = new HashSet<>();
        startState.sethValue(0);
        P.add(startState);
        while (!P.isEmpty()) {
            State temp = P.remove();
            computed++;
            if (temp.isGoal(this.goal)) {
                writeToFileSolution(temp.getParentOperator(), true,solutionFile);
                endTime = System.nanoTime();
                writeToFileAdditional(temp.getParentOperator().length(), P.size() + computed, T.size(), maxDeph, ((endTime - startTime)/1000000),additionalFile);
                return true;
            }
            T.add(temp);
            for(State n : G.neighbours(temp)) {
                if (!T.contains(n)) {
                    n.setParentOperator(temp.getParentOperator());
                    maxDeph = this.maxDeph(n.getParentOperator(), maxDeph);
                    int f = n.getParentOperator().length() + this.heuristic(h, n);
                    if (!P.contains(n)) {
                        n.sethValue(f);
                        P.add(n);
                    } else {
                        P.remove(n);
                        n.sethValue(f);
                        P.add(n);
                    }
                }
            }
        }
        writeToFileSolution(startState.getParentOperator(), false,solutionFile);
        endTime = System.nanoTime();
        writeToFileAdditional(-1, P.size() + computed, T.size(), maxDeph, ((endTime - startTime)/1000000),additionalFile);
        return false;
    }
}
