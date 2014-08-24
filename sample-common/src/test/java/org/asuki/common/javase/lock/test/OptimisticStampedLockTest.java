package org.asuki.common.javase.lock.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.asuki.common.javase.lock.OptimisticStampedLockSpaceship;
import org.testng.annotations.Test;

public class OptimisticStampedLockTest extends AbstractLockTest {

    {
        spaceship = new OptimisticStampedLockSpaceship();
    }

    @Test(dependsOnMethods = "testThreads")
    public void testResult() {
        assertThat(spaceship.toString(), containsString(expected));
    }

}
