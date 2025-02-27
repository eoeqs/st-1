package org.eoeqs.domain;

public class ComputerBank {
    private String state;
    private FrontSide frontSide;

    public ComputerBank(String state, FrontSide frontSide) {
        this.state = state;
        this.frontSide = frontSide;
    }

    public String getState() {
        return state;
    }

    public FrontSide getFrontSide() {
        return frontSide;
    }

    public void updateState(Environment environment) {
        frontSide.updateMeltingState(environment.getHeat());
        if (frontSide.isMelted()) {
            state = "disintegrating";
        }
    }
}