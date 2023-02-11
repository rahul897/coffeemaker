package com.coffee.dao;

import com.coffee.DB;
import com.coffee.model.Inventory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryRepository implements IInventoryRepository {

    private DB db;
    public InventoryRepository(DB db){
        this.db = db;
    }

    @Override
    public void addOrUpdate(Inventory inventory) {
        this.db.addOrUpdate(inventory.getName(),inventory.getQuantity());

    }

    @Override
    public String checkAndDeduct(List<Inventory> inventoryList) {
        Map<String,Integer> invMap = new HashMap<>();
        for(Inventory inv:inventoryList){
            invMap.put(inv.getName(),inv.getQuantity());
        }
        return this.db.checkAndDeduct(invMap);
    }
}
