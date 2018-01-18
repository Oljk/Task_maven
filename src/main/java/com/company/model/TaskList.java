package com.company.model;

import java.util.Iterator;
import java.io.Serializable;
public abstract class TaskList implements Iterable, Cloneable, Serializable {
    protected int size;
    public   TaskList() { }
    public abstract void add(Task task);
    public abstract int size();
    public abstract TaskList newList(); /*new*/
    public abstract Task getTask(int index)/* throws TaskEx*/;
    public abstract boolean remove(Task task);
    public abstract void clear();

    /*  public  TaskList incoming(Date from, Date to) {
          TaskList myArray = newList();

          for (int i = 0; i < this.size(); i++) {
              Date nextTime = new Date();
              try {
                  nextTime = this.getTask(i).nextTimeAfter(from);
              } catch (Exception e) {}

              if (nextTime.before(to) && nextTime != null) {
                  try {
                      myArray.add(this.getTask(i));
                  } catch (Exception e) { }
              }
          }
          return myArray;
      }
  */
    private class  myIterator implements Iterator {
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
      /*  if (this.getClass() == LinkedTaskList.class) {
        }*/
        link.clear();
        Iterator it = this.iterator();
        while (it.hasNext()) {
            Task task = ((Task) (it.next())).clone();
            link.add(task);
        }
        return link;
    }
}



