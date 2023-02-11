import com.coffee.DB;
import com.coffee.Task;
import com.coffee.Utility;
import com.coffee.bo.CoffeeMachineBO;
import com.coffee.dao.InventoryRepository;
import com.coffee.model.Beverage;
import com.coffee.model.Inventory;
import com.coffee.service.CoffeeMachineService;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
    }

    @After
    public void cleanUp() {
        DB connection = DB.getInstance();
        connection.clear();
    }

    @Test
    public void prepareMachineAndBeverage() throws Exception {
        DB connection = DB.getInstance();
        InventoryRepository inventoryRepository = new InventoryRepository(connection);
        CoffeeMachineService coffeeMachineService = new CoffeeMachineService(inventoryRepository);
        CoffeeMachineBO coffeeMachineBO = new CoffeeMachineBO(coffeeMachineService);
        String input = "{\"machine\":{\"beverages\":{\"black_tea\":{\"ginger_syrup\":30,\"hot_water\":300,\"sugar_syrup\":50,\"tea_leaves_syrup\":30},\"green_tea\":{\"ginger_syrup\":30,\"green_mixture\":30,\"hot_water\":100,\"sugar_syrup\":50},\"hot_coffee\":{\"ginger_syrup\":30,\"hot_milk\":400,\"hot_water\":100,\"sugar_syrup\":50,\"tea_leaves_syrup\":30},\"hot_tea\":{\"ginger_syrup\":10,\"hot_milk\":100,\"hot_water\":200,\"sugar_syrup\":10,\"tea_leaves_syrup\":30}},\"outlets\":{\"count_n\":3},\"total_items_quantity\":{\"ginger_syrup\":100,\"hot_milk\":500,\"hot_water\":500,\"sugar_syrup\":100,\"tea_leaves_syrup\":100}}}";
        LinkedTreeMap ltm = new Gson().fromJson(input, LinkedTreeMap.class);
        Integer maxBeverages = Utility.getMaxBeverages(ltm);
        List<Inventory> inventoryList = Utility.getInventoryList(ltm);
        List<Beverage> beverages = Utility.getBeverages(ltm);
        List<Task> taskList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            coffeeMachineBO.add(inventory);
        }
        ExecutorService pool = Executors.newFixedThreadPool(maxBeverages);

        for(Beverage beverage:beverages)
            taskList.add(new Task(coffeeMachineBO,beverage));
        List<Future<String>> futures = pool.invokeAll(taskList);
        List<String> results = new ArrayList<>();
        for(Future<String> future:futures)
            results.add(future.get());
        pool.shutdown();
        String expectedResult = "black_tea is prepared, green_tea cannot be prepared because green_mixture is not available, hot_coffee is prepared, hot_tea cannot be prepared because item sugar_syrup is not sufficient";
        Collections.sort(results);
        String result = results.stream().collect(Collectors.joining(", "));
        System.out.println("___basic input test___");
        System.out.println(result);
        assertEquals(expectedResult,result);
    }

    @Test
    public void lowOnInventory() throws Exception {
        DB connection = DB.getInstance();
        InventoryRepository inventoryRepository = new InventoryRepository(connection);
        CoffeeMachineService coffeeMachineService = new CoffeeMachineService(inventoryRepository);
        CoffeeMachineBO coffeeMachineBO = new CoffeeMachineBO(coffeeMachineService);
        String input = "{\"machine\":{\"outlets\":{\"count_n\":3},\"total_items_quantity\":{\"hot_water\":500,\"hot_milk\":500,\"ginger_syrup\":100,\"sugar_syrup\":100,\"tea_leaves_syrup\":100},\"beverages\":{\"hot_tea\":{\"hot_water\":1000,\"hot_milk\":100,\"ginger_syrup\":10,\"sugar_syrup\":10,\"tea_leaves_syrup\":30}}}}";
        LinkedTreeMap ltm = new Gson().fromJson(input, LinkedTreeMap.class);
        Integer maxBeverages = Utility.getMaxBeverages(ltm);
        List<Inventory> inventoryList = Utility.getInventoryList(ltm);
        List<Beverage> beverages = Utility.getBeverages(ltm);
        List<Task> taskList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            coffeeMachineBO.add(inventory);
        }
        ExecutorService pool = Executors.newFixedThreadPool(maxBeverages);

        for(Beverage beverage:beverages)
            taskList.add(new Task(coffeeMachineBO,beverage));
        List<Future<String>> futures = pool.invokeAll(taskList);
        List<String> results = new ArrayList<>();
        for(Future<String> future:futures)
            results.add(future.get());
        pool.shutdown();
        String expectedResult = "hot_tea cannot be prepared because item hot_water is not sufficient";
        Collections.sort(results);
        String result = results.stream().collect(Collectors.joining(", "));
        System.out.println("____low stock test____");
        System.out.println(result);
        assertEquals(expectedResult,result);
    }

    @Test
    public void noItemPresent() throws Exception {
        DB connection = DB.getInstance();
        InventoryRepository inventoryRepository = new InventoryRepository(connection);
        CoffeeMachineService coffeeMachineService = new CoffeeMachineService(inventoryRepository);
        CoffeeMachineBO coffeeMachineBO = new CoffeeMachineBO(coffeeMachineService);
        String input = "{\"machine\":{\"beverages\":{\"hot_coffee\":{\"ginger_juice\":10,\"hot_milk\":100,\"hot_water\":10,\"sugar_syrup\":10,\"tea_leaves_syrup\":30}},\"outlets\":{\"count_n\":3},\"total_items_quantity\":{\"ginger_syrup\":100,\"hot_milk\":500,\"hot_water\":500,\"sugar_juice\":100,\"tea_leaves_syrup\":100}}}";
        LinkedTreeMap ltm = new Gson().fromJson(input, LinkedTreeMap.class);
        Integer maxBeverages = Utility.getMaxBeverages(ltm);
        List<Inventory> inventoryList = Utility.getInventoryList(ltm);
        List<Beverage> beverages = Utility.getBeverages(ltm);
        List<Task> taskList = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            coffeeMachineBO.add(inventory);
        }
        ExecutorService pool = Executors.newFixedThreadPool(maxBeverages);

        for(Beverage beverage:beverages)
            taskList.add(new Task(coffeeMachineBO,beverage));
        List<Future<String>> futures = pool.invokeAll(taskList);
        List<String> results = new ArrayList<>();
        for(Future<String> future:futures)
            results.add(future.get());
        pool.shutdown();
        String expectedResult = "hot_coffee cannot be prepared because sugar_syrup is not available";
        Collections.sort(results);
        String result = results.stream().collect(Collectors.joining(", "));
        System.out.println("____no item present test____");
        System.out.println(result);
        assertEquals(expectedResult,result);
    }

    @Test
    public void maxBeverageNotGreaterThanZero() throws Exception {
        String input = "{\"machine\":{\"outlets\":{\"count_n\":1},\"total_items_quantity\":{\"hot_water\":500,\"hot_milk\":500,\"ginger_syrup\":100,\"sugar_syrup\":100,\"tea_leaves_syrup\":100},\"beverages\":{\"hot_tea\":{\"hot_water\":1000,\"hot_milk\":100,\"ginger_juice\":10,\"sugar_syrup\":10,\"tea_leaves_syrup\":30}}}}";
        LinkedTreeMap ltm = new Gson().fromJson(input, LinkedTreeMap.class);
        Integer maxBeverages = Utility.getMaxBeverages(ltm);
        System.out.println("_____max beverage not greater than zero_____");
        assertTrue(maxBeverages>0);
    }
}