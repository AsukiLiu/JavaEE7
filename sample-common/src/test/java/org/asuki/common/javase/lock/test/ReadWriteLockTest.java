package org.asuki.common.javase.lock.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.asuki.common.javase.lock.ReadWriteLockSpaceShip;
import org.testng.annotations.Test;

public class ReadWriteLockTest extends AbstractLockTest {

    {
        spaceship = new ReadWriteLockSpaceShip();
    }

    @Test(dependsOnMethods = "testThreads")
    public void testResult() {
        assertThat(spaceship.toString(), containsString(expected));
    }

}