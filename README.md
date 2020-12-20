# POC: Spring REST API Validations

It demonstrates how to implement endpoints to expose business logic validation rules.

The goal is to implement a REST API to create a bank account, find it and allow the owner to operate it (deposit and draw).

We also want an endpoint that returns all the validation errors that will occur when the draw operation is requested for a given account. For example, trying to draw money from a newly created account, therefore locked and without credit, should return the payload below.

```json
{
  "errors": [
    {
      "field": "locked",
      "message": "Account.State.isLocked",
      "metadata": []
    },
    {
      "field": "balance",
      "message": "Account.Operation.Draw.hasEnoughBalance",
      "metadata": []
    }
  ]
}
```

## How to run

| Description | Command |
| :--- | :--- |
| Run tests | `./gradlew test` |
| Run application | `./gradlew run` |
