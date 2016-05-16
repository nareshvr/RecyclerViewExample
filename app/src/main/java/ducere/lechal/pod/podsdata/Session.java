package ducere.lechal.pod.podsdata;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by sunde on 09-05-2016.
 */
public class Session implements Serializable{
    int id;
    String name;
    int activityType;
    int goalType;
    int goalValue;
    int milestoneFreq;
    int steps;
    int calories;
    int time;
    int distance;
    int status;
    boolean sync;
    long startTime;
    long endTime;

    public Session(){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getActivityType() {
        return activityType;
    }

    public int getGoalType() {
        return goalType;
    }

    public int getGoalValue() {
        return goalValue;
    }

    public int getMilestoneFreq() {
        return milestoneFreq;
    }

    public int getSteps() {
        return steps;
    }

    public int getCalories() {
        return calories;
    }

    public int getTime() {
        return time;
    }

    public int getDistance() {
        return distance;
    }

    public int getStatus() {
        return status;
    }

    public boolean isSync() {
        return sync;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public void setGoalType(int goalType) {
        this.goalType = goalType;
    }

    public void setGoalValue(int goalValue) {
        this.goalValue = goalValue;
    }

    public void setMilestoneFreq(int milestoneFreq) {
        this.milestoneFreq = milestoneFreq;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
