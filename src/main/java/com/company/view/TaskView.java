package com.company.view;

import com.company.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Отвечает за текстовый вывод и взаимодействие с пользователем приложения
 * @author olga
 * @version 1.0
 */

public class TaskView {
    /** Поле форматирования внешнего вида Даты для ввода/вывода*/
    private static SimpleDateFormat formatForDate =
            new SimpleDateFormat("y-MM-dd HH:mm");
    /** Вывод меню
     * @return String - строку, которую ввел пользователь - обозначает определенный пункт в меню
     * @param list - Tasklist с которым работает приложение
     * */
    public String menu(TaskList list) {
        String string;
        int index;
        Iterator it = list.iterator();
        System.out.println("A --- Show Calendar");
        System.out.println("B --- Add Task");
        System.out.println("C --- Exit");
        if(list.size()>0) {
            System.out.println("Enter the nuber of the Task: ");
        }else{
            System.out.println("There are no Tasks currently");
        }

        if(list.size()!=0) {
            for (int i = 1; it.hasNext(); i++) {
                System.out.println(i + " ---" + ((Task) (it.next())).tell());
            }
        }
        do{
            System.out.print("Enter your choise: ");
            string = inputString();
            if(string.equals("null")) {
                continue;
            }
            try {
                index = Integer.parseInt(string);
                if(list.size()==0) {
                    System.out.println("You entered wrong text.");
                    continue;
                }
                if(index<=0 || index > list.size()) {
                    System.out.println("You entered wrong index");
                    continue;
                }
                return "" + index;
            }catch(NumberFormatException e){
               if(string.equals("A") || string.equals("B") || string.equals("C") ||
                       string.equals("a") || string.equals("b") || string.equals("c")){
                   return string;
               }else{
                   System.out.println("You entered wrong text, try once more");
               }
            }
        }while(true);
    }

    /**
     * Вывод сообщения о том, что пользователь удет просматривать календарь
     * @param start ссылка на стартовую дату
     * @param end ссылка на конечкую дату
     *            в дальнейшем обьекты по ссылкам изменятся
     *
     */
    public void dateForCalendar(Date start, Date end){
        System.out.print("You want to view the calendar,");
        changeDate(start, end);
    }

    /**
     * Вывод календаря за промежуток времени
     * @param map ссылка на SortedMap, в котором хранятся задачи за введенны пользователем промежуток
     * @return int индекст задачи, которую выбрал пользователь или 0 - возврат
     */
    public int showCalendar(SortedMap<Date, Set<Task>> map){
        int i=1;
        for(Map.Entry entry: map.entrySet()) {
            //получить ключ
            Date key = (Date) entry.getKey();
            //получить значение
            Set<Task> value = (Set<Task>) entry.getValue();
            System.out.println("Date: " + formatForDate.format(key));
            for (Object o: value) {
                System.out.println("\t" + i + " -- " + ((Task)o).tell());
                i++;
            }
        }
        System.out.println("0 --- return back");
        int n;
        do{
            n = inputIndex();
            if(n==0) return n;
            if(n<0 || n>=i){
                continue;
            }
            return n;
        }while (true);
    }

    /**
     * Вывод параметром задачи + меню в вариантами возможных изменений
     * @param task задача, параметры которой выводят
     * @return индекс для меню - обозначает какое изменение произойдет с задачей или выход
     */
    public int showTask(Task task){
        System.out.println(task.tell());
        System.out.println("What do you want to change?");
        System.out.println("0 --- return back");
        System.out.println("1 --- change Date");
        System.out.println("2 --- change Title");
        System.out.println("3 --- change Interval");
        if(task.isActive()) {
            System.out.println("4 --- set inactive");
        }else{
            System.out.println("4 --- set active");
        }
        System.out.println("5 --- DELETE the task");
        int index;
        do {
            index = inputIndex();
            if (index == -1 || index > 5) continue;
            return index;
        }while(true);
    }

    /**
     *Вывод удаления задачи
     * @return 0 если пользователь не будет удалять задачу, 1 - будет
     */
    public int deletingTask(){
        System.out.println("Are you sure you want to delete this Task?");
        do {
            System.out.println("0 --- NO");
            System.out.println("1 -- YES");
            System.out.print("Enter: ");
            int n = inputIndex();
            if(n<0 || n>1){
                System.out.println("Enter correct.");
                continue;
            }
            return n;
        }while(true);
    }

