#Dunzo Coffee Machine

##setup
mvn clean install

##How to run:
mvn exec:java

(Please provide minified json(single line))
sample input
{"machine":{"beverages":{"black_tea":{"ginger_syrup":30,"hot_water":300,"sugar_syrup":50,"tea_leaves_syrup":30},"green_tea":{"ginger_syrup":30,"green_mixture":30,"hot_water":100,"sugar_syrup":50},"hot_coffee":{"ginger_syrup":30,"hot_milk":400,"hot_water":100,"sugar_syrup":50,"tea_leaves_syrup":30},"hot_tea":{"ginger_syrup":10,"hot_milk":100,"hot_water":200,"sugar_syrup":10,"tea_leaves_syrup":30}},"outlets":{"count_n":3},"total_items_quantity":{"ginger_syrup":100,"hot_milk":500,"hot_water":500,"sugar_syrup":100,"tea_leaves_syrup":100}}}

##How to test:
mvn test
Contains 4 funcitonal testcase
- basic/provided input test
- low stock present
- no stock present
- no.of max Beverages must > 0