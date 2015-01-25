package org.asuki.common.javase;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.ToString;

import org.testng.annotations.Test;

import com.google.common.collect.Lists;

public class ComparatorTest {

    @Test
    public void testCompare() {

        // @formatter:off

        List<ComparableTarget> list = Lists.newArrayList(
                new ComparableTarget(22), 
                new ComparableTarget(31), 
                new ComparableTarget(21),
                new ComparableTarget(12), 
                new ComparableTarget(19)
        );

        // Faster
        List<ComparableTarget> sortedList1 = list.stream()
                .sorted(CustomComparator.INSTANCE)
                .collect(Collectors.toList());

        List<ComparableTarget> sortedList2 = list
                .stream()
                .sorted(Comparator.comparing(ComparableTarget::getA)
                        .thenComparing(ComparableTarget::getB)
                        .thenComparing(ComparableTarget::getC))
                .collect(Collectors.toList());

        List<ComparableTarget> sortedList3 = list
                .stream()
                .sorted(Comparator.comparingInt(ComparableTarget::getA)
                        .thenComparingInt(ComparableTarget::getB)
                        .thenComparingInt(ComparableTarget::getC))
                .collect(Collectors.toList());

        // @formatter:on

        assertThat(sortedList1.toString(), is(sortedList2.toString()));
        assertThat(sortedList1.toString(), is(sortedList3.toString()));
    }
}

enum CustomComparator implements Comparator<ComparableTarget> {
    INSTANCE;

    @Override
    public int compare(ComparableTarget o1, ComparableTarget o2) {
        int comp = Integer.compare(o1.getA(), o2.getA());
        if (comp == 0) {
            comp = Integer.compare(o1.getB(), o2.getB());
            if (comp == 0) {
                comp = Integer.compare(o1.getC(), o2.getC());
            }
        }
        return comp;
    }
}

@ToString
class ComparableTarget {
    @Getter
    private int a, b, c;

    public ComparableTarget(int i) {
        a = i % 2;
        b = i % 10;
        c = i;
    }
}
