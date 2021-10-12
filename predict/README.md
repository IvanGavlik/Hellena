# Hellena

Application for predicting the results of football matches.

## API

generation is trigger on mvn clean compile
generate source code is in target/generated-sources/src

test files are also generated (do i need to disable geneeration of test files ?)

generated is controller and interface for controller service
so developer only need to implement interface for controller service

generation is done by maven plugin openapi-generator-maven-plugin.
Open APi Specification is in yaml file (dir is set by inputSpec property)

more info here:
https://swagger.io/specification/
https://swagger.io/docs/specification/basic-structure/
https://www.youtube.com/watch?v=YmQyzNF5iKg
https://github.com/OpenAPITools/openapi-generator/tree/master/modules/openapi-generator-maven-plugin
https://openapi-generator.tech/docs/generators/kotlin-spring

## Database

PostgreSQL

### Migration

flywaydb with Maven plugin (flyway-maven-plugin)

mvn flyway:migrate to start migration

more info: https://flywaydb.org/documentation/usage/maven/migrate
also
https://flywaydb.org/documentation/usage/maven/info


## Deploy

Configured for Heroku platform with Maven plugin (heroku-maven-plugin)

### Steps
* heroku login
* mvn clean heroku:deploy

## TODO

-> replace flyway plugin with out-of-the-box integration with Flyway
https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway
https://flywaydb.org/documentation/usage/plugins/springboot
