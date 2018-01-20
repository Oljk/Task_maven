package com.company.model;
/**
 * Наследник TaskList реализует хранение задач по логике LinkedList
 * @author olga
 * @version 1.0
 * @see com.company.model.TaskList
 * @see com.company.model.ArrayTaskList
 */

public class LinkedTaskList extends TaskList {
    /**
     * Начальный узел
     */
    private Node head = null;
    /**
     * Конечный узел
     */
    private Node tail = null;

    /**
     * внутренний класс Узел
     */
    public class Node {
        /**
         * ссылка на саму задача(Task)
         */
        private Task value;
        /**
         * Ссылка на следующий узел
         */
        private Node next;
        /**
         * Ссылка на предыдущий узел
         */
        private Node prev;

        /**
         * Конструктор, создает обьект класса
         * @param task - ссылка на задачу, которая записывается в значение узла
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