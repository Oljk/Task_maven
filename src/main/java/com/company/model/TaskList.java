package com.company.model;

import java.util.Iterator;
import java.io.Serializable;
/**
 * An abstract class that implements the storage of tasks
 * @author olga
 * @version 1.0
 * @see com.company.model.LinkedTaskList
 * @see com.company.model.ArrayTaskList
 */
public abstract class TaskList implements Iterable, Cloneable, Serializable {
    /**
     * size of tasklist
     */
    protected int size;
    public   TaskList() { }

    /**@param task link on the task, which is adding*/
    public abstract void add(Task task);

    /**
     * @return size of list
     */
    public abstract int size();

    /**
     * @return new object of child-class
     */
    public abstract TaskList newList();

    /**
     * @param index in list
     * @return link of the task by index in list
     */
    public abstract Task getTask(int index);

    /**
     * @param task link of the task is deleting
     * @return true - if deleting was successfully
     */
    public abstract boolean remove(Task task);

    /**
     * clear the list completely
     */
    public abstract void clear();

    /**
     * inner class, that implements Iterator
     * @see java.util.Iterator
     */
    private class  myIterator implements Iterator {
        /**
         * current index of element
         */
        private int currentIndex = -1;
        myIterator() {
        }
        public boolean hasNext() {
            return  ((currentIndex < (TaskList.this.size - 1)) && (size != 0));
        }

        public Task next() {
            currentIndex++;
            return getTask(currentIndex);
        }

        public void remove() {
            if (currentIndex < 0 || currentIndex >= size)  throw new IllegalStateException();
            TaskList.this.remove(getTask(currentIndex));
            currentIndex--;
        }
    }
    public Iterator iterator() {
        return new myIterator();
    }

    public int hashCode() {
        if (size == 0) return 0;
        int hash = 0;
        Iterator it = this.iterator();
        while (it.hasNext()) {
            hash += it.next().hashCode();
        }
        return hash * size;
    }
    public String toString() {
        Iterator it = this.iterator();
        StringBuilder s = new StringBuilder();
        int i = 0;
        s.append("\n");
        while (it.hasNext()) {
            s.append(this.getClass().getSimpleName() + "[" + i + "] " + ((Task) (it.next())).toString() + "\n");
            i++;
        }
        return s.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        TaskList task = (TaskList) o;
        if (task.size != this.size) return false;
        Iterator it = task.iterator();
        Iterator it2 = this.iterator();
        while (it.hasNext()) {
            if (!it.next().equals(it2.next())) return false;
        }
        return true;
    }

    /** find equal task in list
     * @param task link of the rask
     * @return equal task in the list, if it exist, if no - null
     */
    public Task findEqual(Task task){
        for (Object o:this) {
            if((o).equals(task)) return ((Task)o);
        }
        return null;
    }
    public TaskList  clone() {
        TaskList link;
        try {
            link = (TaskList) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        link.clear();
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Task task = ((Task) (it.next())).clone();
            link.add(task);
        }
        return link;
    }
}



