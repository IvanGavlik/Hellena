# Hellena

Application for predicting the results of football matches.

## Database

PostgreSQL

### Migration

flywaydb with Maven plugin (flyway-maven-plugin)

more info: https://flywaydb.org/documentation/usage/maven/migrate
also
https://flywaydb.org/documentation/usage/maven/info

## Deploy

Configured for Heroku platform with Maven plugin (heroku-maven-plugin)

### Steps
* heroku login
* mvn clean heroku:deploy
