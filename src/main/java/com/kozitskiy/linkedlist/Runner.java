package com.kozitskiy.linkedlist;

public class Runner {
    public static void main(String[] args){

        LinkedList<Integer> myList = new LinkedList<>();
        myList.addLast(5);
        System.out.println(myList.size());
        myList.addLast(3);
        myList.addLast(4);
        System.out.println(myList.size());

        System.out.println("-----------------------");

        System.out.println(myList.get(2));

        System.out.println(myList.getFirst()+"FIRST");

        myList.removeFirst();

        System.out.println(myList.getFirst()+"FIRST");

        myList.removeFirst();

        System.out.println(myList.getFirst()+"FIRST");

        myList.removeFirst();
        System.out.println(myList.getFirst()+"FIRST");

        myList.removeFirst();


    }
}
