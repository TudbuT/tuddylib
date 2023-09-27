package de.tudbut.tools;

import de.tudbut.type.FInfo;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ExtendedMath {
    public static long[] solveSimple(String eq, char toSolveChar, int maxResults) throws InterruptedException {
        eq = eq.replaceAll("=", "==").replaceAll("======", "==").replaceAll("====", "==");


        final boolean[] stop = {false};
        final long[] result = new long[maxResults];
        final char[] current = {0};

        String finalEq = eq;
        new Thread(() -> {
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
            for (long i = -1; i > Long.MIN_VALUE; i--) {
                if (stop[0])
                    break;
                try {
                    if (engine.eval("(" + finalEq.replaceAll(String.valueOf(toSolveChar), "(" + i + ")") + ")").equals(true)) {
                        if (current[0] == result.length)
                            stop[0] = true;
                        else {
                            result[current[0]] = i;
                            current[0]++;
                            if (current[0] == result.length)
                                stop[0] = true;
                        }
                        if (stop[0])
                            break;
                    }
                }
                catch (ScriptException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
        String finalEq1 = eq;
        new Thread(() -> {
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
            for (long i = 0; i < Long.MAX_VALUE; i++) {
                if (stop[0])
                    break;
                try {
                    if (engine.eval("(" + finalEq1.replaceAll(String.valueOf(toSolveChar), "(" + i + ")") + ")").equals(true)) {
                        if (current[0] == result.length)
                            stop[0] = true;
                        else {
                            result[current[0]] = i;
                            current[0]++;
                            if (current[0] == result.length)
                                stop[0] = true;
                        }
                        if (stop[0])
                            break;
                    }
                }
                catch (ScriptException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();

        while (!stop[0]) {
            Thread.sleep(0);
        }

        return result;
    }

    public static long solveSimple(String eq, char toSolveChar) throws InterruptedException {
        return solveSimple(eq, toSolveChar, 1)[0];
    }

    public static double[] solveDouble(String eq, char toSolveChar, int precision, int maxResults) throws InterruptedException {
        eq = eq.replaceAll("=", "==").replaceAll("======", "==").replaceAll("====", "==");


        final boolean[] stop = {false};
        final double[] result = new double[maxResults];
        final char[] current = {0};

        String finalEq = eq;
        new Thread(() -> {
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
            for (long i = -1; i > Long.MIN_VALUE; i--) {
                if (stop[0])
                    break;
                try {
                    if (engine.eval(finalEq.replaceAll(String.valueOf(toSolveChar), "(" + (double) i / (double) precision + ")")).equals(true)) {
                        if (current[0] == result.length)
                            stop[0] = true;
                        else {
                            result[current[0]] = (double) i / (double) precision;
                            current[0]++;
                            if (current[0] == result.length)
                                stop[0] = true;
                        }
                        if (stop[0])
                            break;
                    }
                }
                catch (ScriptException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();
        String finalEq1 = eq;
        new Thread(() -> {
            ScriptEngine engine = new ScriptEngineManager().getEngineByExtension("js");
            for (long i = 0; i < Long.MAX_VALUE; i++) {
                if (stop[0])
                    break;
                try {
                    if (engine.eval(finalEq1.replaceAll(String.valueOf(toSolveChar), "(" + (double) i / (double) precision + ")")).equals(true)) {
                        if (current[0] == result.length)
                            stop[0] = true;
                        else {
                            result[current[0]] = (double) i / (double) precision;
                            current[0]++;
                            if (current[0] == result.length)
                                stop[0] = true;
                        }
                        if (stop[0])
                            break;
                    }
                }
                catch (ScriptException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }).start();

        while (!stop[0]) {
            Thread.sleep(0);
        }

        return result;
    }


    public static double solveDouble(String eq, char toSolveChar, int precision) throws InterruptedException {
        return solveDouble(eq, toSolveChar, precision, 1)[0];
    }

    public static double solveDouble(String eq, char toSolveChar) throws InterruptedException {
        return solveDouble(eq, toSolveChar, 100, 1)[0];
    }

    public static double[] solveDouble(String eq, char toSolveChar, long maxResults) throws InterruptedException {
        return solveDouble(eq, toSolveChar, 100, (int) maxResults);
    }

    public static int random(int lower, int upper) {
        return (int) randomLong(lower, upper);
    }

    public static long randomLong(long lower, long upper) {
        upper ++;
        return (long) (Math.floor(Math.random() * (upper - lower)) + lower);
    }

    public static float randomFloat(float lower, float upper, int precision) {
        return (float) randomLong((int) (lower * (precision)), (int) (upper * (precision))) / (precision);
    }

    public static double min(double... doubles) {
        double currentMin = doubles[0];

        for (int i = 1; i < doubles.length; i++) {
            currentMin = Math.min(currentMin, doubles[i]);
        }

        return currentMin;
    }
    
    public static int min(int... ints) {
        int currentMin = ints[0];
        
        for (int i = 1; i < ints.length; i++) {
            currentMin = Math.min(currentMin, ints[i]);
        }
        
        return currentMin;
    }

    public static double max(double... doubles) {
        double currentMax = doubles[0];

        for (int i = 1; i < doubles.length; i++) {
            currentMax = Math.max(currentMax, doubles[i]);
        }

        return currentMax;
    }
    
    public static int max(int... ints) {
        int currentMax = ints[0];
        
        for (int i = 1; i < ints.length; i++) {
            currentMax = Math.max(currentMax, ints[i]);
        }
        
        return currentMax;
    }

    public static double highestMinusLowest(double d1, double d2) {
        if (d1 > d2) {
            return d1 - d2;
        }
        if (d2 > d1) {
            return d2 - d1;
        }
        return 0;
    }
    
    public static int highestMinusLowest(int i1, int i2) {
        if (i1 > i2) {
            return i1 - i2;
        }
        if (i2 > i1) {
            return i2 - i1;
        }
        return 0;
    }

    public static <T> T[] flipArray(T[] array) {
        T[] oldArray = array.clone();

        for (int i = 0; i < array.length; i++) {
            array[-i + array.length - 1] = oldArray[i];
        }

        return array;
    }
    
    public static double removeSign(double d) {
        return d < 0 ? -d : d;
    }

    @FInfo(s = "only requires ~0.5 measurable ticks (-> new Date().toInstant().getNanos()) while Math.round takes ~5")
    public static long fastRound(double d) {
        return d - (long) d < 0.5 ? (long) d : (long) d + 1;
    }

    @FInfo(s = "only requires ~0.5 measurable ticks (-> new Date().toInstant().getNanos()) while Math.round takes ~5")
    public static int fastIntRound(double d) {
        return d - (int) d < 0.5 ? (int) d : (int) d + 1;
    }
}
