package org.asuki.common.javase.lock.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.asuki.common.javase.lock.DirtySpaceship;
import org.testng.annotations.Test;

public class DirtyTest extends AbstractLockTest {

    {
        spaceship = new DirtySpaceship();
    }

    @Test(enabled = false, dependsOnMethods = "testThreads")
    public void testResult() {
        assertThat(spaceship.toString(), not(containsString(expected)));
    }
}
