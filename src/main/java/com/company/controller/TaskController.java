package com.company.controller;

import com.company.model.*;
import com.company.view.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
* Отвечает за логику работы приложения
* @author olga
* @version 1.0
 */


public class TaskController {
    /** Logger log4j*/
    final static Logger logger = Logger.getLogger(TaskController.class);
    /** обьект класса TaskView, методы которого будут использоваться для взаимодействия с пользователем*/
    private TaskView view;
    /** TaskList в котором хранятся все задачи пользователя*/
    private TaskList list;
    /**Файл, в котором хранится тасклист*/
    private File dan;
    /**Поток, который работает паралелльно и отвечает за уведомление пользователя о текущей задаче*/
    private Thread myTaskThread;
    /** Конструктор, создает обьект класса
     * @param view обьект класса TaskView
     * @param file файл, в котором будут хранится данные
     * */
    public TaskController(TaskView view, File file) {
        this.view = view;
        list = new LinkedTaskList();
        dan = file;
        if(!dan.exists()){
            try {
                dan.createNewFile();
            } catch (IOException e) {
                logger.error("IOExc in creating file " + e);
            }
        }
        try {
            TaskIO.readText(list, dan);
        } catch (TaskIOException e) {
            logger.error("TaskIOExc in reading " + e);
        }
        myTaskThread = new TaskThread(list);
        myTaskThread.setDaemon(true);
        myTaskThread.start();
        this.view.welcome();
    }
    /** Прописана логика меню
     * вызываются методы календаря, добавления задачи, просмотра задачи и выхода с программы */

    public void menu(){
        do {
            String menu = view.menu(list);
            int index;
            try {
                index = Integer.parseInt(menu);
                Task myTask = list.getTask(index-1); //нумерация с 1
                showAndEditTask(myTask);
            } catch (NumberFormatException e) {
                switch (menu) {
                    case "A":
                    case "a":
                        calendar();
                        break;
                    case "B":
                    case "b":
                        addTask();
                        break;
                    case "C":
                    case "c":
                        exit();
                        return;
                    default:
                        System.out.println("Something incredible and very exciting happened.");
                        exit();
                        return;
                }
            }
        }while(true);
    }
    /** метод, который записывает list в файл dan*/
    private void editFile(){
        try {
            TaskIO.writeText(list, dan);
        } catch (TaskIOException e) {
            logger.error("TaskIOExc in editing ", e);
        }
    }
    /**Метод просмотра календаря
     * пользователь вводит две даты
     * получает вывод календаря в виде дата - все задачи
     * может выбрать задачу и посмотреть ее параметры:
     * */
    public void calendar() {
        int index;
        Date start = new Date();
        Date end = new Date();
        if (list.size() == 0) {
            System.out.println("It is nothing in your list..."); //выносить метод в View или оставить так?
            return;
        }
        view.dateForCalendar(start, end);
        SortedMap<Date, Set<Task>> mymap = Tasks.calendar(list.clone(), start, end);
        do {
            index = view.showCalendar(mymap);
            if (index == 0) {
                return; //возврат в меню
            } else {
                Task myTask = inMapByIndex(mymap, index); //нашла таск в календаре, но это копия
                myTask = list.findEqual(myTask); //теперь ссылка на тот таск, который в листе
                showAndEditTask(myTask);
            }
        }while(true);
    }

    /**
     * Логика вывода параметров задачи, и изменения этих параметров, включая удаление
     * @param myTask - ссылка на задачу, которая изменяется
     */
    private void showAndEditTask(Task myTask){
        int index;
        do {
            index = view.showTask(myTask);
            switch (index) {
                case 0:
                    return;
                case 1:
                    view.changeDate(myTask.getStartTime(), myTask.getEndTime());
                    break;
                case 2:
                    myTask.setTitle(view.changeTitle());
                    break;
                case 3:
                    myTask.setTime(myTask.getStartTime(), myTask.getEndTime(), view.changeInterval());
                    break;
                case 4:
                    myTask.setActive(view.changeActive(myTask.isActive()));
                    break;
                case 5:
                    if(view.deletingTask()){
                        list.remove(myTask);
                        editFile();
                        return;
                    }
                    break;
            }
            editFile();
        }while(true);
    }

    /**
     * находит ссылку на задачу, которую именно выбрал пользователь, когда просматривал календарь
     * @param mymap SortedMap, которую возвращает метод Tasks.calendar
     * @param index индекс таска, который выбрал пользователь
     * @return ссылка на задачу, которую выбрал пользователь
     */
    private Task inMapByIndex(SortedMap<Date, Set<Task>> mymap, int index){
        int i=1;
        for(Map.Entry entry: mymap.entrySet()) {
            Set<Task> value = (Set<Task>) entry.getValue();
            for (Object o: value) {
                if(i==index) return ((Task) o);
                i++;
            }
        }
        return null;
    }

    /**
     * логика добавления задачи в лист
     */
    public void addTask(){
        Task task = new Task();
        view.addTask();
        task.setTitle(view.changeTitle());
        Date start = new Date();
        Date end = new Date();
        view.changeDate(start, end);
        int interval =  view.changeInterval();
        task.setTime(start,end,interval);
        task.setActive(view.setActive());
        list.add(task);
        editFile();
    }
    /**
    Выход из программы - сообщение и закрытие myTaskThread потока
     */
    public void exit(){
        myTaskThread.interrupt();
        view.exit();
    }

}
