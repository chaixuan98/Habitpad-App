package com.example.habitpadapplication.Model;

public class WorkoutLog {

    private String addWorkoutDetailID,workoutName,numberOfDuration,totalCalories,addWorkoutDate,addWorkoutTime;

    public WorkoutLog(String addWorkoutDetailID,String workoutName, String numberOfDuration,String totalCalories, String addWorkoutDate , String addWorkoutTime ){
        this.addWorkoutDetailID = addWorkoutDetailID;
        this.workoutName = workoutName;
        this.numberOfDuration = numberOfDuration;
        this.totalCalories = totalCalories;
        this.addWorkoutDate = addWorkoutDate;
        this.addWorkoutTime = addWorkoutTime;

    }

    public String getAddWorkoutDetailID() {
        return addWorkoutDetailID;
    }
    public String getWorkoutName() {
        return workoutName;
    }
    public String getNumberOfDuration() {
        return numberOfDuration;
    }
    public String getTotalCalories() {
        return totalCalories;
    }
    public String getAddWorkoutDate() {
        return addWorkoutDate;
    }
    public String getAddWorkoutTime() {return addWorkoutTime;}
}
