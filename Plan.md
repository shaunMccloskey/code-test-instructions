# URL Shortener Coding Task Plan

## High level architecture 

Split into two parts within the repo:
1. front end application built using react 
2. backend spring application with controller, service, model, repository

due to the data structure that i will be dealing with of a shortened url object containing only two feilds 
and the nature of the application and how it handles scale decided to use noSQL and for simplicity will use mongoDB


For each of the frontend and back end can docker the applications and use docker compose to deploy them both with simple mongo DB image.

folder structure 
/url-shortener-backend
 | - spring application dockerized
/url-shortener-frontend
 | - react application dockerized
docker-compose.yml - builds the mongoDB, backend and front end, connect network and define env variables

### Implementation details
model - urlPair will store the alias and url 
repository - will need to be able to pull object by alias, check pre existing alias as they must be unique and delete by alias.

service -
        | - shorten method to take in alias and url, if no alias given generate random alias,  check if alias is unique and save new alias
        | - generateAlias method to generate random string
        | - helper get url
        | - helper delete url 
        | - get all urls
        | - generate random alias
Controller -
           | - post shortener - call service to saveUrlPair 
           | - get {alias} call service helper method
           | - delete {alias} call service helper method
           | - get /urls call service helper method return all urlPairs

code quality and linters
use spotless with google standards, add to the pom so that can run check to test if styling matches and apply to update code to match standards.
added as pre commit hook to the project, any commits will auto run the check and fail if not matching.




### tests
spring application will need to test the controller, service and repository layer as well as some integration tests.

Junit and mockito with spring framework for testing
Test the react app with jest


start with the data layer and work back 



## TODO
observability
testing reports
security
splitting environments, dev vs production

longer term how to update to pull secrets from store in build?