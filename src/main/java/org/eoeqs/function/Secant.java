package org.eoeqs.function;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class Secant {

    private static final List<BigInteger> eulerNumbers = new ArrayList<>();
    private static final List<BigInteger> factorialCache = new ArrayList<>();

    static {
        eulerNumbers.add(BigInteger.ONE);
        factorialCache.add(BigInteger.ONE);
    }

    public static double sec(double x) {
        x = normalizeAngle(x);

        if (Math.abs(x) >= Math.PI / 2) {
            throw new IllegalArgumentException("x is too close to discontinuity point");
        }

        double sum = 0.0;
        double xSquared = x * x;
        double xPower = 1.0;
        int n = 0;
        final double epsilon = 1e-16;
        double prevSum;

        do {
            prevSum = sum;
            BigInteger eulerBI = computeEulerNumber(n);
            BigInteger factorialBI = getFactorial(2 * n);

            if (eulerBI.bitLength() > 62 || factorialBI.bitLength() > 62) {
                break;
            }

            double coefficient = eulerBI.doubleValue() / factorialBI.doubleValue();
            double term = coefficient * xPower;

            if (Double.isInfinite(term) || Double.isNaN(term)) {
                break;
            }

            sum += term;
            n++;
            xPower *= xSquared;
        } while (n < 1_000_000 && Math.abs(sum - prevSum) > epsilon);

        return sum;
    }

    private static double normalizeAngle(double x) {
        x %= Math.PI;
        if (x > Math.PI/2) {
            x -= Math.PI;
        } else if (x < -Math.PI/2) {
            x += Math.PI;
        }
        return x;
    }

    static BigInteger computeEulerNumber(int n) {
        if (n < eulerNumbers.size()) {
            return eulerNumbers.get(n);
        }

        for (int i = eulerNumbers.size(); i <= n; i++) {
            BigInteger sum = BigInteger.ZERO;
            for (int k = 0; k < i; k++) {
                BigInteger c = combination(2 * i, 2 * k);
                BigInteger e = eulerNumbers.get(k);
                if ((i - k - 1) % 2 == 0) {
                    sum = sum.add(c.multiply(e));
                } else {
                    sum = sum.subtract(c.multiply(e));
                }
            }
            eulerNumbers.add(sum);
        }

        return eulerNumbers.get(n);
    }

    private static BigInteger combination(int m, int k) {
        if (k < 0 || k > m) return BigInteger.ZERO;
        if (k == 0 || k == m) return BigInteger.ONE;
        k = Math.min(k, m - k);
        BigInteger result = BigInteger.ONE;
        for (int i = 1; i <= k; i++) {
            result = result.multiply(BigInteger.valueOf(m - k + i));
            result = result.divide(BigInteger.valueOf(i));
        }
        return result;
    }

    private static BigInteger getFactorial(int m) {
        if (m < factorialCache.size()) {
            return factorialCache.get(m);
        }
        BigInteger last = factorialCache.get(factorialCache.size() - 1);
        for (int i = factorialCache.size(); i <= m; i++) {
            last = last.multiply(BigInteger.valueOf(i));
            factorialCache.add(last);
        }
        return last;
    }
}