package com.example.habitpadapplication.Model;

public class Task {

    private String taskID,taskImg,taskTitle,taskDescription,taskPoint;

    public Task(String taskID,String taskImg, String taskTitle, String taskDescription, String taskPoint) {
        this.taskID = taskID;
        this.taskImg = taskImg;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskPoint = taskPoint;

    }
    public String getTaskID() {
        return taskID;
    }

    public String getTaskImg() {
        return taskImg;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }



    public String getTaskPoint() {
        return taskPoint;
    }


}
