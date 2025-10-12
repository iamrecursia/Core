package com.kozitskiy;

import com.kozitskiy.skynet.entity.Factory;
import com.kozitskiy.skynet.entity.Faction;

public class RobotWarSimulation
{
    public static void main( String[] args ) {
        Factory factory = new Factory();
        Faction world = new Faction("World");
        Faction wednesday = new Faction("Wednesday");

        for (int day = 1; day <= 100; day++) {
            factory.produceParts();

            Thread worldThread = new Thread(() -> {
                world.takePartsFromFactory(factory);
            }, "World-Thread");

            Thread wednesdayThread = new Thread(() -> {
                wednesday.takePartsFromFactory(factory);
            }, "Wednesday-Thread");

            worldThread.start();
            wednesdayThread.start();

            try {
                worldThread.join();
                wednesdayThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread is interrupter");
            }

            world.buildRobots();
            wednesday.buildRobots();
        }

        System.out.println("--- Results after 100 days ---");
        System.out.println(world.getName() + ": " + world.getRobotsBuilt() + " robots");
        System.out.println(wednesday.getName() + ": " + wednesday.getRobotsBuilt() + " robots");

        if (world.getRobotsBuilt() > wednesday.getRobotsBuilt()) {
            System.out.println("The Winner: " + world.getName());
        } else if (wednesday.getRobotsBuilt() > world.getRobotsBuilt()) {
            System.out.println("The Winner: " + wednesday.getName());
        } else {
            System.out.println("The draw!");
        }
    }
}
