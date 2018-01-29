package com.company.controller;

import com.company.model.*;
import com.company.view.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.*;
/**
* Logic of working the application
* @author olga
* @version 1.0
 */


public class TaskController {
    /** Logger log4j*/
    final static Logger logger = Logger.getLogger(TaskController.class);
    /** A Task View object whose methods will be used to interact with the user*/
    private TaskView view;
    /** TaskList - saves all Tasks of the usser*/
    private TaskList list;
    /**file - saves the tasklist*/
    private File dan;
    /**A thread that works in parallel and is responsible for notifying the user of the current task*/
    private Thread myTaskThread;
    /** Constructor, creates an object of the class
     * @param view object of the class TaskView
     * @param file the file in which this tasklist will be saved
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
        this.menu();
    }
    /** The logic of the menu
     * called  methods of the calendar, adding a task, viewing the task and exiting the program */
    private void menu(){
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
    /** method writes list in the file dan*/
    private void editFile(){
        try {
            TaskIO.writeText(list, dan);
        } catch (TaskIOException e) {
            logger.error("TaskIOExc in editing ", e);
        }
    }
    /** method to view the calendar
     * user enters 2 dates
     * gets caledar in the view: date - tasks on this date
     * can choose the task and see its parametrs
     */
    private  void calendar() {
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
                return; //return to menu
            } else {
                Task myTask = inMapByIndex(mymap, index); //find task in calendar, but it is a copy
                myTask = list.findEqual(myTask); //link on the task in this taskList
                showAndEditTask(myTask);
            }
        }while(true);
    }
    /**
     * output parameters of task and opportunity to change this parameters, including deleting
     * @param myTask - link on the task, what will be changed
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
     * finds a link to the task, that the user chose, when was viewing the calendar
     * @param mymap SortedMap,which returns the method  Tasks.calendar
     * @param index the index of the task the user has chosen
     * @return the link of the task the user has chosen
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
     * logic of adding a task to the list
     */
    private void addTask(){
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
     Exiting the program - message and closing the myTaskThread thread
     */
    private void exit(){
        myTaskThread.interrupt();
        view.exit();
    }
}
