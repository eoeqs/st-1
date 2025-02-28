package org.eoeqs.domain;

public class SceneNarrator {
    private final Chapter chapter;
    private final MoltenMetalStreams moltenMetalStreams;

    public SceneNarrator(Chapter chapter, MoltenMetalStreams moltenMetalStreams) {
        this.chapter = chapter;
        this.moltenMetalStreams = moltenMetalStreams;
    }

    public String narrateScene() {
        StringBuilder narrative = new StringBuilder();

        Bombardment bombardment = chapter.getBombardment();
        narrative.append("Bombardment resumed. ");

        Environment environment = chapter.getEnvironment();
        narrative.append("Heat and noise were unimaginable. ");

        ComputerBank computerBank = chapter.getComputerBank();
        narrative.append("The computer bank began to gradually disintegrate into pieces. ");

        FrontSide frontSide = computerBank.getFrontSide();
        narrative.append("Its front side had ")
                .append(frontSide.getMeltedDegree())
                .append(" melted, ");

        narrative.append("and ")
                .append(moltenMetalStreams.getDensity())
                .append(" streams of molten metal began creeping into the corner where they were sitting. ");

        People people = chapter.getCorner().getPeople();
        narrative.append("They huddled ")
                .append(people.getGroupDensity())
                .append(" and began waiting for the end. ");

        narrative.append("Chapter ")
                .append(chapter.getNumber());

        return narrative.toString();
    }
}