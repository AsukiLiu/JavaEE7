package org.asuki.dp.gof23;

import org.asuki.dp.gof23.Mediator.ConcreteColleague1;
import org.asuki.dp.gof23.Mediator.ConcreteColleague2;
import org.asuki.dp.gof23.Mediator.BossImpl;
import org.testng.annotations.Test;

public class MediatorTest {

    @Test
    public void test() {
        BossImpl boss = new BossImpl();
        ConcreteColleague1 c1 = new ConcreteColleague1(boss);
        ConcreteColleague2 c2 = new ConcreteColleague2(boss);

        boss.addColleague(c1);
        boss.addColleague(c2);

        c1.send("Hello");
        c2.send("Hola");
    }

}
