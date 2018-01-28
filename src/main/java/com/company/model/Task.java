package com.company.model;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

/**
 * Класс, в котором хранятся все параметры задачи
 * @author olga
 * @version 1.0
 */
public class Task implements Cloneable, Serializable {
    private String titlle;
    private  boolean isactive;
    /**
     * интервал в секундах
     */
    private  int interval;
    /**
     * Дата в int формате
     */
    //private int start;
    //private  int end;
    private  Date end_;
    private  Date start_;
    /**
     * Паттерн для даты
     */
    private static final SimpleDateFormat formatForDate =
            new SimpleDateFormat("[yyyy-MM-dd  HH:mm:ss.SSS]");
    public Task(String titlle) {
        this.titlle = titlle;
    }

    public Task(String titlle, Date time) {
        if (titlle == null) titlle = "null";
        this.titlle = titlle;
        this.start_ = (Date) time.clone();
        this.isactive = false;
        this.end_ = (Date) time.clone();
    }
    public Task(String titlle, Date start, Date end, int interval) {
        if (interval < 0) interval = 0;
        if (titlle == null) titlle = "null";
        this.titlle = titlle;
        this.isactive = false;
        this.interval = interval;
        this.start_ = (Date) start.clone();
        this.end_ = (Date) end.clone();
    }
    public void setTitle(String titlle) {
        if (titlle == null) titlle = "null";
        this.titlle = titlle;
    }
    public boolean isActive() {
        return isactive;
    }

    public void setActive(boolean isactive) {
        this.isactive = isactive;
    }

    public Date getTime() {
        return start_;
    }
    public  void setTime(Date time) {
        if (interval > 0) interval = 0;
        this.start_ = (Date) time.clone();
        this.end_ = (Date) time.clone();
    }

    public Date getStartTime() {
        return start_;
    }

    public Date getEndTime() {
        return end_;
    }
    public  int getRepeatInterval() {
        return interval;
    }

    public void setTime(Date start, Date end, int interval) {
        this.start_ = (Date) start.clone();
        this.end_ = (Date) end.clone();
        this.interval = interval;
    }
    public boolean isRepeated() {
        return interval > 0;
    }
    public Date nextTimeAfter(Date current) {
        if (!isactive) return null;
        if (interval == 0) {
            if (current.before(start_)) return start_;
            return null;
        }
        long nextTime;
        for (nextTime = start_.getTime(); nextTime <= current.getTime();) {
            if (nextTime > end_.getTime()) return null;
            nextTime += interval * 1000;
        }
        if (nextTime > end_.getTime()) return null;
        return new Date(nextTime);
    }

    public String getTitle() {
        return titlle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (isactive != task.isactive) return false;
        if (interval != task.interval) return false;
        if (titlle != null ? !titlle.equals(task.titlle) : task.titlle != null) return false;
        if (end_ != null ? !end_.equals(task.end_) : task.end_ != null) return false;
        return start_ != null ? start_.equals(task.start_) : task.start_ == null;
    }

    public int hashCode() {
        int hash;
        if (titlle == null) {
            hash = 0;
        } else {
            hash = titlle.hashCode();
        }
        if (start_ != null) hash += start_.hashCode() + end_.hashCode();
        if (isactive) hash++;
        return hash + interval * 3;
    }
    public Task() {
    }
    public String toString() {
        String s = "title: " + titlle + ", is active: " + isactive + ", interval: " + interval;
        if (start_ != null && end_ != null) {
            s = s + ", start_d - " + formatForDate.format(start_) + ", end_d " + formatForDate.format(end_);
        }
        return  s;
    }

    /**
     * Метод, который создает строке - описание задачи в корректном для пользователя виде
     * @return строку
     */
    public String tell() {
        String s = "Task: " + titlle ;
        if(isactive) {
            s += ", active, ";
        }else{
            s += ", inactive, ";
        }
        if (start_ != null && end_ != null) {
            s += "start date - " + formatForDate.format(start_);
            if(isRepeated()) {
                s += ", end date " + formatForDate.format(end_);
            }
        }
        if(this.isRepeated()) {
            s += ", every " + TaskIO.interval(interval);
        }else{
            s += " Task is not repeated";
        }
        return  s;
    }
    public Task clone() {
        Task task;
        try {
            task = (Task) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
        if (this.end_ == null) return task;
        task.end_ = (Date) this.end_.clone();
        task.start_ = (Date) this.start_.clone();
        return task;
    }

}