package com.company.model;
/**
 * child of TaskList class that implements the storage of tasks by LinkedList logic
 * @author olga
 * @version 1.0
 * @see com.company.model.TaskList
 * @see com.company.model.ArrayTaskList
 */

public class LinkedTaskList extends TaskList {
    /**
     * start node
     */
    private Node head = null;
    /**
     * end node
     */
    private Node tail = null;

    /**
     * inner class node
     */
    public class Node {
        /**
         * link of current task
         */
        private Task value;
        /**
         * link on the next node
         */
        private Node next;
        /**
         * link on the previous node
         */
        private Node prev;

        /**
         * constructor, make an object of the class
         * @param task - lik on the task - value
         */
        private Node(Task task) {
            value = task;
        }
    }

    public void add(Task task) {
        if (task == null) return;
        Node node = new Node(task);
        Node h;
        if (head == null) {
            head = node;
            tail = node;
        } else {
            tail.next = node;
            h = tail;
            tail = node;
            tail.prev = h;
        }
        size++;
    }

    public boolean remove(Task task) {
        Node current = head;
        while (current != null) {
            if (current.value.equals(task)) {
                if (current.prev != null) {
                    current.prev.next = current.next;
                    if (current == head) {
                        current.next.prev = null;
                    }
                    if (current.next != null) current.next.prev = current.prev;

                    if (current.next == null)  {
                        tail = current.prev;
                    }
                } else {
                    head = head.next;
                    if (head == null) {
                        tail = null;
                    } else {
                        head.prev = null;
                    }
                }
                size--;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public TaskList newList() {
        return new LinkedTaskList();
    }

    public Task getTask(int index) {
        if (index < 0) return new Task();
        if (head == null) return new Task();
        Node node = head;
        for (int i = 0; i < size; i++) {
            if (node == null) return new Task();
            if (node.value == null) return new Task();
            if (index == i) return node.value;
            node = node.next;
        }
        return new Task();
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
    public LinkedTaskList  clone() {
        return (LinkedTaskList) super.clone();
    }
}