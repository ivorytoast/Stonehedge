Submit Order

The steps are:
   1. A unique ID is generated and assigned to the new order
   2. If it is a buy order, it gets sent to the Bids of the underlier
   3. If it is a sell order, it gets sent to the Asks of the underlier
   
---

For each order:
   1. Determine the fill price
      1. Update the order fill amounts
      2. Update the bid/ask amounts
   2. Determine if it is a success or failure
      1. If it has gone through 5 cycles, it is always a failure
      2. It at least 1 share is filled, then it is successful
         3. If it is not full, update the Fill Status
   3. After each order gets processed, if any tarde has been bought or sold, that price becomes the current price of the underlier