package com.kozitskiy.linkedlist;

public class LinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int size = 0;

    public int size(){
        return size;
    }

    public void addFirst(T data){
        Node<T> node = new Node<>(data);
        node.next = head;
        head = node;
        size++;
    }

    public void addLast(T data) {
        Node<T> node = new Node<>(data);
        if (head == null){
            head = node;
        }
        else{
            Node<T> current = head;
            while(current.next != null){
                current = current.next;
            }
            current.next = node;
        }
        size++;
    }

    public void add(int index, T data){
        if (index < 0 || index > size){
            throw new IndexOutOfBoundsException("Index: " + index + "Size: " + size);
        }

        if(index == 0){
            addFirst(data);
            return;
        }

        if(index == size){
            addLast(data);
            return;
        }

        Node<T> current = head;
        for(int i = 0; i < index - 1; i++){
            current = current.next;
        }

        Node<T> newNode = new Node<>(data);
        newNode.next = current.next;
        current.next = newNode;
        size++;

    }

    public T getFirst(){
        if(head == null){
            throw new IllegalStateException("List is empty");
        }
        return head.data;
    }

    public T getLast(){
        if(head == null){
            throw new IllegalStateException("List is empty");
        }
        Node<T> current = head;
        while (current.next != null){
            current = current.next;
        }
        return current.data;
    }

    public T get(int index){
       if(head == null){
           throw new IndexOutOfBoundsException("List is empty");
       }
       if(index < 0 || index >= size){
           throw new IndexOutOfBoundsException("Index: " + index + "Size: " + size);
       }

       Node<T> current = head;
       for(int i = 0; i < index; i++){
           current = current.next;
       }
       return current.data;
    }

    public void removeFirst(){
        if(head == null){
            throw new IllegalStateException("List is empty");
        }
        head = head.next;
        size--;
    }

    public void removeLast(){
        if(head == null){
            throw new IllegalStateException("List is empty");
        }
        if(head.next == null){
            throw new IllegalStateException("List has one element");
        }else {
            Node<T> current = head;
            while (current.next.next != null){
                current = current.next;
            }
            current.next = null;
        }
        size--;

    }

    public void remove(int index){
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException("Index: " + index + "Size: " + size);
        }

        if(index == 0){
            removeFirst();
            return;
        }

        if(index == size - 1){
            removeLast();
            return;
        }

        Node<T> current = head;
        for(int i = 0; i < index - 1; i++){
            current = current.next;
        }
        current.next = current.next.next;
        size--;
    }

}
