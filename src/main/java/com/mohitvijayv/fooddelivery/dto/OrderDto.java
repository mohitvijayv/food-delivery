package com.mohitvijayv.fooddelivery.dto;

import java.util.List;

public class OrderDto {
    private Integer orderId;
    private List<String> meals;
    private float distance;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(final Integer orderId) {
        this.orderId = orderId;
    }

    public List<String> getMeals() {
        return meals;
    }

    public void setMeals(final List<String> meals) {
        this.meals = meals;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(final float distance) {
        this.distance = distance;
    }
}
