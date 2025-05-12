package com.pagereplacementalgorithm.algorithms;

import com.pagereplacementalgorithm.PageResult;

import java.util.*;

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

    public static List<PageResult> runFIFO(int[] referenceString, int frameCount) {
        List<PageResult> results = new ArrayList<>(); // collects each result and whether there's a fault
        Queue<Integer> queue = new LinkedList<>(); // For preserving the order
        Set<Integer> set = new HashSet<>(); // For quick checking

        for (int ref : referenceString) {
            boolean isFault = false;

            if (!set.contains(ref)) { // If page is not on memory = fault
                isFault = true;
                if (queue.size() == frameCount) {
                    int removed = queue.poll(); // remove oldest page
                    set.remove(removed);
                }
                queue.add(ref); // add the new page
                set.add(ref);
            }
            // List of page frame results paired with if it's a fault
            results.add(new PageResult(new ArrayList<>(queue), isFault));
        }

        return results;
    }

    public static List<PageResult> runLRU(int[] referenceString, int frameCount) {
        List<PageResult> results = new ArrayList<>();  // collects each result and whether there's a fault
        List<Integer> frames = new ArrayList<>(); // Simulates the frame
        Map<Integer, Integer> lastUsed = new HashMap<>(); // Searching for LRU

        for (int time = 0; time < referenceString.length; time++) {
            int page = referenceString[time]; // Tracks when the page was last used
            boolean isFault;

            if (frames.contains(page)) {
                isFault = false;
            } else {
                isFault = true;
                if (frames.size() < frameCount) {
                    frames.add(page);
                } else {
                    // Find least recently used
                    int lruPage = frames.getFirst(); // start with the first page
                    int minTime = lastUsed.getOrDefault(lruPage, -1); // check how many times the current page has been used

                    // Checking which page has the minimum time used
                    for (int p : frames) {
                        int usedTime = lastUsed.getOrDefault(p, -1); // Each page getting checked
                        if (usedTime < minTime) {
                            lruPage = p;
                            minTime = usedTime;
                        }
                    }
                    // replace te lru
                    int indexToReplace = frames.indexOf(lruPage);
                    frames.set(indexToReplace, page);
                }
            }

            // Update the last used time
            lastUsed.put(page, time);

            // List of page frame results paired with if it's a fault
            results.add(new PageResult(new ArrayList<>(frames), isFault));
        }

        return results;
    }

    public static List<PageResult> runOptimal(int[] referenceString, int frameCount) {
        List<PageResult> results = new ArrayList<>();
        Set<Integer> set = new HashSet<>(); // For quick checking

        return results;
    }

}
