package com.coffee;

import com.coffee.bo.CoffeeMachineBO;
import com.coffee.dao.InventoryRepository;
import com.coffee.model.Beverage;
import com.coffee.model.Inventory;
import com.coffee.service.CoffeeMachineService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        /*
        All this configuration is way to mimick Autowiring at start of application.
         */
        /*
        DB is assumed to be singleton reflecting single source of connection and having only one table invnetory
         */
        DB connection = DB.getInstance();
        /*
        Repository is kind of JPA abstraction/ORM
         */
        InventoryRepository inventoryRepository = new InventoryRepository(connection);
        CoffeeMachineService coffeeMachineService = new CoffeeMachineService(inventoryRepository);
        CoffeeMachineBO coffeeMachineBO = new CoffeeMachineBO(coffeeMachineService);

        System.out.println("Please provide input in one line");
        String input = sc.next();
        System.out.println("____________");

        /*
        json is not in proper format to fit POJO.
        So parsed in different way like a map
         */
        LinkedTreeMap ltm = new Gson().fromJson(input,LinkedTreeMap.class);
        Integer maxBeverages = Utility.getMaxBeverages(ltm);
        if(maxBeverages<=0){
            System.out.println(maxBeverages+" must be greater than 0");
            System.exit(0);
        }
        List<Inventory> inventoryList = Utility.getInventoryList(ltm);
        List<Beverage> beverages = Utility.getBeverages(ltm);
        for(Inventory inventory:inventoryList){
            coffeeMachineBO.add(inventory);
        }


        /*
        pool acts as application server routing requests
         */
        ExecutorService pool = Executors.newFixedThreadPool(maxBeverages);
        for(Beverage beverage:beverages)
        pool.submit(new Task(coffeeMachineBO,beverage));
        /*
        Subsequent beverages can be requests.
        not implemented to maintain input consistency
         */
        pool.shutdown();
    }
}
