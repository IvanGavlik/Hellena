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

### API and Postman

* Import into postman content of predict/src/main/resources/api more info [here](https://learning.postman.com/docs/integrations/available-integrations/working-with-openAPI/)
* Create local environment. Params
    * name: baseUrl value: http://localhost:8080/v1

## Database

PostgreSQL

### Migration

flywaydb with Maven plugin (flyway-maven-plugin)

mvn flyway:migrate to start migration

more info: https://flywaydb.org/documentation/usage/maven/migrate
also
https://flywaydb.org/documentation/usage/maven/info

### Data Access

For DB data access Spring Data JPA is used.

Configuration of data source is done at src/main/resources/application.properties

Use data class to map table
Use standard Spring approach when creating repos
More info [here](https://docs.spring.io/spring-data/jdbc/docs/2.2.5/reference/html/#jdbc.repositories)

## Deploy

Configured for Heroku platform with Maven plugin (heroku-maven-plugin)

Runs on java 11 (specified in system.properties file) more info [here](https://devcenter.heroku.com/articles/customizing-the-jdk#specify-a-jdk-version)

### Steps
* heroku login
* mvn clean heroku:deploy

## FE

for fronted is used angular + boostrap
to start only FE navigate to src/main/angular/hellena ng serve -o

content of dist folder is in src/main/resources/static

build FE
* for production ng build --configuration production
* for development ng build --configuration development

to start FE and BE
* src/main/angular/hellena ng build
* run BE project (mvn or InteliJ)


## TODO
-> set constraint on DB (flyway)

-> replace flyway plugin with out-of-the-box integration with Flyway
https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto.data-initialization.migration-tool.flyway
https://flywaydb.org/documentation/usage/plugins/springboot

-> Auditing (created by and created day)
https://docs.spring.io/spring-data/jdbc/docs/2.2.5/reference/html/#auditing

-> maven configuration properties
    like java compiler version
    like java sdk version
    profiles ...

-> software requirements (see: https://mixmastamyk.bitbucket.io/pro_soft_dev/sdlc_2_req.html)

-> design (see: https://mixmastamyk.bitbucket.io/pro_soft_dev/sdlc_3_des.html)

-> quality (https://mixmastamyk.bitbucket.io/pro_soft_dev/sdlc_5_qa.html)
    -> sonar
    -> explore other tools (pmd, checkstyle ... Halstead Complexity Measures, Weighted Micro Function Points, and Cyclomatic Complexity.)

-> also https://mixmastamyk.bitbucket.io/pro_soft_dev/challenges.html

-> sonar qube: https://stackoverflow.com/questions/56805948/how-to-deploy-sonarqube-server-on-heroku-dyno
    add guide
    add automatic start of the sonar server
    add sonar guard and set sonar configuration
    fix on heroku build sonar qube is not executed

-> for selenium on heroku
    https://elements.heroku.com/buildpacks/heroku/heroku-buildpack-chromedrive
    then
       https://devcenter.heroku.com/articles/using-multiple-buildpacks-for-an-app
       how to -> https://www.andressevilla.com/running-chromedriver-with-python-selenium-on-heroku/
    else
        https://devcenter.heroku.com/articles/container-registry-and-runtime

-> start angular and BE in the same time (also possibility to start them separately maybe docker)

-> profiles local, dev, test, production

-> e2e tests

-> pathEncodingProblem

TODO
-> https://www.slant.co/topics/2579/~ocr-libraries
