package com.kozitskiy.skynet.entity;

import lombok.Data;

import java.util.*;

@Data
public class Faction {
    private final String name;
    private final Map<RobotPart, Integer> inventory = new HashMap<>();
    private int robotsBuilt = 0;

    public Faction(String name){
        this.name = name;
        for (RobotPart part : RobotPart.values()) {
            inventory.put(part, 0);
        }
    }

    public void takePartsFromFactory(Factory factory){
        int taken = 0;
        List<RobotPart> availableParts = factory.getAvailableRobotParts();

        Collections.shuffle(availableParts);

        Iterator<RobotPart> it = availableParts.iterator();
        while (it.hasNext() && taken < 5){
            RobotPart part = it.next();
            if (factory.takePartByFaction(part)){
                inventory.put(part, inventory.get(part) + 1);
                taken++;
            }
        }
    }

    public void buildRobots(){
        int maxRobots = Integer.MAX_VALUE;

        maxRobots = Math.min(maxRobots, inventory.get(RobotPart.HEAD));
        maxRobots = Math.min(maxRobots, inventory.get(RobotPart.TORSO));
        maxRobots = Math.min(maxRobots, inventory.get(RobotPart.FEET) / 2);
        maxRobots = Math.min(maxRobots, inventory.get(RobotPart.HAND) / 2);

        if (maxRobots > 0){
            inventory.put(RobotPart.HEAD, inventory.get(RobotPart.HEAD) - maxRobots);
            inventory.put(RobotPart.TORSO, inventory.get(RobotPart.TORSO) - maxRobots);
            inventory.put(RobotPart.FEET, inventory.get(RobotPart.FEET) - 2 * maxRobots);
            inventory.put(RobotPart.HAND, inventory.get(RobotPart.HAND) - 2 * maxRobots);
            robotsBuilt += maxRobots;
        }
    }
}
