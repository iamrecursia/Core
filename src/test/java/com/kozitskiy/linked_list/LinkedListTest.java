package com.kozitskiy.linked_list;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Custom LinkedList Tests")
public class LinkedListTest {

    private LinkedList<String> list;

    @BeforeEach
    void setUp(){
        list = new LinkedList<>();
    }

    @Test
    @DisplayName("size() returns 0 for empty list")
    void sizeReturnsZeroWhenEmpty(){
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("size() returns correct size after adding elements")
    void sizeReturnsCorrectAfterAdding(){
        list.addFirst("4");
        list.addLast("5");

        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("size() returns correct size after removing elements")
    void sizeReturnsCorrectAfterRemoving(){
        list.addFirst("4");
        list.addLast("5");
        list.removeFirst();

        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("addFirst() adds element to the beginning")
    void addFirstAddsToBeginning(){
        list.addFirst("2");
        list.addFirst("Kirill");
        assertEquals("Kirill", list.getFirst());
        assertEquals("2", list.getLast());
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("addLast() adds element to the end")
    void addLastAddsToEnd(){
        list.addFirst("Ivan");
        list.addFirst("Kirill");
        list.addLast("Nikita");
        assertEquals("Nikita", list.getLast());
        assertEquals("Kirill", list.getFirst());
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("add(index, data) inserts at correct position")
    void IsDataInsertsCorrectlyAtIndex(){
        list.addFirst("First Word");
        list.addLast("Last Word");
        list.add(1, "Between");

        assertEquals("First Word", list.get(0));
        assertEquals("Last Word", list.get(2));
        assertEquals("Between", list.get(1));
        assertEquals(3, list.size());
    }

    @Test
    @DisplayName("add (0, data) works like addLast")
    void addAtZeroWorksAsAddFirst(){
        list.add(0, "A");
        assertEquals("A", list.getFirst());
    }

    @Test
    @DisplayName("add(index, data) works like addLat()")
    void addAtIndexWorksAsAddLast(){
        list.addLast("A");
        list.add(1, "V");
        assertEquals("V", list.getLast());
    }

    @Test
    @DisplayName("add() throws IndexOutOfBoundsException for negative index")
    void addThrownForIndexGreaterThenSize(){
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, "X"));
    }

    @Test
    @DisplayName("getFirst() throws illegalStateException when empty")
    void getFirstThrowsWhenEmpty(){
        assertThrows(IllegalStateException.class, () -> list.getFirst());
    }

    @Test
    @DisplayName("getLast() throws illegalStateException when empty")
    void getLastThrowsWhenEmpty(){
        assertThrows(IllegalStateException.class, () -> list.getLast());
    }

    @Test
    @DisplayName("get() returns correct element by index")
    void getReturnsCorrectElement(){
        list.add(0, "D");
        list.addLast("F");
        list.addFirst("C");

        assertEquals("C", list.get(0));
        assertEquals("D", list.get(1));
        assertEquals("F", list.get(2));
    }

    @Test
    @DisplayName("get() returns IndexUotOfBoundException for negative index")
    void getThrowsForIndexOutOfRange(){
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
    }

    @Test
    @DisplayName("get() returns IndexOutOfBOundException for index >= size")
    void getThrowsFOrIndexOutOfRange(){
        list.addFirst("Noize");
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
    }

    @Test
    @DisplayName("removeFirst() removes first element")
     void removeFirstRemovesElement(){
        list.addLast("F");
        list.addFirst("C");
        list.removeFirst();

        assertEquals("F", list.getFirst());
        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("removeFirst() throws IllegalStateException when empty")
    void removeFirstThrowsWhenEmpty(){
        assertThrows(IllegalStateException.class, () -> list.removeFirst());
    }

    @Test
    @DisplayName("removeLast() removes last element")
    void removeLastElement(){
        list.addLast("F");
        list.addFirst("C");
        list.removeLast();

        assertEquals("C", list.getLast());
    }

    @Test
    @DisplayName("removeLast() throws IllegalStateException when list has one element")
    void removeLastWorksForSingleElement(){
        list.addLast("A");
        assertThrows(IllegalStateException.class, list::removeLast);
    }

    @Test
    @DisplayName("removeLast() throws IllegalStateException when empty")
    void removeLastThrowsWhenEmpty() {
        assertThrows(IllegalStateException.class, list::removeLast);
    }

    @Test
    @DisplayName("remove(index) removes element from middle")
    void removeMiddleElement(){
        list.addFirst("C");
        list.addFirst("B");
        list.addFirst("A");
        list.remove(1);

        assertEquals("A", list.get(0));
        assertEquals("C", list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    @DisplayName("remove(0) works like removeFirst()")
    void removeZeroWorksAsRemoveFirst() {
        list.addFirst("A");
        list.addLast("B");

        list.remove(0);
        assertEquals("B", list.get(0));
    }

    @Test
    @DisplayName("remove(size-1) works like removeLast()")
    void removeLastIndexWorksAsRemoveLst(){
        list.addLast("D");
        list.addFirst("A");
        list.addLast("B");
        list.remove(2);
        assertEquals("D", list.getLast());

    }

    @Test
    @DisplayName("remove() throws IndexOutOfBoundsException for negative index")
    void removeThrowsForNegativeIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
    }

    @Test
    @DisplayName("remove() throws IndexOutOfBoundsException for index >= size")
    void removeThrowsForIndexOutOfRange() {
        list.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(1));
    }

    @Test
    @DisplayName("Full workflow: add, get, remove, size")
    void fullWorkFlowTest(){
        assertTrue(list.size() == 0);

        list.addFirst("Word");
        assertEquals("Word", list.get(0));
        assertEquals(1, list.size());

        list.add(0, "O");
        assertEquals("O", list.getFirst());
        assertEquals("Word", list.getLast());

        list.remove(0);
        assertEquals("Word", list.getFirst());
        assertEquals(1, list.size());
    }

}
