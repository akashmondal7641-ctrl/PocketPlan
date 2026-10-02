package com.pocketplan.model;

import java.time.LocalDate;

public class SavingsGoal {

    private int id;
    private int userId;
    private String name;
    private double targetAmount;
    private double savedAmount;
    private LocalDate targetDate;

    public SavingsGoal() {
    }

    public SavingsGoal(int userId, String name,
                       double targetAmount,
                       double savedAmount,
                       LocalDate targetDate) {
        this.userId = userId;
        this.name = name;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.targetDate = targetDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public double getSavedAmount() {
        return savedAmount;
    }

    public void setSavedAmount(double savedAmount) {
        this.savedAmount = savedAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public double getProgress() {
        if (targetAmount <= 0) {
            return 0;
        }

        return (savedAmount / targetAmount) * 100;
    }
}
