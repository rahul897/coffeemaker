package com.coffee;

import com.coffee.bo.CoffeeMachineBO;
import com.coffee.model.Beverage;

import java.util.concurrent.Callable;

public class Task implements Callable<String>
{
    private CoffeeMachineBO coffeeMachineBO;
    private Beverage beverage;

    public Task(CoffeeMachineBO coffeeMachineBO,Beverage beverage)
    {
        this.coffeeMachineBO = coffeeMachineBO;
        this.beverage = beverage;
    }

    @Override
    public String call() throws Exception {
        return this.coffeeMachineBO.prepare(this.beverage);

    }
}
