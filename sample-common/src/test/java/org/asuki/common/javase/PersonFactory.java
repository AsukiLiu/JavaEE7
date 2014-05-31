package org.asuki.common.javase;

interface PersonFactory<P extends Person> {
    P create(String name);
}
