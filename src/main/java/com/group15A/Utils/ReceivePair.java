package com.group15A.Utils;

/**
 * The form of the data being passed between pages
 *
 * @author Milovan Gveric
 */
public class ReceivePair {
    private final ReceiveType first;
    private final Object second;

    /**
     * Constructor for ReceivePair
     *
     * @param first the type of data to expect
     * @param second the data itself
     */
    public ReceivePair(ReceiveType first, Object second) {
        this.first = first;
        this.second = second;
    }

    public ReceiveType getFirst() {
        return first;
    }

    public Object getSecond() {
        return second;
    }
}
