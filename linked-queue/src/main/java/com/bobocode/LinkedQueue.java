package com.bobocode;

import java.util.Objects;

/**
 * {@link LinkedQueue} implements FIFO {@link Queue}, using singly linked nodes. Nodes are stores in instances of nested
 * class Node. In order to perform operations {@link LinkedQueue#add(Object)} and {@link LinkedQueue#poll()}
 * in a constant time, it keeps to references to the head and tail of the queue.
 *
 * @param <T> a generic parameter
 */
public class LinkedQueue<T> implements Queue<T> {

    private int size;
    private Node<T> headNode;
    private Node<T> tailNode;

    /**
     * Adds an element to the end of the queue.
     *
     * @param element the element to add
     */
    public void add(T element) {
        Node<T> newNode = Node.valueOf(element);

        if (Objects.isNull(headNode)) {
            headNode = tailNode = newNode;
        } else {
            tailNode.next = newNode;
            tailNode = newNode;
        }

        size++;
    }

    /**
     * Retrieves and removes queue head.
     *
     * @return an element that was retrieved from the head or null if queue is empty
     */
    public T poll() {
        if (Objects.isNull(headNode)) {
            return null;
        }

        size--;
        throw new UnsupportedOperationException("This method is not implemented yet"); // todo: implement this method
    }

    /**
     * Returns a size of the queue.
     *
     * @return an integer value that is a size of queue
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return {@code true} if the queue is empty, returns {@code false} if it's not
     */
    public boolean isEmpty() {
        return size == 0;
    }

    private static class Node<T> {

        T value;
        Node<T> next;

        static <T> Node<T> valueOf(T element) {
            Node<T> result = new Node<>();
            result.value = element;

            return result;
        }

    }

}
