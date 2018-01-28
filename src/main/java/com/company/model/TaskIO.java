package com.company.model;
import java.io.*;
import java.nio.CharBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для работы с потоками записи и считывания, и файлами
 * @author olga
 * @version 1.0
 */

public class TaskIO {
    /**
     * Шаблон для даты
     */
    private static final SimpleDateFormat formatForDate =
            new SimpleDateFormat("'['y-MM-dd  HH:mm:ss.SSS']'");
    public static void write(TaskList tasks, OutputStream out)
            throws TaskIOException {
        try (ObjectOutputStream  myObjectStr = new ObjectOutputStream(out)) {
            myObjectStr.writeObject(tasks);
        }
        catch (IOException e) {
            throw new TaskIOException("IOException  in write(TaskList tasks, OutputStream out)", e);
        }
    }
    public static void read(TaskList tasks, InputStream in) throws TaskIOException {
        TaskList my;
        try (ObjectInputStream inObjStr = new ObjectInputStream(in)) {
            my = (TaskList) inObjStr.readObject();
        } catch (IOException e) {
            throw new TaskIOException("IOException  in read(TaskList tasks, InputStream in)", e);
        } catch (ClassNotFoundException e) {
            throw new TaskIOException("ClassNotFoundException  in read(TaskList tasks, InputStream in)", e);
        }
        Iterator it = my.iterator();
        while (it.hasNext()) {
            tasks.add((Task) it.next());
        }
    }
    public static void writeBinary(TaskList tasks, File file) throws TaskIOException {
        try {
            OutputStream os = new FileOutputStream(file);
            write(tasks, os);
        } catch (FileNotFoundException e) {
            throw new TaskIOException("FileNotFoundException in writeBinary(TaskList tasks, File file)", e);
        }
    }
    public static void readBinary(TaskList tasks, File file) throws TaskIOException {
        try {
            InputStream is = new FileInputStream(file);
            read(tasks, is);
        } catch (FileNotFoundException e) {
            throw new TaskIOException("FileNotFoundException in readBinary(TaskList tasks, File file)", e);
        }
    }
    public static void write(TaskList tasks, Writer out) throws TaskIOException {
        Iterator it = tasks.iterator();
        String s;
        while (it.hasNext()) {
            Task help =  (Task) it.next();
            if(help.getStartTime()==null) help.setTime(new Date(), new Date(), 0);
            if (!help.isRepeated()) {
                s = ("\"" + help.getTitle().replaceAll("\"", "\"\"") + "\" at " + formatForDate.format(help.getStartTime()));
            } else {
                s = ("\"" + help.getTitle().replaceAll("\"", "\"\"") + "\" from"
                        + formatForDate.format(help.getStartTime())) + " to "
                        + formatForDate.format(help.getEndTime()) + " every "
                        + interval(help.getRepeatInterval());
            }
            if (!help.isActive()) {
                s = s + " inactive";
            }
            try {
                if (it.hasNext()) {
                    out.write(s + ";\n");
                } else {
                    out.write(s + ".");
                }
            } catch (IOException e) {
                throw new TaskIOException("IOException in write(TaskList tasks, Writer out) method, try to write", e);
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            throw new TaskIOException("IOException in write(TaskList tasks, Writer out) method, try to close", e);
        }
    }

    /**
     * Статический метод превращает интервал в секундах - в строчную запись в виде 1 day 10 hour 17 minute 36 second
     * @param i интервал в секундах
     * @return строку
     */
    public static String interval(int i) {
        StringBuilder st = new StringBuilder();
        if (i / 86400 > 0) {
            st.append((i / 86400) + " day ");
        }
        i = i - (i / 86400) * 86400;
        if (i / 3600 > 0) {
            st.append((i / 3600) + " hour ");
        }
        i = i - (i / 3600) * 3600;
        if (i / 60 > 0) {
            st.append((i / 60) + " minute ");
        }
        i = i - (i / 60) * 60;
        st.append((i) + " second");
        return st.toString();
    }

    /**
     * шаблон для поиска названия задачи
     */
    private static final String titl_pattern = "^\"([\\S\\s^\"]+)\"";
    /**
     * шаблон для поиска даты
     */
    private static final String date_pattern = "\\[[\\d.:\\-\\s]+\\]";
    /**
     * шаблон для поиска активности/неактивности задачи
     */
    private static final String inactive_pattern = "inactive";
    public static void read(TaskList tasks, Reader in) throws TaskIOException {
        tasks.clear();
        StringBuffer s2;
        Pattern p;
        Matcher m;
        try (Reader mIn = in) {
            s2 = new StringBuffer();
            CharBuffer buf = CharBuffer.allocate(1000);
            int mychar = mIn.read();
            while (mychar != -1) {
                s2.append((char) mychar);
                mychar = mIn.read();
            }
            s2.append(buf + "\n");
            String[] mas = s2.toString().split("\n");
            for (String st : mas) {
                Task help = new Task();
                p = Pattern.compile(titl_pattern);
                m = p.matcher(st);
                String find;
                Date start;
                Date end;
                if (m.find()) {
                    find = m.group().replaceAll("\"\"", "\"");
                    help.setTitle(find.substring(1, find.length() - 1));
                } // add title
                p = Pattern.compile(date_pattern);
                m = p.matcher(st);
                if (m.find()) {
                    find = m.group();
                    start = formatForDate.parse(find);
                    end = (Date) start.clone();
                    m = p.matcher(st.substring(m.end()));
                    if (m.find()) {
                        find = m.group();
                        end = formatForDate.parse(find);
                    }
                    help.setTime(start, end, parse_interval(st));
                }
                p = Pattern.compile(inactive_pattern);
                m = p.matcher(st);
                if (!m.find()) {
                    help.setActive(true);
                }
                tasks.add(help);
            }
            if(tasks.getTask(0).getTitle()==null) {
                tasks.clear();
            }
        } catch (IOException e) {
            throw new TaskIOException("IOException in read(TaskList tasks, Reader in)", e);
        } catch (ParseException e) {
            throw new TaskIOException("ParseException in read(TaskList tasks, Reader in)", e);
        }
    }

    /**
     * переводит со строки в инт значение интервал в секундах
     * @param s - строка интервала в виде 1 day 10 hour 17 minute 36 second
     * @return значение интервала в секундах
     */
    public static int parse_interval(String s) {
        Pattern p = Pattern.compile("((\\d)+)\\sday");
        Matcher m =  p.matcher(s);
        String find;
        int interval = 0;
        if (m.find()) {
            find = m.group();
            interval += Integer.parseInt(find.substring(0, find.length() - 4)) * 86400;
        }
        p  = Pattern.compile("((\\d)+)\\shour");
        m =  p.matcher(s);
        if (m.find()) {
            find = m.group();
            interval += Integer.parseInt(find.substring(0, find.length() - 5)) * 3600;
        }
        p  = Pattern.compile("((\\d)+)\\sminute");
        m =  p.matcher(s);
        if (m.find()) {
            find = m.group();
            interval += Integer.parseInt(find.substring(0, find.length() - 7)) * 60;
        }
        p  = Pattern.compile("((\\d)+)\\ssecond");
        m =  p.matcher(s);
        if (m.find()) {
            find = m.group();
            interval += Integer.parseInt(find.substring(0, find.length() - 7));
        }
        return interval;
    }
    public static  void writeText(TaskList tasks, File file) throws TaskIOException {
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(file));
            write(tasks, w);
        } catch (FileNotFoundException e) {
            throw new TaskIOException("FileNotFoundException in writeText(TaskList tasks, File file)", e);
        }

    }
    public static void readText(TaskList tasks, File file) throws TaskIOException {
        try {
            Reader r = new InputStreamReader(new FileInputStream(file));
            read(tasks, r);
        } catch (FileNotFoundException e) {
            throw new TaskIOException("FileNotFoundException in readText(TaskList tasks, File file)", e);
        }
    }

}

