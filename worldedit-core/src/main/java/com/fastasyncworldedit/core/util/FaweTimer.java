package com.fastasyncworldedit.core.util;

import java.util.Arrays;

public class FaweTimer implements Runnable {

    private final double[] history = new double[]{20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d, 20d};
    private int historyIndex;
    private long lastPoll = System.currentTimeMillis();
    private long tickStart = System.currentTimeMillis();
    private final long tickInterval = 5;
    private long tick;
    private long tickMod;

    @Override
    public void run() {
        tickStart = System.currentTimeMillis();
        tick++;
        if (++tickMod == tickInterval) {
            tickMod = 0;
        } else {
            return;
        }
        long timeSpent = tickStart - lastPoll;
        if (timeSpent == 0) {
            timeSpent = 1;
        }
        double millisPer20Interval = tickInterval * 50 * 20;
        double tps = millisPer20Interval / timeSpent;
        history[historyIndex++] = tps;
        if (historyIndex >= history.length) {
            historyIndex = 0;
        }
        lastPoll = tickStart;
    }

    private long lastGetTPSTick;
    private double lastGetTPSValue = 20d;

    public double getTPS() {
        if (tick < lastGetTPSTick + tickInterval) {
            return lastGetTPSValue;
        }
        double total = Arrays.stream(history).sum();
        lastGetTPSValue = total / history.length;
        lastGetTPSTick = tick;
        return lastGetTPSValue;
    }

    public long getTick() {
        return tick;
    }

    public long getTickMillis() {
        return System.currentTimeMillis() - tickStart;
    }

    public long getTickStart() {
        return tickStart;
    }

    private long skip = 0;
    private long skipTick = 0;

    public boolean isAbove(double tps) {
        if (tps <= 0) {
            return true;
        }
        if (skip > 0) {
            if (skipTick != tick) {
                skip--;
                skipTick = tick;
                return true; // Run once per tick
            }
            return false;
        }
        if (getTickMillis() > 100 || getTPS() < tps) {
            skip = 10;
            skipTick = tick;
        }
        return true;
    }

    private boolean runIfAbove(Runnable run, double tps) {
        if (isAbove(tps)) {
            run.run();
            return true;
        }
        return false;
    }

}
