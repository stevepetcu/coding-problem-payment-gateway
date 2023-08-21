# Payment Gateway API

TODO: A lot of the README is "To be continued…"; for example, the diagrams mentioned below need to be added:

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

- Java 17
- Docker running

### 1.2 Running the tests locally

#### 1.2.1 Unit and integration tests

Docker is required to run the local suite of integration tests. The app uses Test Containers to bootstrap
a local database with the purpose of running the integration tests.

> Make sure Docker is running on your machine before attempting to run the suite of tests locally.

To run the tests, in a terminal window, navigate to the directory containing this README file, and execute:
```commandline
./gradlew test
```

#### 1.2.1 End-to-end tests

TODO: Add end-to-end tests.

### 1.3 Running the application locally with the included bank simulator

See the [README file](../README.md#1-running-the-services-on-your-local-machine) included in the root of this project.

#### 1.3.1 SwaggerUI

TODO: Add SwaggerUI.

#### 1.3.1 Postman

For manual exploratory testing, a [Postman collection](./docs/postman) is included in this repository.

> The Postman collection is a living doc, and it must be kept up-to-date.

## 2. Assumptions made for the exercise

- We're implementing payouts for merchants; we already capture and store the merchant's bank account, 
and it's linked to their client id.
- We can wire the money from the cardholder's bank account into the merchant's based on the above and by
getting their client id e.g., from a JWT auth token.
- Hence, none of the payee's bank account information is captured for this exercise.

For more notes, see [3. Areas for improvement](#3-areas-for-improvement)

## 3. Areas for improvement

> Note: many of the tactical areas for improvement are documented as TODOs in the code.

### 3.1 Local development

- Report test coverage to get an idea of the areas that need improvement
- Implement code style checking/formatting, e.g., as part of running the tests

### 3.2 Production

#### 3.2.1 Topics

3.2.1.1 Uptime

Uptime = % of time that the service is usable by end-users for its intended purpose.

Assuming we deploy using a CI/CD pipeline:
- Implement a strong CI pipeline testing strategy, covering all levels of testing using a swiss-cheese model:
  - Unit
  - Integration
  - End-to-end in an integration environment that is as close to production as possible
  - Also run static code and security analysis + dependency vulnerability analysis before deploying a new image to any env.
- Implement rolling updates
- Implement Blue/Green deployments with smoke tests
- Implement synthetic tests
- Implement contract tests at domain boundaries
- Use exploratory testing to find any issues left undiscovered
- Use chaos testing to test how robust and failure-tolerant the services are
- Implement an adequate level of observability and alerting + on-call practices
- Implement ways to handle thundering herds and cache stampedes etc.
- TODO: to be continued…

3.2.1.2 Latency

Latency = how quickly the service responds to requests.

- Run load tests as part of the CI pipeline
- Scale services horizontally where possible
- Implement caching at different levels (e.g., client-side, reverse-proxy-side, cache stores, application cache etc.);
avoid running into metastable failures caused by incorrect cache practices.
- Track service loads over time to predict load levels
- TODO: to be continued…

3.2.1.3 Scale

Scale = the amount and variety of workloads that the service can handle before latency and uptime start to degrade.

- Implement asynchronous handling of work where possible
- TODO: to be continued…

3.2.1.4 Velocity

Velocity = how fast new features can be added to the service.

- Team topologies
- Reverse Conway's law
- DORA practices + Agile practices
- TODO: to be continued…

3.2.1.5 Privacy and Security

- Ensure data security (there's a lot to unpack here)
- Build systems that conform to GDPR standards (golden standard)
- Ensure users can get their data removed upon request, in a timely manner

For example:
- The database must be encrypted at rest
- The card details table (`cards`) could be further encrypted – e.g., using client-side encryption.
See https://www.postgresql.org/docs/current/encryption-options.html

### 3.2.2 Strategic/system-level design

- Reconciliation API

### 3.2.3 Tactical/application-level design

- Ensure idempotency
- Improve validation
- Improve test automation

### 3.2.4 Test automation

### 3.2.5 API improvements

- Add support for more types of payment
- Add support for processing payments asynchronously
- Improve validation error messages
- Improve error case handling (e.g., bank timeouts or errors when contacting the bank to initiate the payment)
- Implement authentication

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
