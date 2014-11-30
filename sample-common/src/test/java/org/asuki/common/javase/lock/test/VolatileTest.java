package org.asuki.common.javase.lock.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.asuki.common.javase.lock.VolatileSpaceship;
import org.testng.annotations.Test;

public class VolatileTest extends AbstractLockTest {

    {
        spaceship = new VolatileSpaceship();
    }

    @Test(enabled = false, dependsOnMethods = "testThreads")
    public void testResult() {
        assertThat(spaceship.toString(), not(containsString(expected)));
    }
}
