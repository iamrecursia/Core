package com.kozitskiy.minispring.entity;

import com.kozitskiy.minispring.annotations.Component;

@Component("dogBean")
public class Dog implements Animal{
    @Override
    public void say() {
        System.out.println("I am a dog");
    }
}
