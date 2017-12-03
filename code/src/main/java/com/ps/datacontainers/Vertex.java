package com.ps.datacontainers;

/*
This class represents a vertex on the graph. It is used to store important properties of a vertex.
*/

public class Vertex {
    protected int id;
    protected int score;

    public Vertex(int id) {
        this.id = id;
        this.score = Integer.MIN_VALUE;
    }

    public Vertex(int id, int score) {
        this.id = id;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public int hashCode() {
        return this.id;
    }

    @Override
    public boolean equals(Object other) {
        if (getClass() == other.getClass() && this.id == ((Vertex)other).getId())
            return true;
        if (this == other)
            return true;
        return false;
    }
}
