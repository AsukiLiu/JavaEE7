package org.asuki.common.javase.model;

import org.asuki.common.javase.annotation.HintsInherited;

@HintsInherited
public class Super {
    private int superPrivate;

    public int superPublic;

    public Super() {
    }

    private int superPrivate() {
        return superPrivate;
    }

    public int superPublice() {
        return superPrivate();
    }
}
