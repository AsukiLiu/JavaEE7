package org.asuki.common.util;

import javax.annotation.Nullable;

@FunctionalInterface
public interface Function<F, T, E extends Exception> {
    @Nullable
    T apply(@Nullable F input) throws E;
}
