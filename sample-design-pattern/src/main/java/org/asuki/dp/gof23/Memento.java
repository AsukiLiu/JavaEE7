package org.asuki.dp.gof23;

import static org.asuki.dp.gof23.Memento.State.OFF;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Memento {

    @AllArgsConstructor
    static class Mementor {
        @Getter
        private final State state;
    }

    static class Caretaker {
        @Getter
        @Setter
        private Mementor mementor;
    }

    static class Originator {
        @Getter
        @Setter
        private State state = OFF;

        public Mementor createMementor() {
            return new Mementor(state);
        }

        public void setMementor(Mementor mementor) {
            this.state = mementor.getState();
        }
    }

    enum State {
        ON, OFF
    }

}
