package org.asuki.common.javase;

import org.asuki.common.javase.model.Person;

interface PersonFactory<P extends Person> {
    P create(String name);
}
