package com.coffee.service;

import com.coffee.dao.IInventoryRepository;
import com.coffee.model.Beverage;
import com.coffee.model.Inventory;

public class CoffeeMachineService {

    private IInventoryRepository inventoryRepository;
    public CoffeeMachineService(IInventoryRepository iInventoryRepository){
        this.inventoryRepository = iInventoryRepository;
    }

    public String prepare(Beverage beverage){
        String resp = this.inventoryRepository.checkAndDeduct(beverage.getIngredients());
        return beverage.getName()+resp;
    }

    public void addInventory(Inventory inventory){
        this.inventoryRepository.addOrUpdate(inventory);
    }
}
