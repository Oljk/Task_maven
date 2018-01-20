package com.company.model;

import java.util.Iterator;
import java.io.Serializable;
/**
 * Абстрактный класс, который реализует хранение задач
 * @author olga
 * @version 1.0
 * @see com.company.model.LinkedTaskList
 * @see com.company.model.ArrayTaskList
 */
public abstract class TaskList implements Iterable, Cloneable, Serializable {
    /**
     * Переменная, в которой хранится размер массива
     */
    protected int size;
    public   TaskList() { }

    /**
     * Добавление задачи в лист
     * @param task ссылка на задачу, которая добавляется
     */
    public abstract void add(Task task);

    /**
     * @return размер массива
     */
    public abstract int size();

    /**
     * @return новый обьект класса-наследника
     */
    public abstract TaskList newList();

    /**
     * @param index - индекс в массиве
     * @return ссылку на задачу по индексу в массиве
     */
    public abstract Task getTask(int index);

    /**
     * @param task ссылка на задачу, которая удаляется
     * @return true - если удаление прошло успешно
     */
    public abstract boolean remove(Task task);

    /**
     * Очищает лист
     */
    public abstract void clear();

    /**
     * Внутренний класс, который реализует интерфейс Iterator
     * @see java.util.Iterator
     */
    private class  myIterator implements Iterator {
        /**
         * текущий индекс элемента
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

    /** находит точно такую же задачу-копию в листе
     * @param task ссылка на задачу
     * @return копию приходящей задачи, если она есть, если нет - null
     */
    public Task findEqual(Task task){
        for (Object o:this) {
            if(((Task)o).equals(task)) return ((Task)o);
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



