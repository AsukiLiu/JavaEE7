package org.asuki.common.javase.model;

import org.asuki.common.javase.annotation.Hints;

@Hints
public class Sub extends Super {
    private int subPrivate;

    public int subPublic;

    private Sub() {
    }

    public Sub(int i) {
        this();
    }

    private int subPrivate() {
        return subPrivate;
    }

    public int subPublice() {
        return subPrivate();
    }
}
