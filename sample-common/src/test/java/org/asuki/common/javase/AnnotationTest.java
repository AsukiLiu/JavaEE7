package org.asuki.common.javase;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.asuki.common.javase.annotation.Hint;
import org.asuki.common.javase.annotation.Hints;
import org.testng.annotations.Test;

public class AnnotationTest {

    @Test
    public void testRepeatable() {
        Hint hint = Person.class.getAnnotation(Hint.class);
        assertThat(hint, is(nullValue()));

        Hints hints = Person.class.getAnnotation(Hints.class);
        assertThat(hints.value().length, is(2));

        Hint[] hintHint = Person.class.getAnnotationsByType(Hint.class);
        assertThat(hintHint.length, is(2));
    }

}
