package com.gameskraft.shubhambansal;

import com.gameskraft.shubhambansal.countingSet.CountingSet;
import com.gameskraft.shubhambansal.countingSet.CountingSetImpl;

public class GameskraftSolution {
    public static void main(String[] args) {
        CountingSet countingSet = new CountingSetImpl();
        System.out.println(countingSet.count("a"));
    }
}
