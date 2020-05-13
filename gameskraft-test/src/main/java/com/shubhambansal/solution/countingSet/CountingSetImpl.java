package com.shubhambansal.solution.countingSet;

/*
 * @author Shubham Bansal
 */

import com.shubhambansal.solution.constants.Operation;
import java.util.concurrent.ConcurrentHashMap;

public class CountingSetImpl implements CountingSet {
    public static boolean isDebugModeEnabled;
    private final ConcurrentHashMap<String, Integer> countingSet;

    public CountingSetImpl() {
        countingSet = new ConcurrentHashMap<>();
    }

    CountingSetImpl(int intialCapacity) {
        countingSet = new ConcurrentHashMap<>(intialCapacity);
    }

    CountingSetImpl(boolean debug) {
        isDebugModeEnabled = debug;
        countingSet = new ConcurrentHashMap<>();
    }

    @Override
    public int add(String s) {
        countingSet.merge(s, 1, Integer::sum);
        Integer count = countingSet.get(s);
        if (isDebugModeEnabled)
            System.out.println(Operation.ADD + " : " + Thread.currentThread().getName() + " : " + count);
        return count;
    }

    @Override
    public int remove(String s) {
        countingSet.compute("a", (key, value) -> value==null||value == 1? null : value - 1);
        int count = countingSet.getOrDefault(s, 0);
        if (isDebugModeEnabled)
            System.out.println(Operation.REMOVE + " : " + Thread.currentThread().getName() + " : " + count);
        return count;
    }

    @Override
    public int count(String s) {
        return countingSet.getOrDefault(s, 0);
    }
}
