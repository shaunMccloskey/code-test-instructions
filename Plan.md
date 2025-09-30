# URL Shortener Coding Task Plan

## High level architecture 

Split into two parts within the repo:
1. front end application built using react 
2. backend spring application with controller, service, model, repository

due to the data structure that i will be dealing with of a shortened url object containing only two feilds 
and the nature of the application and how it handles scale decided to use noSQL and for simplicity will use mongoDB


For each of the frontend and back end can docker the applications and use docker compose to deploy them both with simple mongo DB image.

### tests
spring application will need to test the controller, service and repository layer as well as some integration tests.

Junit and mockito with spring framework for testing
Test the react app with jest


start with the data layer and work back 



## TODO
observability
lombok on spring
testing reports
code quality and linters
security
splitting environments, dev vs production

longer term how to update to pull secrets from store in build?