## Running Application in Repl.it
Press the "Run" button

-- OR --

From the Terminal in repl.it, run:

```
mvn clean package

java -jar target/food-delivery-0.0.1-SNAPSHOT.jar
```

once the application is up and running, we can use api calls to fetch and get data from our app. 

simply in repl.it, terminal run "run.sh" file, it will read from input.json and will return the output.
```
sh run.sh
```

## Design Problem Solution

We will be using MySql db for this problem.

TABLE

futureOrders(userId, orderId, meals, distance, time, triggerType, recurPeriod)

Details:
- userId - Will store userId of the person who made future order. primary key.
- orderId - Same user can make more than one future orders.
- meals - Will contain meals he subscribed for. ['A' and 'M']
- distance - distance in km to deliver address.
- time - Time to trigger the next order. DD:MM:YYYY::HH:MM
- triggerType - can be either immediate or scheduled, to tell whether it is recurring order. [IMMEDIATE/SCHEDULED]
- recurPeriod - How often the order recurs. MIN:H:D:M:Y. subscribed every MIN minutely, H hourly, D daily, M monthly, Y yearly.

EXAMPLE:
1. {
   "userId": 1, "orderId": 5, "meals" : ['M', 'A'], "distance": 5, "time": 01:02:2022::13:00, "triggerType":"IMMEDIATE"}

This means user has future order of meals ['M', 'A'] at 1 PM 1st February 2022, and it is not recurring.

2. {
   "userId": 3, "orderId": 5, "meals" : ['M'], "distance": 6, "time": 01:22:2022::21:00, "triggerType":"SCHEDULED", "recurPeriod": 0:0:2:0:0}

This means user has future order of meals ['M'] at 9 PM 1st February 2022, and it is recurring every alternate day (once in 2 days).


A scheduler needs to be written on top of this Database table. Job of the scheduler is to find all the orders to be added to the order queue at any given minute. we can use cron scheduler in conjunction with springboot.

CRUD FLOW:

1. User using post request will send order.
2. update futureOrders tables with new order received.
3. scheduler will run every minute to find if there is any order to be added to queue.
4. scheduler will check current time and time column in futureOrders.
   a. if they match
    - add (orderId, meals, distance) to the order queue.
    - if order's recurring column is true.
        - then calculate when the next order will be based on the reccurPeriod.
        - update time column in the for that userId and orderId.
    - else
        - delete the row from the futureOrders.
          b. if they do not match
    - move to next row.
5. If the user updates a certain order using put request or deletes the order.
- find the userId, orderId which needs to updated and update it.
- if the entry is not there, then order is already in the queue and cannot be changed.

OPTIMIZATIONS:
Scheduler might have to run every minute on huge dataset. This could be achieved by using distributed systems.
Every core runs' scheduler functionality for a certain set of data. Bucketing this data would be good too. Load balancer will come into picture here.