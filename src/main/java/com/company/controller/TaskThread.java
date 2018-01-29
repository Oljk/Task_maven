package com.company.controller;

import com.company.model.*;

import java.util.Date;

/**
 * A class describes a thread that checks all the time if there is a task to be executed at this time
 * @author olga
 * @version 1.0
 */
public class TaskThread extends Thread{
    /**
     * Tasklist with which the program works
     */
   private TaskList list;
    /**
     * the date the latest was active, in starting will be 1970 year - to have smth to compare
     */
   private Date helpNext = new Date(20);
    /**
     * tasklist of the task was active in this active-specific
     * ()
     */
    private TaskList helpList = new LinkedTaskList();

    /** Constructor, creates an object of the class
     * @param list with which the application is working, was created in com.company.controller.TaskController
     */
    public TaskThread(TaskList list) {
      this.list = list;
    }

    /**
     * Overrided method run
     * All the time, an infinite loop checks if there are in the list task, that should be executed in current time
     * the loop stopped, when the thread is Interrupted
     * the thread interropts in the method com.company.controller.TaskController.exit();
     */
    @Override
    public void run() {
        while(true){
            if(this.isInterrupted()){
                break;
            }
            for (Object task: list) {
                Date date = new Date(); //тут текущее время
                Date next = ((Task) task).nextTimeAfter(date);
                if(next!=null &&((Task) task).isActive() && (next.getTime()-date.getTime())<8){ //цифра  зависит от того, как быстро работает комп equal не работает
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
                        System.out.println("\n"+  "Now you must complete this task: " + ((Task) task).tell() + "\nContinue working with the program.");

                    }
                }
            }
        }
    }
}
