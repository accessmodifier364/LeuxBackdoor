package me.accessmodifier364.leuxbackdoor.client.util;

public class Pair2<F, S> {
    private F first;

    private S second;

    public Pair2(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public F getFirst() {
        return this.first;
    }

    public S getSecond() {
        return this.second;
    }
}
