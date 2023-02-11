package com.coffee;

import java.util.HashMap;
import java.util.Map;

//model db having only inventorytable

public class DB {
    private Map<String ,Integer> inventoryTable = new HashMap<>();

    private static DB single_instance = null;

    public static DB getInstance()
    {
        if (single_instance == null)
            single_instance = new DB();

        return single_instance;
    }

    /*
    using synchronized to allow only one thread to modify from table
    Can be assumed as table wide locking instead of row wise locking(InnoDB uses table wide locking when deleting/updating)
     */
    public void addOrUpdate(String name,Integer quantity){
        synchronized (inventoryTable) {
            inventoryTable.put(name, quantity);
        }
    }

    public void delete(String name,Integer quantity){
        synchronized (inventoryTable) {
            inventoryTable.remove(name);
        }
    }

    public void clear() {
        synchronized (inventoryTable){
            inventoryTable.clear();
        }
    }
    /*
    ideally the below code must be part of BO,
    for convinience and representaion (it can be assumed as stored procedure in db)
    read and write happen in one single transaction
     */
    public String checkAndDeduct(Map<String ,Integer> list){
        synchronized (inventoryTable){
            Boolean satisfied = true;
            Boolean present = true;
            String current = "";
            for(Map.Entry<String,Integer> entry:list.entrySet()){
                current = entry.getKey();
                int deduct = entry.getValue();
                int cur = inventoryTable.getOrDefault(current,-1);
                if(cur==-1){
                    present = false;
                    break;
                }
                if(cur-deduct<0){
                    satisfied = false;
                    break;
                }
            }
            /*
            As of now the code just breaks at the first missing criteria
            */
            if(!satisfied){
                return " cannot be prepared because item "+current+" is not sufficient";
            }
            if(!present){
                return " cannot be prepared because "+current+" is not available";
            }
            for(Map.Entry<String,Integer> entry:list.entrySet()){
                current = entry.getKey();
                int deduct = entry.getValue();
                int cur = inventoryTable.getOrDefault(current,-1);
                inventoryTable.put(current,cur-deduct);
            }
            return " is prepared";
        }
    }
}
