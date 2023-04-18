package com.epam.esm.criteria;

/**
 * Enum representing sort order options.
 */
public enum SortOrder {
    DESC, UNSORTED, ASC;

    /**
     * Returns true if the sort order is ascending (ASC).
     *
     * @return true if ascending, false otherwise.
     */
    public boolean isAsc() {
        return ordinal() == 2;
    }

    /**
     * Returns true if the sort order is descending (DESC).
     *
     * @return true if descending, false otherwise.
     */
    public boolean isDesc() {
        return ordinal() == 0;
    }

    /**
     * Returns true if the sort order is unsorted.
     *
     * @return true if unsorted, false otherwise.
     */
    public boolean isUnsorted() {
        return ordinal() == 1;
    }
}
