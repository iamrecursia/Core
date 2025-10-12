package com.kozitskiy.skynet.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Factory {
    private final List<RobotPart> parts = new ArrayList<>();
    private final Random random = new Random();
    private final Object lock = new Object();

    public void produceParts(){
        synchronized (lock){

            int count = random.nextInt(10) + 1;
            for (int i = 0; i < count; i++){
                RobotPart part = RobotPart.values()[random.nextInt(RobotPart.values().length)];
                parts.add(part);
            }
        }
    }

    public List<RobotPart> getAvailableRobotParts(){
        return new ArrayList<>(parts);
    }

    public boolean takePartByFaction(RobotPart part){
        synchronized (lock){
            if (parts.contains(part)){
                parts.remove(part);
                return true;
            }
            return false;
        }
    }
}
