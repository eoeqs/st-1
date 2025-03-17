package org.eoeqs.function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for Secant function implementation")
public class SecantTest {

    private static final double HIGH_PRECISION = 1e-10;
    private static final double MEDIUM_PRECISION = 1e-6;

    @ParameterizedTest(name = "sec({0}) should approximate {1} with precision {2}")
    @DisplayName("Test basic secant values")
    @MethodSource("provideBasicTestCases")
    public void testBasicCases(double x, double expected, double precision) {
        assertEquals(expected, Secant.sec(x), precision);
    }

    @ParameterizedTest(name = "sec({0}) should throw IllegalArgumentException")
    @DisplayName("Test invalid arguments causing exceptions")
    @MethodSource("provideInvalidArguments")
    public void testInvalidArguments(double x) {
        assertThrows(
                IllegalArgumentException.class,
                () -> Secant.sec(x),
                "x = " + x + " should throw exception"
        );
    }

    @ParameterizedTest(name = "sec({0}) < sec({1}) in increasing interval")
    @DisplayName("Test monotonicity in (0, pi/2)")
    @CsvSource({"0.1, 0.2", "0.5, 0.6", "1.0, 1.1"})
    public void testMonotonicity(double x1, double x2) {
        assertTrue(
                Secant.sec(x2) > Secant.sec(x1),
                "Function should be increasing in (0, pi/2)"
        );
    }

    @ParameterizedTest(name = "sec({0}) = sec(-{0})")
    @DisplayName("Test even function property")
    @ValueSource(doubles = {0.1, 0.5, 1.0})
    public void testEvenFunction(double x) {
        assertEquals(
                Secant.sec(x),
                Secant.sec(-x),
                HIGH_PRECISION,
                "sec(x) should equal sec(-x)"
        );
    }

    @Test
    @DisplayName("Test computation of Euler numbers")
    public void testEulerNumbers() {
        assertEquals(BigInteger.ONE, Secant.computeEulerNumber(0));
        assertEquals(BigInteger.ONE, Secant.computeEulerNumber(1));
        assertEquals(BigInteger.valueOf(5), Secant.computeEulerNumber(2));
    }

    @ParameterizedTest(name = "sec({0}) near discontinuity should not throw")
    @DisplayName("Test behavior near discontinuities")
    @ValueSource(doubles = {
            Math.PI / 2 - 0.0999,
            -Math.PI / 2 + 0.0999,
            3 * Math.PI / 2 - 0.0999
    })
    public void testNearDiscontinuity(double x) {
        assertDoesNotThrow(
                () -> Secant.sec(x),
                "x = " + x + " should be valid"
        );
    }

    @ParameterizedTest(name = "sec({0}) should equal sec({0} + 2kπ) with precision {2}")
    @DisplayName("Test periodicity of secant function")
    @MethodSource("providePeriodicityTestCases")
    public void testPeriodicity(double x, double expected, double precision) {
        double period1 = x + 2 * Math.PI;
        double period2 = x + 4 * Math.PI;
        double secX = Secant.sec(x);
        assertEquals(expected, secX, precision, "sec(" + x + ") should match expected value");
        assertEquals(secX, Secant.sec(period1), precision, "sec(" + x + ") should equal sec(" + period1 + ")");
        assertEquals(secX, Secant.sec(period2), precision, "sec(" + x + ") should equal sec(" + period2 + ")");
    }


    private static Stream<Arguments> providePeriodicityTestCases() {
        return Stream.of(
                Arguments.of(0, 1.0, HIGH_PRECISION),
                Arguments.of(Math.PI / 6, 1.15470053838, HIGH_PRECISION),
                Arguments.of(5 * Math.PI / 6, -1.15470053838, HIGH_PRECISION),
                Arguments.of(7 * Math.PI / 4, 1.41421356, MEDIUM_PRECISION)
        );
    }

    private static Stream<Arguments> provideBasicTestCases() {
        return Stream.of(
                Arguments.of(0, 1.0, HIGH_PRECISION),
                Arguments.of(Math.PI / 6, 1.15470053838, HIGH_PRECISION),
                Arguments.of(-Math.PI / 6, 1.15470053838, HIGH_PRECISION),
                Arguments.of(0.1, 1.005020918400455, HIGH_PRECISION),
                Arguments.of(Math.PI / 4, 1.41421356237, MEDIUM_PRECISION),
                Arguments.of(2 * Math.PI - 0.1, 1.005020918400455, HIGH_PRECISION),
                Arguments.of(-2 * Math.PI + 0.1, 1.005020918400455, HIGH_PRECISION),
                Arguments.of(5 * Math.PI / 6, -1.15470053838, HIGH_PRECISION),
                Arguments.of(7 * Math.PI / 6, -1.15470053838, HIGH_PRECISION),
                Arguments.of(7 * Math.PI / 4, 1.41421356, MEDIUM_PRECISION)
        );
    }

    private static Stream<Arguments> provideInvalidArguments() {
        return Stream.of(
                Arguments.of(Math.PI / 2),
                Arguments.of(-Math.PI / 2),
                Arguments.of(3 * Math.PI / 2),
                Arguments.of(-3 * Math.PI / 2),
                Arguments.of(5 * Math.PI / 2),
                Arguments.of(-5 * Math.PI / 2)
        );
    }
}