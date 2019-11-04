### About the project

This project provides RESTful API for money transfers between bank accounts.

### Running the server

Execute `./gradlew runServer` command. It starts the server on `http://localhost:4567` address.
 
This command assembles a .jar archive and runs the application.

### Implementation details

[Spark](http://sparkjava.com/) is used as a web framework, Gson library is used to facilitate working with JSON.

[REST-assured](https://github.com/rest-assured/rest-assured) was employed for automated testing of the REST API.

A naive in-memory repository is used for persistence, so application data will be lost after server shutdown.

### What you can do with the REST API

You can create users and open accounts for them. A user can have multiple accounts in different currencies.

The API allows to transfer money between accounts. These accounts should operate in the same currency, e.g. to send
five dollars, origin and destination accounts should both have balance in dollars. Money exchange and inter-currency
payments were not implemented.

### API reference and examples

Note: HATEOAS was omitted since it doesn't have much sense for the implemented simplistic API.

#### Users 
First of all, you need create a user:

```curl -X POST --data '{"firstName": "John", "lastName": "Doe"}' http://localhost:4567/users```

Response:
```
{
  "firstName": "John",
  "lastName": "Doe",
  "id": "4ce4d5af-5b72-4f62-aebd-23f10999e0de"
}
```

You can also GET all users (```curl -X GET  http://localhost:4567/users```)
or GET a user by an ID (```curl -X GET  http://localhost:4567/users/{userId}```)

### Accounts
To perform money transfers, you need to open two more accounts:

```curl -XPOST --data '{"currency": "USD", "initialAmount": "100.00", "accountType": "DebitAccount"}' http://localhost:4567/users/{userId}/accounts```

Response:
```shell script
{
  "ownerId": "4ce4d5af-5b72-4f62-aebd-23f10999e0de",
  "accountType": "DebitAccount",
  "balance": {
    "amount": "100.00",
    "currency": "USD"
  },
  "id": "3720e302-e476-49af-b487-1040df9a8fcf"
}
```

You can also GET all accounts of a user (```curl -X GET http://localhost:4567/users/{userId}/accounts```)
or GET a specific account of a user (```curl -X GET http://localhost:4567/users/{userId}/accounts/{accountId}```)

### Payments 
To perform money transfer between accounts within the same currency:

```
curl -X POST --data '{"senderAccountId": "d7204a0b-7128-4ce6-86bc-cd05925a5088",
    "recipientAccountId": "002d0f6b-088f-48b3-ac7e-6b4a49326313",
    "instructedAmount": { "amount": "5.00", "currency": "USD" }}'\
    http://localhost:4567/domestic-payments
``` 

Response:
```
{
  "senderAccountId": "d7204a0b-7128-4ce6-86bc-cd05925a5088",
  "recipientAccountId": "002d0f6b-088f-48b3-ac7e-6b4a49326313",
  "instructedAmount": {
    "amount": "5.00",
    "currency": "USD"
  },
  "id": "2408bc0e-6761-4b12-8f81-674c5441e574"
}
```

Then you can check account balances using account resource.