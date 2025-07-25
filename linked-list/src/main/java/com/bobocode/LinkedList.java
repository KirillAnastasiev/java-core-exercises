package com.bobocode;

import java.util.Arrays;
import java.util.Objects;

/**
 * {@link LinkedList} is a list implementation that is based on singly linked generic nodes. A node is implemented as
 * inner static class {@link Node<T>}. In order to keep track on nodes, {@link LinkedList} keeps a reference to a head node.
 *
 * @param <T> generic type parameter
 */
public class LinkedList<T> implements List<T> {

    private Node<T> headNode;
    private Node<T> tailNode;
    private int size;

    /**
     * This method creates a list of provided elements
     *
     * @param elements elements to add
     * @param <T>      generic type
     * @return a new list of elements the were passed as method parameters
     */
    @SafeVarargs
    public static <T> List<T> of(T... elements) {
        LinkedList<T> linkedList = new LinkedList<>();
        Arrays.stream(elements).forEach(linkedList::add);

        return linkedList;
    }

    /**
     * Adds an element to the end of the list
     *
     * @param element element to add
     */
    @Override
    public void add(T element) {
        Node<T> newNode = new Node<>();
        newNode.value = element;

        if (Objects.isNull(headNode)) {
            headNode = tailNode = newNode;
        } else {
            tailNode.next = newNode;
            tailNode = tailNode.next;
        }

        size++;
    }

    /**
     * Adds a new element to the specific position in the list. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index   an index of new element
     * @param element element to add
     */
    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        if (index == size) {
            add(element);
            return;
        }

        Node<T> newNode = new Node<>();
        newNode.value = element;
        if (index == 0) {
            newNode.next = headNode;
            headNode = newNode;
        } else {
            Node<T> currentNode = headNode;
            for (int i = 1; i < index; i++) {
                currentNode = currentNode.next;
            }
            newNode.next = currentNode.next;
            currentNode.next = newNode;
        }

        size++;
    }

    /**
     * Changes the value of an list element at specific position. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index   an position of element to change
     * @param element a new element value
     */
    @Override
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        Node<T> currentNode = headNode;
        for (int i = 1; i <= index; i++) {
            currentNode = currentNode.next;
        }
        currentNode.value = element;
    }

    /**
     * Retrieves an elements by its position index. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index element index
     * @return an element value
     */
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        Node<T> currentNode = headNode;
        for (int i = 1; i <= index; i++) {
            currentNode = currentNode.next;
        }

        return currentNode.value;
    }

    /**
     * Removes an elements by its position index. In case provided index in out of the list bounds it
     * throws {@link IndexOutOfBoundsException}
     *
     * @param index element index
     */
    @Override
    public void remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }

        if (index == 0) {
            headNode = headNode.next;
        } else {
            Node<T> currentNode = headNode;
            for (int i = 1; i < index ; i++) {
                currentNode = currentNode.next;
            }
            Node<T> removedNode = currentNode.next;
            currentNode.next = removedNode.next;
            removedNode.next = null;
        }

        size--;
    }


    /**
     * Checks if a specific exists in he list
     *
     * @return {@code true} if element exist, {@code false} otherwise
     */
    @Override
    public boolean contains(T element) {
        if (isEmpty()) {
            return false;
        }

        Node<T> currentNode = headNode;
        while (currentNode != tailNode.next) {
            if (element.equals(currentNode.value)) {
                return true;
            }
            currentNode = currentNode.next;
        }

        return false;
    }

    /**
     * Checks if a list is empty
     *
     * @return {@code true} if list is empty, {@code false} otherwise
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the number of elements in the list
     *
     * @return number of elements
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Removes all list elements
     */
    @Override
    public void clear() {
        headNode = tailNode = null;
        size = 0;
    }

    private static class Node<T> {

        private T value;
        private Node<T> next;

    }

}
