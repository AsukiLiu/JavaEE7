package org.asuki.common.javase.lock.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.asuki.common.javase.lock.SemaphoreSpaceShip;
import org.testng.annotations.Test;

public class SemaphoreSpaceShipTest extends AbstractLockTest {

    {
        spaceship = new SemaphoreSpaceShip();
    }

    @Test(dependsOnMethods = "testThreads")
    public void testResult() {
        assertThat(spaceship.toString(), containsString(expected));
    }
}
