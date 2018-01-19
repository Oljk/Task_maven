package com.company;

import com.company.controller.TaskController;
import com.company.controller.TaskThread;
import com.company.model.*;
import com.company.view.*;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/*
* джавадоки
* батник
* изменить class diagram
 */
public class Main {

    public static void main(String[] args) {
        File dan = new File("dan.txt");

        TaskView view = new TaskView();
        TaskController controller = new TaskController(view, dan);
        controller.menu();
    }
}
