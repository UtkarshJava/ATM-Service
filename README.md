atm-service
===============================

'atm-service' is back-end system for ATM machines created using restful API with below features.

1. Check balance: GET request to retrieve user balance using accountnumber and pin as input
2. Withdraw money: POST request to withdraw money from user account as accountnumber, pin and amount as input

Application uses Apache Derby embedded DB, for configuration. 
createschema.sql and insertdata.sql is used to create schema and setup accounts at startup. 


Running the Application
=========================
Please execute main method in Application.java to start the embedded tomcat server.

Application services
========================

Both the services first call ValidateCustomerAccount method to validate the accountnumber with pin entered.
When validation is successful, user can use this ATM application with below URLs:

1. Check balance: https://localhost:9011/atm/balanceEnquiry/{accountNumber}/{pin}, ex: https://localhost:9011/atm/balanceEnquiry/?accountNumber=123456789&pin=1234
2. Withdraw Money: https://localhost:9011/atm/withdrawMoney/{accountNumber}/{pin}/{amount}, ex: https://localhost:9011/atm/withdrawMoney?accountNumber=123456789&pin=1234&requestAmount=10


How to test
=====================

Use below REST url to get balance

https://localhost:9011/atm/balanceEnquiry

Use below json object

{
"accountNumber":"123456789", 
"pin":"1234"
}

Use Postman to execute post url, a screen shot is added to the project.

https://localhost:9011/atm/withdrawMoney

Use below json object

{
"accountNumber":"123456789", 
"pin":"1234",
"requestAmount":10
}



