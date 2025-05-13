package com.pagereplacementalgorithm.algorithms;

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
        List<PageResult> results = new ArrayList<>(); // collects each result and whether there's a fault
        List<Integer> frames = new ArrayList<>(Collections.nCopies(frameCount, -1));
        // Start with empty frames (-1 = empty slots)

        // Iterate over the reference string
        for (int i = 0; i < referenceString.length; i++) {
            int page = referenceString[i];
            boolean isFault = false;

            if (!frames.contains(page)) {  // Page fault checking
                isFault = true;

                if (frames.contains(-1)) {
                    // add the page in the first empty slot
                    frames.set(frames.indexOf(-1), page);
                } else {
                    // Replace page using Optimal
                    int pageToReplace = getOptimalPageToReplace(frames, referenceString, i); // Finds the page that has the least usage
                    int indexToReplace = frames.indexOf(pageToReplace); // Gets the index of the page to be replaced
                    frames.set(indexToReplace, page);  // Replace the page at the same index
                }
            }

            // Add the result of this step
            results.add(new PageResult(new ArrayList<>(frames), isFault));
        }

        return results;
    }

    // Determines the optimal page to replace
    private static int getOptimalPageToReplace(List<Integer> frames, int[] referenceString, int currentIndex) {
        int farthestUse = -1;  // The farthest used page in the future
        int pageToReplace = -1;

        // Go through each page in frames and find the one that will be used the farthest
        for (int page : frames) {
            if (page == -1) continue;  // Skip empty slots

            int nextUse = getNextUse(referenceString, currentIndex, page);

            // Return the page if it's not used again
            if (nextUse == -1) {
                return page;
            }

            // Find the page with the farthest future use
            if (nextUse > farthestUse) {
                farthestUse = nextUse; // Current farthest page used
                pageToReplace = page; // Current page that is optimal to replace
            }
        }

        return pageToReplace;
    }

    // Finds the next use of a page from a certain index in the reference string
    private static int getNextUse(int[] referenceString, int currentIndex, int page) {
        // Check the next occurrence of the page
        for (int i = currentIndex + 1; i < referenceString.length; i++) {
            if (referenceString[i] == page) {
                return i;  // Return the index of the next occurrence
            }
        }
        return -1;  // return -1 if page is not used
    }

}
