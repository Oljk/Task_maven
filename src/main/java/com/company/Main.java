package com.company;

import com.company.controller.TaskController;
import com.company.view.*;
import java.io.File;

/**
 * Main class of the program with main method
 * @author olga
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        File dan = new File("dan.txt");
        TaskView view = new TaskView();
        TaskController controller = new TaskController(view, dan);
    }
}
