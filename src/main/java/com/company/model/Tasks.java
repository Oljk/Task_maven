package com.company.model;
import java.util.*;

public class Tasks {
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date from, Date to) {
     /*   try {
            myArray = tasks.getClass().newInstance();
        }
        catch(Exception e){}*/

           /* for (int i = 0; i < this.size(); i++) {
                Date nextTime = new Date();
                try {
                    nextTime = this.getTask(i).nextTimeAfter(from);
                } catch (Exception e) {}

                if (nextTime.before(to) && nextTime != null) {
                    try {
                        myArray.add(this.getTask(i));
                    } catch (Exception e) { }
                }
            }*/
        if (tasks == null || to == null || from == null) return null;
        Iterator it = tasks.iterator();
        while (it.hasNext()) {
            Date nextTime;
            nextTime = ((Task) it.next()).nextTimeAfter(from);
            if (nextTime == null || nextTime.after(to)) {
                it.remove();
            }
        }
        return tasks;
    }

    public static SortedMap<Date, Set<Task>> calendar(Iterable<Task> tasks, Date start, Date end) {
        SortedMap<Date, Set<Task>> map = new TreeMap<Date, Set<Task>>();
        if (tasks == null || start == null || end == null) return null;
        Iterator it = incoming(tasks, start, end).iterator();
        while (it.hasNext()) {
            Task currentTask = (Task) it.next();
            Date nextTime = currentTask.nextTimeAfter(start);
            if (currentTask.isRepeated()) {
                long helpTime = 0;
                Date i = (Date) nextTime.clone();
                for (; i.before(end) || i.equals(end); i.setTime(helpTime)) {
                    for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
                        if (pair.getKey().equals(i)) {
                            Set<Task> meset;
                            meset = pair.getValue();
                            meset.add(currentTask.clone());
                            pair.setValue(meset);
                        }
                    }
                    helpTime = i.getTime() + currentTask.getRepeatInterval() * 1000;
                    if (!map.containsKey(i)) {
                        Set<Task> kek = new HashSet<Task>();
                        kek.add(currentTask.clone());
                        map.put((Date) i.clone(), kek);
                    }
                }
            } else {
                Set<Task> myset = new HashSet<Task>();
                if (map.containsKey(nextTime)) {
                    for (Map.Entry<Date, Set<Task>> pair : map.entrySet()) {
                        if (pair.getKey().equals(nextTime)) {
                            myset = pair.getValue();
                            myset.add(currentTask);
                            pair.setValue(myset);
                        }
                    }
                } else {
                    myset.add(currentTask.clone());
                    map.put((Date) nextTime.clone(), myset);
                }
            }

        }
        return map;
    }
}
