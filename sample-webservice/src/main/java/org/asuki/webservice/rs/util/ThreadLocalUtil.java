package org.asuki.webservice.rs.util;

import static com.google.common.base.Objects.firstNonNull;

import java.util.Locale;

public class ThreadLocalUtil {

    public static final ThreadLocal<Locale> THREAD_LOCAL = new ThreadLocal<>();

    public static Locale get() {
        return firstNonNull(THREAD_LOCAL.get(), Locale.getDefault());
    }

    public static void set(Locale locale) {
        THREAD_LOCAL.set(locale);
    }

    public static void unset() {
        THREAD_LOCAL.remove();
    }
}
