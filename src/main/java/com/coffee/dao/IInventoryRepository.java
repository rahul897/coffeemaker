package com.coffee.dao;

import com.coffee.model.Inventory;

import java.util.List;
//ORM abstraction for Inventory Class
public interface IInventoryRepository {
    void addOrUpdate(Inventory inventory);
    String checkAndDeduct(List<Inventory> inventoryList);
}
