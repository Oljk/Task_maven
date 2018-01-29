package com.company.model;
/**
 * child of TaskList class that implements the storage of tasks by ArrayList logic
 * @author olga
 * @version 1.0
 * @see com.company.model.TaskList
 * @see com.company.model.LinkedTaskList
 */

public class ArrayTaskList  extends TaskList  {
    private Task[] TaskArray = new Task[Count];

    private static final int Count = 10;

    public ArrayTaskList() {
    }

    public void add(Task task) {
        if (task == null) return;
        if (size % Count == 0) {
            Task[] help =  new Task[size + Count];
            System.arraycopy(TaskArray, 0, help, 0, size);
            TaskArray = help;
        }
        TaskArray[size] = task;
        size++;
    }

    public int size() {
        return size;
    }

    public TaskList newList() {
        return new ArrayTaskList();
    }


    public Task getTask(int index) {
        if (index >= size) return new Task();
        return TaskArray[index];
    }

    public boolean remove(Task task) {
        for (int i = 0; i < size; i++) {
            if (TaskArray[i].equals(task)) {
                for (; i < size - 1; i++) {
                    TaskArray[i] = TaskArray[i + 1];
                }
                size--;
                if (size % Count == 0) {
                    Task[] help =  new Task[TaskArray.length - Count];
                    System.arraycopy(TaskArray, 0, help, 0, size);
                    TaskArray = help;
                }
                return true;
            }
        }
        return false;
    }
    public void clear() {
        size = 0;
        if (TaskArray.length > Count) TaskArray = new Task[Count];
    }
    public ArrayTaskList  clone() {
        return (ArrayTaskList) super.clone();
    }

}