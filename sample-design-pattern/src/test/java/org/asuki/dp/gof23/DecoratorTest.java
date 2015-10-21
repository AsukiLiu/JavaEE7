package org.asuki.dp.gof23;

import org.asuki.dp.gof23.Decorator.Animal;
import org.asuki.dp.gof23.Decorator.Rat;
import org.asuki.dp.gof23.Decorator.AnimalDecorator;
import org.asuki.dp.gof23.Decorator.FlyFeature;
import org.asuki.dp.gof23.Decorator.DigFeature;
import org.asuki.dp.gof23.Decorator.BaseDecorator;
import org.asuki.dp.gof23.Decorator.Component;
import org.asuki.dp.gof23.Decorator.OriginalComponent;
import org.asuki.dp.gof23.Decorator.ConcreteDecoratorA;
import org.asuki.dp.gof23.Decorator.ConcreteDecoratorB;
import org.testng.annotations.Test;

public class DecoratorTest {

    @Test
    public void testNormalVersion() {
        Component com = new OriginalComponent();
        BaseDecorator decoratorA = new ConcreteDecoratorA();
        BaseDecorator decoratorB = new ConcreteDecoratorB();

        decoratorA.setCom(com);
        decoratorB.setCom(decoratorA);

        decoratorB.operation();

        // OR

        com = new ConcreteDecoratorA(new ConcreteDecoratorB(
                new OriginalComponent()));

        com.operation();
    }

    @Test
    public void testReflectionVersion() {
        Animal animal = new Rat();
        animal = new AnimalDecorator(animal, FlyFeature.class);
        animal = new AnimalDecorator(animal, DigFeature.class);
        animal.act();
    }
}
