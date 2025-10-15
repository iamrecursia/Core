package com.kozitskiy;

import com.kozitskiy.minispring.MiniApplicationContext;
import com.kozitskiy.minispring.entity.Animal;
import com.kozitskiy.minispring.entity.Cat;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        MiniApplicationContext context = new MiniApplicationContext("com.kozitskiy.minispring");
        Animal cat = context.getBean(Cat.class);

        cat.say();
        System.out.println(cat);


    }
}
