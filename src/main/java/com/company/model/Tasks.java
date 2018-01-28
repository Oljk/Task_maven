package com.company.model;
import java.util.*;

/**
 * Класс со статическими методами для работы с Iterable<Task>
 *  @author olga
 * @version 1.0
 */
public class Tasks {
    public static Iterable<Task> incoming(Iterable<Task> tasks, Date from, Date to) {
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
        SortedMap<Date, Set<Task>> map = new TreeMap<>();
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
                        Set<Task> kek = new HashSet<>();
                        kek.add(currentTask.clone());
                        map.put((Date) i.clone(), kek);
                    }
                }
            } else {
                Set<Task> myset = new HashSet<>();
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
