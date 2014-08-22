package org.asuki.common.javase.lock;

import java.util.concurrent.atomic.LongAdder;

import com.google.common.base.Objects;

public class AdderSpaceship implements Spaceship {

    private final LongAdder x = new LongAdder();
    private final LongAdder y = new LongAdder();

    @Override
    public int read(int[] coordinates) {
        coordinates[0] = x.intValue();
        coordinates[1] = y.intValue();

        return 1;
    }

    @Override
    public int write(int xDelta, int yDelta) {
        x.add(xDelta);
        y.add(yDelta);

        return 1;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("x", this.x.intValue())
                .add("y", this.y.intValue()).toString();
    }
}
