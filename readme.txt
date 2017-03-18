Simple, no thrills microservice cluster.  Design is such as to demonstrate features, technologies.

1. Configuration Service - supplies ALL microservice configuration using multiple git repositories (one for each microservice)
2. Front end Aggregation Service - has one api called by the user, /notify/customer/{customerid}.  This service validates the customer
   with the Customer Locate service, then calls the FireAndForget Service.
3. Customer Locate service, /customer/{customerid} finds the customer and returns a Customer (JSON) object
4. FireAndForget service sends the Customer object as a JSON string to a RabbitMQ queue, CustomerQ, asynchronously.
5. The Messaging service receives the message (Customer) and then makes synchronous calls to the Email Service and an Event Notify Service