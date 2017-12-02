package com.ps.enums;

/*
This is an enum used in the code to determine which algorithm to run.
 */

public enum AlgorithmType {
    LocalSearch1,
    LocalSearch2;

    @Override
    public String toString() {
        String out = "invalid";

        switch(this) {
            case LocalSearch1: out = "LS1"; break;
            case LocalSearch2: out = "LS2"; break;
        }

        return out;
    }
}
