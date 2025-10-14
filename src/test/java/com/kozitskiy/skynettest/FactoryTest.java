package com.kozitskiy.skynettest;

import com.kozitskiy.skynet.entity.Factory;
import com.kozitskiy.skynet.entity.RobotPart;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FactoryTest {
    @Test
    void shouldProduceBetween1And10Parts(){
        Factory factory = new Factory();
        factory.produceParts();

        int total = factory.getAvailableRobotParts().size();
        assertTrue( total >= 1 && total <= 10);
    }
}
