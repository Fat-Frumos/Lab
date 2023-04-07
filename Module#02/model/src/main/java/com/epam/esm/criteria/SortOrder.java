package com.epam.esm.criteria;

public enum SortOrder {
    DESC, UNSORTED, ASC;

    public boolean isAsc() {
        return ordinal() == 2;
    }
    public boolean isDesc() {
        return ordinal() == 0;
    }
    public boolean isUnsorted() {
        return ordinal() == 1;
    }
}
