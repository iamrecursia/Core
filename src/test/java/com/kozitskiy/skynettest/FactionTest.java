package com.kozitskiy.skynettest;

import com.kozitskiy.skynet.entity.Faction;
import com.kozitskiy.skynet.entity.RobotPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FactionTest {

    @Test
    void shouldBuildOneRobotWithMinimalParts(){
        Faction faction = new Faction("Test");

        faction.getInventory().put(RobotPart.HEAD, 1);
        faction.getInventory().put(RobotPart.FEET, 2);
        faction.getInventory().put(RobotPart.TORSO, 1);
        faction.getInventory().put(RobotPart.HAND, 2);

        faction.buildRobots();

        assertEquals(1, faction.getRobotsBuilt());
        assertEquals(0, faction.getInventory().get(RobotPart.HEAD));
        assertEquals(0, faction.getInventory().get(RobotPart.HAND));
    }

    @Test
    void shouldNotBuildRobotIfMissingHands(){
        Faction faction = new Faction("Test");

        faction.getInventory().put(RobotPart.HEAD, 1);
        faction.getInventory().put(RobotPart.FEET, 2);
        faction.getInventory().put(RobotPart.TORSO, 1);
        faction.getInventory().put(RobotPart.HAND, 1);

        faction.buildRobots();

        assertEquals(0, faction.getRobotsBuilt());
    }

    @Test
    void shouldBuildTwoRobotsIfEnoughParts(){
        Faction faction = new Faction("Test");

        faction.getInventory().put(RobotPart.HEAD, 2);
        faction.getInventory().put(RobotPart.FEET, 4);
        faction.getInventory().put(RobotPart.TORSO, 2);
        faction.getInventory().put(RobotPart.HAND, 4);

        faction.buildRobots();

        assertEquals(2, faction.getRobotsBuilt());
    }
}
