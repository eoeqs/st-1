package org.eoeqs.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain model tests")
public class DomainModelTest {

    @ParameterizedTest(name = "The temperature calculation for intensity {0} and duration {1} should be {2}")
    @DisplayName("Bombardment temperature calculation test")
    @CsvSource({
            "true, high, 10, 1100",
            "true, low, 5, 550",
            "true, high, 0, 1000",
            "true, low, 15, 650"
    })
    public void testBombardmentTemperatureCalculation(boolean isActive, String intensity, int duration, double expectedTemp) {
        Bombardment bombardment = new Bombardment(isActive, intensity, duration);
        assertEquals(expectedTemp, bombardment.calculateTemperature(), 0.01);
    }

    @ParameterizedTest(name = "Updating the ambient temperature to {2} at intensity {0} and duration {1}")
    @DisplayName("Ambient temperature update test")
    @CsvSource({
            "high, 20, 1200",
            "high, 10, 1100",
            "low, 5, 550",
            "high, 0, 1000"
    })
    public void testEnvironmentHeatUpdate(String intensity, int duration, double expectedHeat) {
        Bombardment bombardment = new Bombardment(true, intensity, duration);
        Environment env = new Environment(0, "unimaginable");
        env.updateHeat(bombardment);
        assertEquals(expectedHeat, env.getHeat(), 0.01);
    }

    @ParameterizedTest(name = "The front side does not melt at a temperature {0} below the melting point {1}")
    @DisplayName("Test the condition of the front side below the melting point")
    @CsvSource({
            "500, 660, false, none",
            "600, 660, false, none",
            "0, 660, false, none"
    })
    public void testFrontSideMeltingBelowMeltingPoint(double temperature, double meltingPoint,
                                                      boolean expectedMelted, String expectedDegree) {
        FrontSide frontSide = new FrontSide(false, "none", meltingPoint);
        frontSide.updateMeltingState(temperature);
        assertEquals(expectedMelted, frontSide.isMelted());
        assertEquals(expectedDegree, frontSide.getMeltedDegree());
    }

    @ParameterizedTest(name = "The front side melts at a temperature {0} above the melting point {1}")
    @DisplayName("Melting test of the front side")
    @CsvSource({
            "700, 660, true, partially",
            "800, 660, true, almost completely",
            "1000, 660, true, almost completely"
    })
    public void testFrontSideMeltingAboveMeltingPoint(double temperature, double meltingPoint,
                                                      boolean expectedMelted, String expectedDegree) {
        FrontSide frontSide = new FrontSide(false, "none", meltingPoint);
        frontSide.updateMeltingState(temperature);
        assertEquals(expectedMelted, frontSide.isMelted());
        assertEquals(expectedDegree, frontSide.getMeltedDegree());
    }

    @ParameterizedTest(name = "The state of the bank does not change at temperature {0}")
    @DisplayName("Computer bank condition test without melting")
    @CsvSource({
            "500, intact, false, none",
            "600, intact, false, none"
    })
    public void testComputerBankStateUpdateNotMelted(double heat, String expectedState,
                                                     boolean expectedMelted, String expectedDegree) {
        FrontSide frontSide = new FrontSide(false, "none", 660);
        ComputerBank bank = new ComputerBank("intact", frontSide);
        Environment env = new Environment(heat, "unimaginable");
        bank.updateState(env);
        assertEquals(expectedState, bank.getState());
        assertEquals(expectedMelted, bank.getFrontSide().isMelted());
        assertEquals(expectedDegree, bank.getFrontSide().getMeltedDegree());
    }

    @ParameterizedTest(name = "The state of the bank changes at temperature {0}")
    @DisplayName("Test of the state of the computer bank during melting")
    @CsvSource({
            "1200, disintegrating, true, almost completely",
            "700, disintegrating, true, partially"
    })
    public void testComputerBankStateUpdateMelted(double heat, String expectedState,
                                                  boolean expectedMelted, String expectedDegree) {
        FrontSide frontSide = new FrontSide(false, "none", 660);
        ComputerBank bank = new ComputerBank("intact", frontSide);
        Environment env = new Environment(heat, "unimaginable");
        bank.updateState(env);
        assertEquals(expectedState, bank.getState());
        assertEquals(expectedMelted, bank.getFrontSide().isMelted());
        assertEquals(expectedDegree, bank.getFrontSide().getMeltedDegree());
    }

    @ParameterizedTest(name = "Full scene update with duration {1}")
    @DisplayName("Full Scene Update Test")
    @MethodSource("provideChapterSceneData")
    public void testChapterFullSceneUpdate(boolean isActive, int duration, double expectedHeat,
                                           String expectedState, boolean expectedMelted, String expectedDegree) {
        Bombardment bombardment = new Bombardment(isActive, "high", duration);
        Environment env = new Environment(0, "unimaginable");
        FrontSide frontSide = new FrontSide(false, "none", 660);
        ComputerBank bank = new ComputerBank("intact", frontSide);
        People people = new People("sitting", "tight", "end");
        Corner corner = new Corner("room corner", people);
        Chapter chapter = new Chapter(33, bombardment, env, bank, corner);

        chapter.updateScene();

        assertEquals(expectedHeat, chapter.getEnvironment().getHeat(), 0.01);
        assertEquals(expectedState, chapter.getComputerBank().getState());
        assertEquals(expectedMelted, chapter.getComputerBank().getFrontSide().isMelted());
        assertEquals(expectedDegree, chapter.getComputerBank().getFrontSide().getMeltedDegree());
    }

    private static Stream<Object[]> provideChapterSceneData() {
        return Stream.of(
                new Object[]{true, 30, 1300, "disintegrating", true, "almost completely"},
                new Object[]{true, 10, 1100, "disintegrating", true, "almost completely"},
                new Object[]{true, 0, 1000, "disintegrating", true, "almost completely"},
                new Object[]{false, 10, 0, "intact", false, "none"}
        );
    }

    @ParameterizedTest(name = "Inactive bombardment with duration {0}")
    @DisplayName("Inactive Bombardment Scene Test")
    @CsvSource({
            "10, 0, intact, false, none",
            "5, 0, intact, false, none"
    })
    public void testInactiveBombardmentNoChange(int duration, double expectedHeat, String expectedState,
                                                boolean expectedMelted, String expectedDegree) {
        Bombardment bombardment = new Bombardment(false, "high", duration);
        Environment env = new Environment(0, "unimaginable");
        FrontSide frontSide = new FrontSide(false, "none", 660);
        ComputerBank bank = new ComputerBank("intact", frontSide);
        People people = new People("sitting", "tight", "end");
        Corner corner = new Corner("room corner", people);
        Chapter chapter = new Chapter(33, bombardment, env, bank, corner);

        chapter.updateScene();

        assertEquals(expectedHeat, chapter.getEnvironment().getHeat(), 0.01);
        assertEquals(expectedState, chapter.getComputerBank().getState());
        assertEquals(expectedMelted, chapter.getComputerBank().getFrontSide().isMelted());
        assertEquals(expectedDegree, chapter.getComputerBank().getFrontSide().getMeltedDegree());
    }
}