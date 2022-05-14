package com.example.habitpadapplication.Model;

public class WorkoutRecent {

    private String workoutID,workoutType,workoutMET;

    public WorkoutRecent(String workoutID,String workoutType, String workoutMET ){
        this.workoutID = workoutID;
        this.workoutType = workoutType;
        this.workoutMET = workoutMET;
    }


    public String getWorkoutID() {
        return workoutID;
    }
    public String getWorkoutType() {
        return workoutType;
    }
    public String getworkoutMET() {return workoutMET;}
}
