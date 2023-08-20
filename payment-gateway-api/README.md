# Payment Gateway API

Further documentation in the form of diagrams can be found under the 
[/docs](./docs) folder:
- [C4 system-context-level diagrams](./docs/c4/c4-system-context.puml)
- [C4 container-level diagrams](./docs/c4/c4-containers.puml)
- [Payment initiation sequence diagram](./docs/sequence/sequence-payment-initiation.puml)
- [Payment details retrieval sequence diagram](./docs/sequence/sequence-payment-details-retrieval.puml)

> Notes:
> - [Introduction to the C4 model](https://c4model.com/)
> - The diagrams included in this repository are living documents written in
> [PlantUML](https://plantuml.com/). To visualise the diagrams:
>   - If using a JetBrains editor such as IntelliJ IDEA, install the [JetBrains PlantUML integration plugin](https://plugins.jetbrains.com/plugin/7017-plantuml-integration)
>   - If using Visual Studio Code, install the [Visual Studio Code PlantUML plugin](https://marketplace.visualstudio.com/items?itemName=jebbs.plantuml)
>   - If using the Eclipse IDE, see the [official PlantUML Eclipse plugin guide](https://plantuml.com/eclipse)
>   - Alternatively, using the [online PlantUML editor](https://www.planttext.com/), copy-paste the diagram source code into the editor

## 1. Running the tests/application on your local machine

### 1.1 Requirements

- Java
- Docker running

### 1.2 Running the tests locally

#### 1.2.1 Unit and integration tests

Docker is required to run the local suite of integration tests. The app uses Test Containers to bootstrap
a local database with the purpose of running the integration tests.

> Make sure Docker is running on your machine before attempting to run the suite of tests locally.

To run the tests, open a terminal window, navigate to the root of the `payment-gateway-api` application and 
execute the following command:
```commandline
./gradlew test
```

#### 1.2.1 End-to-end tests

### 1.3 Running the application locally with the included bank simulator

TODO: add a readme to the root-root of the project

Reference docker commands: https://docs.tibco.com/pub/mash-local/4.3.0/doc/html/docker/GUID-BD850566-5B79-4915-987E-430FC38DAAE4.html

See the [README.md](../README.md) included in the root of this project.

#### 1.3.1 SwaggerUI

#### 1.3.1 Postman

TODO: add instructions for using the [Postman collection](./docs/postman) included with the project.
TODO: add a random minor amount for each payment (see how I did that in my other project)
TODO: implement an end-to-end suite of tests in Postman

> The Postman collection is a living doc, and it must be kept up-to-date.

## 2. Assumptions made for the exercise

- We're implementing payouts for merchants.
- We already capture and store the merchant's bank account, and it's linked to their client id.
- We can wire the money from the cardholder's bank account into the merchant's based on the above + getting their client id e.g., in the auth token.

## 3. Areas for improvement

### 3.1 Local development

- Add test coverage

### 3.2 Production

#### 3.2.1 Topics

3.2.1.1 Uptime

3.2.1.2 Latency

3.2.1.3 Scale

3.2.1.4 Velocity

3.2.1.5 Privacy

- The database must be encrypted at rest
- The card details table (`cards`) must be encrypted – how? Is at rest enough? See https://www.postgresql.org/docs/current/encryption-options.html

### 3.2.2 Strategic/system-level design

- Reconciliation API

### 3.2.3 Tactical/application-level design

- Idempotency
- Database denormalisation

### 3.2.4 Test automation

### 3.2.5 API improvements

- Add support for more types of payment
- Add support for processing payments asynchronously
- Improve validation error messages
- Handle error cases (e.g., bank timeouts or errors when contacting the bank to initiate the payment)

### 3.4 Observability

- Add logging

#### 3.4.1 Topics

4 Golden Signals:

3.4.1.1 Latency

3.4.1.2 Traffic

3.4.1.3 Errors

3.4.1.4 Saturation

### 3.5 Deployment

#### 3.5.1 CI/CD

#### 3.5.2 Cloud technologies

## 4. Maintenance and contributions

### 4.1 Maintaining the code

#### 4.1.1 Application architecture and conventions

#### 4.1.2 Code style rules

### 4.2 Maintaining the docs
