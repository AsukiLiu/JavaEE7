package org.asuki.common.javase.reflection;

public interface TargetInterface {
    String publicMethod(Number num);

    default String defaultMethod() {
        return "default method";
    };
}
