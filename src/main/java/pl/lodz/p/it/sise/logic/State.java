package pl.lodz.p.it.sise.logic;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class State implements Comparable<State> {

    private int[][] state = new int[4][4];
    private int xZero;
    private int yZero;
    private String operator = "";
    private String parentOperator = "";
    private int hValue = 0;

    public int gethValue() {
        return this.hValue;
    }

    public void sethValue(int value) {
        this.hValue = value;
    }

    @Override
    public int compareTo(State otherState) {
        return Integer.compare(this.gethValue(), otherState.gethValue());
    }

    public int[][] getState() {
        return state;
    }

    public void setState(int[][] state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                this.state[i][j] = state[i][j];
            }
        }
    }

    public String getParentOperator() {
        return parentOperator;
    }

    public void setParentOperator(String operator) {
        this.parentOperator = operator + this.getOperator();
    }

    public int getxZero() {
        return xZero;
    }

    public void setxZero(int xZero) {
        this.xZero = xZero;
    }

    public int getyZero() {
        return yZero;
    }

    public void setyZero(int yZero) {
        this.yZero = yZero;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public State(int[][] temp) {
        this.setState(temp);
        findZeroPosition();
    }

    public boolean isGoal(Object o) {
        return equals(o);
    }

    @Override
    public boolean equals(Object o) {
        return new EqualsBuilder().append(o, this.state)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 41).append(this.state).toHashCode();
    }

    public void findZeroPosition() {
        for (int i = 0; i < this.state.length; i++) {
            for (int j = 0; j < this.state.length; j++) {
                if (this.state[i][j] == 0) {
                    this.xZero = i;
                    this.yZero = j;
                }
            }
        }
    }
}
