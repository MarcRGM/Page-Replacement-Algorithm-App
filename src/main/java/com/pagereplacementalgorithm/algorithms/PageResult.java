package com.pagereplacementalgorithm.algorithms;

import java.util.List;

public class PageResult {

    // Page frames results paired with if it's a fault

    public final List<Integer> frameState;
    public final boolean isFault;

    public PageResult(List<Integer> frameState, boolean isFault) {
        this.frameState = frameState;
        this.isFault = isFault;
    }
}









