package org.asuki.common.javase.lock.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

import org.asuki.common.javase.lock.AtomicSpaceship;
import org.testng.annotations.Test;

public class AtomicTest extends AbstractLockTest {

    {
        spaceship = new AtomicSpaceship();
    }

    @Test(dependsOnMethods = "testThreads")
    public void testResult() {
        assertThat(spaceship.toString(), containsString(expected));
    }
}
