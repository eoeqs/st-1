package org.eoeqs;

import org.eoeqs.domain.*;

public class Main {
    public static void main(String[] args) {
        Bombardment bombardment = new Bombardment(true, "high", 30);
        Environment env = new Environment(1300, "unimaginable");
        FrontSide frontSide = new FrontSide(true, "almost completely", 660);
        ComputerBank bank = new ComputerBank("disintegrating", frontSide);
        People people = new People("sitting", "tight", "end");
        Corner corner = new Corner("room corner", people);
        Chapter chapter = new Chapter(33, bombardment, env, bank, corner);
        MoltenMetalStreams streams = new MoltenMetalStreams("thick", "crawling");

        SceneNarrator narrator = new SceneNarrator(chapter, streams);
        String narrative = narrator.narrateScene();
        System.out.println(narrative);
    }
}