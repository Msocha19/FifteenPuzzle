package pl.lodz.p.it.sise.logic;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private String order = "";

    public void setOrder(String order) {
        this.order = order;
    }

    List<State> neighbours(State state) {
        if (this.order == "") {
            this.order = "LRUD";
        }
        List<State> result = new ArrayList<>();
        this.move(this.order.charAt(0), state, result);
        this.move(this.order.charAt(1), state, result);
        this.move(this.order.charAt(2), state, result);
        this.move(this.order.charAt(3), state, result);
        return result;
    }

    public boolean checkL(int y) {
        boolean isAbleToMove = true;
        if (y == 0) {
            isAbleToMove = false;
        }
        return isAbleToMove;
    }

    public boolean checkR(int y) {
        boolean isAbleToMove = true;
        if (y == 3) {
            isAbleToMove = false;
        }
        return isAbleToMove;
    }

    public boolean checkU(int x) {
        boolean isAbleToMove = true;
        if (x == 0) {
            isAbleToMove = false;
        }
        return isAbleToMove;
    }

    public boolean checkD(int x) {
        boolean isAbleToMove = true;
        if (x == 3) {
            isAbleToMove = false;
        }
        return isAbleToMove;
    }

    public void move(char state, State temp, List<State> list) {
        switch (state){
            case 'L':
                if (this.checkL(temp.getyZero())) {
                     list.add(this.L(temp));
                }
                break;
            case 'R':
                if (this.checkR(temp.getyZero())) {
                    list.add(this.R(temp));
                }
                break;
            case 'U':
                if (this.checkU(temp.getxZero())) {
                    list.add(this.U(temp));
                }
                break;
            case 'D':
                if (this.checkD(temp.getxZero())) {
                    list.add(this.D(temp));
                }
                break;
        }
    }

    public int[][] shallowCopy(State temp) {
        int[][] result = new int[temp.getState().length][temp.getState().length];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result[i][j] = temp.getState()[i][j];
            }
        }
        return result;
    }

    State L(State temp) {
        int[][] result = this.shallowCopy(temp);
        result[temp.getxZero()][temp.getyZero()] = result[temp.getxZero()][temp.getyZero() - 1];
        result[temp.getxZero()][temp.getyZero() - 1] = 0;
        State copy = new State(result);
        copy.setOperator("L");
        return copy;
    }

    State R(State temp) {
        int[][] result = this.shallowCopy(temp);
        result[temp.getxZero()][temp.getyZero()] = result[temp.getxZero()][temp.getyZero() + 1];
        result[temp.getxZero()][temp.getyZero() + 1] = 0;
        State copy = new State(result);
        copy.setOperator("R");
        return copy;
    }

    State U(State temp) {
        int[][] result = this.shallowCopy(temp);
        result[temp.getxZero()][temp.getyZero()] = result[temp.getxZero() - 1][temp.getyZero()];
        result[temp.getxZero() - 1][temp.getyZero()] = 0;
        State copy = new State(result);
        copy.setOperator("U");
        return copy;
    }

    State D(State temp) {
        int[][] result = this.shallowCopy(temp);
        result[temp.getxZero()][temp.getyZero()] = result[temp.getxZero() + 1][temp.getyZero()];
        result[temp.getxZero() + 1][temp.getyZero()] = 0;
        State copy = new State(result);
        copy.setOperator("D");
        return copy;
    }
}
