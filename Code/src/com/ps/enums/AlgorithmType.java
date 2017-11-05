package com.ps.enums;

public enum AlgorithmType {
    LocalSearch1,
    LocalSearch2;

    @Override
    public String toString() {
        switch(this) {
            case LocalSearch1: return "LS1";
            case LocalSearch2: return "LS2";
        }
    }
}
