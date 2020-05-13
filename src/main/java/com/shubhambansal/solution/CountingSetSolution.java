package com.shubhambansal.solution;

import com.shubhambansal.solution.countingSet.CountingSet;
import com.shubhambansal.solution.countingSet.CountingSetImpl;

public class CountingSetSolution {
    public static void main(String[] args) {
        CountingSet countingSet = new CountingSetImpl();
        System.out.println(countingSet.count("a"));
    }
}
