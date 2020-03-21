Welcome to Stonehedge
---
Stonehedge is an attempt to learn about financial exchanges by creating one. The purpose is to have 3 main components:
   1. A working exchange -- a place where orders are accepted, matched, and responses are returned
   2. A client which can place those orders
   3. An interface so one can visually see what the inner workings of the exchange
---
This is a Spring Boot project run with Gradle. Right now there are 2 subprojects:
   1. Stonehedge_Interface: The visual element of the exchange
   2. Stonehedge_Service: The exchange itself -- the logic.
In the future, we want to have a third subproject:
   3. Stonehedge_Client: A client application which can submit exchange orders
---
Since you:
   * Buy at the Ask
   * Sell at the Bid
    
So your:
   * [Sell Order] will be in the Asks
   * [Buy order] will be in the Bids
   
   
Right now the rules are:
   * You can buy or sell stock
   * All orders are market orders. This entails certain rules:
      * It will disregard the price inputted and only find the *best* price
         * This means for a buy, the lowest available. And for a sell, the highest available
         * Right now, the "price" input on an orderRequest is meant to lookup in the bid/ask books
         * Of course, when using the client, a price will be sent through. This will be the current market price of the underlier
            * The logic behind generating the current market price of a stock needs to be implemented
   * You can have a full, partial, or empty completion of your order
      * A full and partial fill status is a SUCCESS
      * An empty fill status is a FAILURE
   * The orders are handled on a first come, first serve basis
      * Therefore, this means that the "best bids/asks" will always be available to the fastest orders
         * This is because the fastest orders will have the highest amount of other orders to fill from