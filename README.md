# Payment Gateway

> Note: For a deeper dive into each application's specifics, see the [Sim Bank API README](./sim-bank-api/README.md), 
> and the [Payment Gateway API README](./payment-gateway-api/README.md) (which is the main README file for the exercise).

## 1. Running the services on your local machine

### 1.1 Requirements

- Docker
- (Optional) Postman

### 1.2 How to run the services locally

1. From a terminal window, navigate to the directory that contains this README file
2. Execute `docker-compose up`. The services may take a long time to start on the first run, 
depending on how many docker images need to be downloaded.
3. When logs stop streaming on the terminal, it hopefully means that all the docker containers
have started successfully.
   1. To check whether all the containers are running, run `docker ps`. There should be at least 3
    running containers, named `sim-bank`, `payment-gateway`, and `payment-database`.
4. Now that the service is up, we're ready to start making requests. 
The [payment-gateway-api/docs/postman folder](./payment-gateway-api/docs/postman) contains a Postman collection
which can be imported and used to demonstrate the API's capabilities.

> Notes:
> 1. The public Payments Gateway API will be exposed on port `8080`. If that port is unavailable, 
> free it up and restart this service; alternatively, edit the [docker-compose.yaml](./docker-compose.yaml) file
> and update line 15 as follows: `<preferred-port>:8080`, before restarting the service. The postman collection
> `port` variable will need to be updated as well.
> 2. The `sim-bank` and `payment-database` services should not be exposed to the local system, hence they cannot be
> "hit" directly. To experiment with the `sim-bank` locally, follow the instructions in that service's README file.
> 3. After the first run (which takes longer than subsequent runs, if docker images are kept locally), using 
> `docker-compose up -d` to start the containers as daemons might be preferable. Run `docker-compose stop` from the same
> directory where `docker-compose up -d` was run, to stop the containers.

#### 1.2.1 Debugging

Apart from the issues described in the previous Notes section, other issues or conflicts that might prevent you from 
starting the docker containers should be fixable using the slightly "nuclear" solution described in 
[this post](https://docs.tibco.com/pub/mash-local/4.3.0/doc/html/docker/GUID-BD850566-5B79-4915-987E-430FC38DAAE4.html).

> Notes:
> 1. Running `eval "$(docker-machine env <docker machine name>)"` may not be needed if docker is installed as an app on
> your system.
> 2. Running the series of commands described in the above post will result in the loss of any database data stored by 
> this service's database, or any other dockerized databases with persistent volumes that you may be working with.

# 2. Additional notes

Additional notes about the assumptions that were made for this exercise, areas for improvement etc. can be found in each
of the app's respective README files, linked at the top of this document.
