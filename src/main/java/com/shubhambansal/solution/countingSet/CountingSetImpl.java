package com.shubhambansal.solution.countingSet;

/*
 * @author Shubham Bansal
 */

import com.shubhambansal.solution.constants.Operation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class CountingSetImpl implements CountingSet {
    public static boolean isDebugModeEnabled;
    private final ConcurrentHashMap<String, Integer> countingSet;
    private final Logger logger = Logger.getLogger("CountingSetLogger");

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
        int count = countingSet.merge(s, 1, Integer::sum);
        // To debug a scenario when get is perform as a thread unsafe operation
        // Uncomment this to test
        //        if (isDebugModeEnabled) {
        //            try {
        //                Thread.sleep(100);
        //            } catch (InterruptedException e) {
        //                e.printStackTrace();
        //            }
        //            count = countingSet.get(s);
        //
        //        }

        if (isDebugModeEnabled)
            logger.info(Operation.ADD + " : " + Thread.currentThread().getName() + " : " + count);
        return count;
    }

    @Override
    public int remove(String s) {
        //If the value is {0,1,null}, we need to return 0, else the value - 1
        int count = countingSet.compute(s, (key, value) -> value == null || value <= 1 ? 0 : value - 1);
        if (isDebugModeEnabled)
            logger.info(Operation.REMOVE + " : " + Thread.currentThread().getName() + " : " + count);
        return count;
    }

    @Override
    public int count(String s) {
        return countingSet.getOrDefault(s, 0);
    }
}
