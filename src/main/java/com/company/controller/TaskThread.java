package com.company.controller;

import com.company.model.*;

import java.util.Date;

public class TaskThread extends Thread{
    TaskList list;
    Date helpNext = new Date(20); //дата, которая последняя была активной, в начале будет там 1970 года, чтобы было что для сравнения
    TaskList helpList = new LinkedTaskList(); //лист тасков, которые были в эту активную дату(в один момет может быть два таска одновременно)
    public TaskThread(TaskList list) {
      this.list = list;
    }
    @Override
    public void run() {
        int i=0;
        while(true){
            if(this.isInterrupted()){
                break;
            }
            for (Object task: list) {
                Date date = new Date(); //по идее тут текущее время
                Date next = ((Task) task).nextTimeAfter(date);
                if(next!=null &&((Task) task).isActive() && (next.getTime()-date.getTime())<5){ //цифра  зависит от того, как быстро работает комп equal не работает
                    if(helpNext.equals(next) && helpList.findEqual((Task) task) != null){ /*этот кусок кода только для того, чтобы не высвечивалось уведомление несколько раз*/
                        continue;
                    }else if(helpNext.equals(next)){
                        helpList.add((Task) task);
                    }else{
                        helpNext = next;
                        helpList.clear();
                        helpList.add((Task) task);
                    }
                    synchronized (((Task) task)){
                        System.out.println("\n" + i + " Now you must complete this task: " + ((Task) task).tell() + "\nContinue working with the program.");
                        i++; //просто для наглядности, при проверке работоспособности
                    }
                }
            }
        }
    }
}
