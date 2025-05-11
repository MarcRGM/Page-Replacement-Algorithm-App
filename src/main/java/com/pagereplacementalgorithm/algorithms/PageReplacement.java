package com.pagereplacementalgorithm.algorithms;

public class PageReplacement {

    public enum algoCategories {
        FIFO("FIRST IN FIRST OUT (FIFO)", "FIFO is the most basic page replacement method. It uses a queue to monitor the order of pages loaded into memory, where the oldest page stays at the front. When replacement is needed, the page that entered first (at the front) gets removed."),
        OPTIMAL("OPTIMAL", "The Optimal algorithm replaces the page that won’t be used for the longest time in the future. It looks ahead in the reference string and chooses the page that’s not needed anytime soon, which minimizes page faults."),
        LRU("LEAST RECENTLY USED (LRU)", "LRU replaces the page that hasn’t been used for the longest time. It assumes that pages used recently will likely be needed again soon, so it removes the one that was accessed the farthest back in time.");

        private final String displayName;
        private final String info;

        algoCategories(String displayName, String info) {
            this.displayName = displayName;
            this.info = info;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getInfo() {
            return info;
        }
    }


}
