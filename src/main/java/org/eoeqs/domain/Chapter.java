package org.eoeqs.domain;
public class Chapter {
    private int number;
    private Bombardment bombardment;
    private Environment environment;
    private ComputerBank computerBank;
    private Corner corner;

    public Chapter(int number, Bombardment bombardment, Environment environment,
                   ComputerBank computerBank, Corner corner) {
        this.number = number;
        this.bombardment = bombardment;
        this.environment = environment;
        this.computerBank = computerBank;
        this.corner = corner;
    }

    public int getNumber() {
        return number;
    }

    public Bombardment getBombardment() {
        return bombardment;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public ComputerBank getComputerBank() {
        return computerBank;
    }

    public Corner getCorner() {
        return corner;
    }

    public void updateScene() {
        environment.updateHeat(bombardment);
        computerBank.updateState(environment);
    }
}