Notes:
  
API is written from rewards application stand point.
DB Schema contains Customer and CustomerTransaction. Customer table was added to be realistic and complete, but not needed in current API requirements.
H2 database used with some initial data.
	
APIs:
curl localhost:8090/rewards?months=3  /*API for reward points for all customers for a given number of months, e.g., for past 3 months in this case */
curl localhost:8090/rewards/14?months=3 --> API for reward points for a customer for a given number of months, e.g., customer 14 and 3 months.
response JSON:
      {
        "customerId": 14, /* Not returned for All customers API */
        "rewardPoints": 0
       }
Aditional customer and customertransaction APIs are written but were not necessary.
	
Artifacts:
rewards-1.0.jar
run with java -jar rewards-1.0.jar from the directory it is placed in.
