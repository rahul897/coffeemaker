package com.coffee.bo;

import com.coffee.model.Beverage;
import com.coffee.model.Inventory;
import com.coffee.service.CoffeeMachineService;

public class CoffeeMachineBO {

    private CoffeeMachineService coffeeMachineService;
    public CoffeeMachineBO(CoffeeMachineService coffeeMachineService) {
        this.coffeeMachineService = coffeeMachineService;
    }

    public String prepare(Beverage beverage){
        String resp = this.coffeeMachineService.prepare(beverage);
        System.out.println(resp);
        return resp;
    }

    public void add(Inventory inventory){
        this.coffeeMachineService.addInventory(inventory);
    }
}
