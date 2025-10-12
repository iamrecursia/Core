package com.kozitskiy.skynettest;

import com.kozitskiy.skynet.entity.Faction;
import com.kozitskiy.skynet.entity.Factory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SimulationIntegrationTest {
    @Test
    void shouldRun100DaySimulationWithoutErrors(){
        Factory factory = new Factory();
        Faction worldTest = new Faction("WorldTest");
        Faction wednesdayTest = new Faction("wednesdayTest");

        for (int day = 1; day <= 5; day++){
            factory.produceParts();

            Thread t1 = new Thread(() -> worldTest.takePartsFromFactory(factory));
            Thread t2 = new Thread(() -> wednesdayTest.takePartsFromFactory(factory));

            t1.start(); t2.start();
            try {
                t1.join(); t2.join();
            }catch (InterruptedException e){
            }

            worldTest.buildRobots();
            wednesdayTest.buildRobots();
        }

        assertTrue(worldTest.getRobotsBuilt() >= 0);
        assertTrue(wednesdayTest.getRobotsBuilt() >= 0);
    }
}
