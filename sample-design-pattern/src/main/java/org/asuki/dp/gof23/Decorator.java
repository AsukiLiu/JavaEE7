package org.asuki.dp.gof23;

import static java.lang.System.out;
import static java.lang.reflect.Modifier.isPublic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import lombok.AllArgsConstructor;

public class Decorator {

    /*
     * Normal version
     */

    interface Component {
        void operation();
    }

    static class OriginalComponent implements Component {
        @Override
        public void operation() {
            out.println(this.getClass().getSimpleName());
        }
    }

    static abstract class BaseDecorator implements Component {
        protected Component com;

        public BaseDecorator() {
        }

        public BaseDecorator(Component com) {
            this.com = com;
        }

        public void setCom(Component com) {
            this.com = com;
        }

        @Override
        public void operation() {
            if (com != null) {
                com.operation();
            }
        }
    }

    static class ConcreteDecoratorA extends BaseDecorator {

        public ConcreteDecoratorA() {
        }

        public ConcreteDecoratorA(Component com) {
            super(com);
        }

        @Override
        public void operation() {
            super.operation();
            out.println(this.getClass().getSimpleName());
        }
    }

    static class ConcreteDecoratorB extends BaseDecorator {

        public ConcreteDecoratorB() {
        }

        public ConcreteDecoratorB(Component com) {
            super(com);
        }

        @Override
        public void operation() {
            super.operation();
            out.println(this.getClass().getSimpleName());
        }
    }

    /*
     * Reflection version
     */

    interface Animal {
        void act();
    }

    static class Rat implements Animal {
        @Override
        public void act() {
            out.println(this.getClass().getSimpleName());
        }
    }

    interface Feature {
        void expand();
    }

    static class FlyFeature implements Feature {
        @Override
        public void expand() {
            out.println(this.getClass().getSimpleName());
        }
    }

    static class DigFeature implements Feature {
        @Override
        public void expand() {
            out.println(this.getClass().getSimpleName());
        }
    }

    @AllArgsConstructor
    static class AnimalDecorator implements Animal {
        private Animal animal;
        private Class<? extends Feature> feature;

        @Override
        public void act() {
            InvocationHandler handler = new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args)
                        throws Throwable {

                    Object obj = null;

                    if (isPublic(method.getModifiers())) {
                        obj = method.invoke(feature.newInstance(), args);
                    }

                    animal.act();

                    return obj;
                }
            };

            ClassLoader loader = getClass().getClassLoader();
            Feature proxy = (Feature) java.lang.reflect.Proxy.newProxyInstance(
                    loader, this.feature.getInterfaces(), handler);
            proxy.expand();
        }
    }
}
