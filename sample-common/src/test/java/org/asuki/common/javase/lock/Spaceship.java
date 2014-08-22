package org.asuki.common.javase.lock;

public interface Spaceship {

    int read(int[] coordinates);

    int write(int xDelta, int yDelta);
}
