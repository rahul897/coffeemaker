package com.coffee;

import com.coffee.model.Beverage;
import com.coffee.model.Inventory;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Utility {
    private static final Logger LOGGER = Logger.getLogger(Utility.class.getName());

    public static List<Beverage> getBeverages(LinkedTreeMap ltm){
        List<Beverage> beverages = new ArrayList<>();
        try{
            ltm = (LinkedTreeMap) ltm.get("machine");
            ltm = (LinkedTreeMap) ltm.get("beverages");
            for(Object entry : ltm.entrySet()){
                String name = (String) ((Map.Entry)entry).getKey();
                List<Inventory> inventoryList = new ArrayList<>();
                Map<String,Double> val = (Map<String, Double>) ((Map.Entry) entry).getValue();
                for(Map.Entry<String,Double> inv:val.entrySet()){
                    inventoryList.add(new Inventory(inv.getKey(),inv.getValue().intValue()));
                }
                Beverage beverage = new Beverage(name,inventoryList);
                beverages.add(beverage);
            }
        }catch (Exception e){
            LOGGER.warning(e.getMessage());
        }

        return beverages;
    }

    public static List<Inventory> getInventoryList(LinkedTreeMap ltm) {
        List<Inventory> inventoryList = new ArrayList<>();
        try {
            ltm = (LinkedTreeMap) ltm.get("machine");
            ltm = (LinkedTreeMap) ltm.get("total_items_quantity");
            for(Object entry: ltm.entrySet()){
                inventoryList.add(new Inventory((String) ((Map.Entry)entry).getKey(),((Double) ((Map.Entry)entry).getValue()).intValue()));
            }
        }catch (Exception e){
            LOGGER.warning(e.getMessage());
        }
        return inventoryList;
    }

    public static Integer getMaxBeverages(LinkedTreeMap ltm) {
        try {
            ltm = (LinkedTreeMap) ltm.get("machine");
            ltm = (LinkedTreeMap) ltm.get("outlets");
            return ((Double)ltm.get("count_n")).intValue();
        }catch (Exception e){
            LOGGER.warning(e.getMessage());
        }
        return 0;
    }
}
