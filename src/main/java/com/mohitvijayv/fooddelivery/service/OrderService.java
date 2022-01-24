package com.mohitvijayv.fooddelivery.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.mohitvijayv.fooddelivery.common.Pair;
import com.mohitvijayv.fooddelivery.dto.OrderDto;
import org.springframework.stereotype.Service;

import static com.mohitvijayv.fooddelivery.common.Constants.COOKING_SLOTS;
import static com.mohitvijayv.fooddelivery.common.Constants.COOKING_SLOTS_REQUIRED_FOR_APPETIZER;
import static com.mohitvijayv.fooddelivery.common.Constants.COOKING_SLOTS_REQUIRED_FOR_MAIN_COURSE;
import static com.mohitvijayv.fooddelivery.common.Constants.COOKING_TIME_APPETIZER_IN_MINS;
import static com.mohitvijayv.fooddelivery.common.Constants.COOKING_TIME_MAIN_COURSE_IN_MINS;
import static com.mohitvijayv.fooddelivery.common.Constants.DELIVERY_TIME_PER_KM;
import static com.mohitvijayv.fooddelivery.common.Constants.MAX_PERMISSIBLE_ETA_IN_MINS;

@Service
public class OrderService {

    public Pair<Integer, Integer> getCountOfTypeOfMeals(OrderDto order){
        int noOfAppetizer=0;
        int noOfMainCourse=0;
        for(int j=0;j<order.getMeals().size();j++){
            if(order.getMeals().get(j).equals("A"))
                noOfAppetizer++;
            else{
                noOfMainCourse++;
            }
        }
        return new Pair<>(noOfAppetizer, noOfMainCourse);
    }

    public Integer getTotalSlotsNeeded(Pair<Integer, Integer> typeCount){
        int noOfAppetizer= typeCount.getKey();
        int noOfMainCourse= typeCount.getValue();
        return noOfAppetizer*COOKING_SLOTS_REQUIRED_FOR_APPETIZER+noOfMainCourse*COOKING_SLOTS_REQUIRED_FOR_MAIN_COURSE;
    }

    public float getCookingTime(Pair<Integer, Integer> typeCount){
        int noOfMainCourse= typeCount.getValue();
        float cookingTime=COOKING_TIME_APPETIZER_IN_MINS;
        if(noOfMainCourse>0)    cookingTime=COOKING_TIME_MAIN_COURSE_IN_MINS;
        return cookingTime;
    }

    public float getWaitingTime(float[] deliveryTimes, int totalSlotsNeeded){
        return deliveryTimes[totalSlotsNeeded-1];
    }

    public float getDeliveryTime(float distance){
        return distance*DELIVERY_TIME_PER_KM;
    }

    public float[] updateCookingSlots(float[] slotTimes, int totalSlotsNeeded, float etaOrder){
        for(int j=0;j<totalSlotsNeeded;j++){
            slotTimes[j]=etaOrder;
        }
        return slotTimes;
    }

    public String ignoreOrder(OrderDto order){
        return "Order " + order.getOrderId() +
            " is denied because the restaurant cannot accommodate it";
    }

    public String completeOrder(OrderDto order, float eta){
        return "Order " + order.getOrderId() +
            " will get delivered in " + String.format("%.1f", eta)+" minutes";
    }

    public List<String> getDeliveryTime(List<OrderDto> orders){
        float[] etaSlotTimes=new float[COOKING_SLOTS];
        Arrays.fill(etaSlotTimes,0);
        List<String> deliveryNotes=new ArrayList<>();

        for(int i=0;i<orders.size();i++){
            Pair<Integer, Integer> mealTypeCount = getCountOfTypeOfMeals(orders.get(i));

            int totalSlotsNeeded=getTotalSlotsNeeded(mealTypeCount);

            float etaOrder=0;

            if(totalSlotsNeeded>COOKING_SLOTS){
                deliveryNotes.add(ignoreOrder(orders.get(i)));
                continue;
            }

            etaOrder = getWaitingTime(etaSlotTimes, totalSlotsNeeded)+getCookingTime(mealTypeCount)+getDeliveryTime(orders.get(i).getDistance());

            if(etaOrder>MAX_PERMISSIBLE_ETA_IN_MINS){
                deliveryNotes.add(ignoreOrder(orders.get(i)));
                continue;
            }

            updateCookingSlots(etaSlotTimes, totalSlotsNeeded, etaOrder);
            Arrays.sort(etaSlotTimes);
            deliveryNotes.add(completeOrder(orders.get(i), etaOrder));
        }
        return deliveryNotes;
    }

}
