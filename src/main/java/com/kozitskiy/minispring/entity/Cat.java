package com.kozitskiy.minispring.entity;

import com.kozitskiy.minispring.annotations.Component;

@Component("catBean")
public class Cat implements Animal{
    @Override
    public void say() {
        System.out.println("I am a cat");
    }
}
