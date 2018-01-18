package com.company.model;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

public class Task implements Cloneable, Serializable {
    private String titlle;
    private  boolean isactive;
    private  int interval; //seconds in tests
    private int start;
    private  int end;
    private  Date end_;
    private  Date start_;
    private  boolean isrep = false;
    private static SimpleDateFormat formatForDate =
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
        //   if (interval <= 0) interval = 10;
        this.start_ = (Date) start.clone();
        this.end_ = (Date) end.clone();
        this.interval = interval;
    }
    public boolean isRepeated() {
        if (interval > 0) return true;
        return false;
    }
    public Date nextTimeAfter(Date current) {
        if (!isactive) return null; //Chto Vozvraschatj vmesto -1???
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


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != Task.class) return false;
        Task x1 = (Task) o;
        if (x1.start_ == null)
            if (this.titlle == null && x1.titlle != null) return false;
        if (x1.titlle == null && this.titlle != null) return false;
        if (this.start_ == null && x1.start_ != null) return false;
        if (x1.start_ == null && this.start_ != null) return false;
        if (this.end_ == null && x1.end_ != null) return false;
        if (x1.end_ == null && this.end_ != null) return false;
        if ((x1.titlle.equals(this.titlle)) && x1.isactive == this.isactive && this.interval == x1.interval
                && this.start_.equals(x1.start_) && this.end_.equals(x1.end_) && this.start == x1.start
                && this.end == x1.end) return true;
        return false;
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
        return hash + interval * 3 + start * 2 + end * 4;
    }
    public Task() {
    }
    public String toString() {
        String s = "title: " + titlle + ", is active: " + isactive + ", start:  " + start + ",end: " + end + ", interval: " + interval;
        if (start_ != null && end_ != null) {
            s = s + ", start_d - " + formatForDate.format(start_) + ", end_d " + formatForDate.format(end_);
        }
        return  s;
    }
    public String tell() { //+
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