    /**
     * Приветствие
     */
    public void welcome(){
        System.out.println("Welcome to our app!");
    }
    /**
     * Дает пользователю ввести 2 даты.
     * @param start первая дата, которую введет пользователь
     * @param end вторая
     */
    public void changeDate(Date start, Date end){
        do{
            System.out.print("Enter the start date in the format \"y-MM-dd HH:mm\" :");
            String s = inputString();
            try {
                Date st = formatForDate.parse(s);
                start.setTime(st.getTime());
                break;
            } catch (ParseException e) {
                System.out.println("Yoe entered smth wrong, try again");
            }
        }while(true);
        do{
            System.out.print("Enter the end date in the format \"y-MM-dd HH:mm\" :");
            String s = inputString();
            try {
                Date st = formatForDate.parse(s);
                end.setTime(st.getTime());
                break;
            } catch (ParseException e) {
                System.out.println("You entered smth wrong, try again");
            }
        }while(true);
    }

    /**
     * Метод предлагает пользователю ввести title
     * @return строку, которую ввел пользователь
     */
    public String changeTitle(){
        System.out.print("Enter title what you want: ");
        return inputString();
    }

    /**
     * Метод для ввода interval в определенном вормате, удобном для пользователя
     * 1 day 10 hour 17 minute 36 second
     * @return значение интервала в секундах - int
     */
    public int changeInterval(){
        System.out.print("Enter interval like - 1 day 10 hour 17 minute 36 second: ");
        int n;
        do {
            String s = inputString();
            n = TaskIO.parse_interval(s);
            System.out.println(TaskIO.interval(n) + " - is this the interval that you entered?");
            int index;
            do {
                System.out.println("0 --- NO");
                System.out.println("1 -- YES");
                System.out.print("Enter: ");
                index = inputIndex();
                if(index<0 || index>1){
                    System.out.println("Enter correct.");
                    continue;
                }
                break;
            }while(true);
            if(index==1) {
                return n;
            } else {
                System.out.println("Try again.");
            }
        }while(true);
    }

    /**
     * Изменяет входной параметр на противоположный и оповещает пользователя о том активным или неактивным стала задача
     * @param b - булевая переменная, обозначает активная или нет задача
     * @return будевое значение, противоположное b
     */
    public boolean changeActive(boolean b){
        b = !b;
        if(b) {
            System.out.println("Youe task is active.");
        }else{
            System.out.println("Your task is inactive.");
        }
        return b;
    }

    /**
     * Метод для ввода активности таска, если пользователь введет 1 - активный, 0 - неактивный
     * @return boolean -true, если таск активный, false - неактивный
     */
    public boolean setActive(){
        do {
            System.out.print("If your Task is inactive enter 0, if your Task is active enter 1: ");
            int i = inputIndex();
            if(i==-1 || i>1) {
                System.out.println("Your enter is wrong");
                continue;
            }
            if(i==0){
                return false;
            }else{
                return true;
            }
        }while(true);
    }

    /**
     * Выводит пользователю сообщение о том, что ему нужно ввести данные о задаче
     */
    public void addTask(){
        System.out.println("Enter data about your new Task: ");
    }

    /**
     * Выводит пользователю о выходе с программы
     */
    public void exit(){
        System.out.print("Thank you for using our application.");
    }

    /**
     * статический метод, который задействует Scanner для ввоода строки
     * @return введенную пользователем строку
     */
    private static String inputString() {
        Scanner sc = new Scanner(System.in);
        String  n;
        try {
            n = sc.nextLine();
        }
        catch(Exception e){
            return "null";
        }
        return n;
    }

    /**
     * статический метод для ввода индекса для меню - только интовское число
     * @return -1, если возникло исключение(пользователь ввел некорректные данные) или int, который ввел пользователь
     */
    private static int inputIndex() {
        Scanner sc = new Scanner(System.in);
        int  n;
        try {
            n = sc.nextInt();
        }
        catch(Exception e){
            return -1;
        }
        return n;
    }
}
