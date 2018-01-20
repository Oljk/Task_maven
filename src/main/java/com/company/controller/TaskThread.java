package com.company.controller;

import com.company.model.*;

import java.util.Date;

/**
 * Класс описывает поток, который все время проверяет, если ли таск, который должен выполнится в это время
 * @author olga
 * @version 1.0
 */
public class TaskThread extends Thread{
    /**
     * тасклист, с которым работает программа
     */
   private TaskList list;
    /**
     * дата, которая последняя была активной, в начале будет 1970 года, чтобы было что для сравнения
     */
   private Date helpNext = new Date(20);
    /**
     * лист тасков, которые были в эту активную дату
     * (в один момет может быть два таска одновременно)
     */
    private TaskList helpList = new LinkedTaskList();

    /**
     * Конструктор, создает обьект класса
     * @param list с которым рабоает приложение, создается в com.company.controller.TaskController
     */
    public TaskThread(TaskList list) {
      this.list = list;
    }

    /**
     * Перегруженный метод run
     * Все время бесконечным циклом проверяет если ли в list таски, которые должны исполнятся в текущее время
     * цикл прекращается, если поток прерван(isInterrupted())
     * поток прерывается в методе com.company.controller.TaskController.exit();
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
                if(next!=null &&((Task) task).isActive() && (next.getTime()-date.getTime())<7){ //цифра  зависит от того, как быстро работает комп equal не работает
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
