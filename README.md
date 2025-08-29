# java-qualifier

Spring Boot template for the Bajaj Finserv Health JAVA qualifier.

## Open in Gitpod
[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/YOUR-USERNAME/java-qualifier)

## Run locally
- Update `src/main/resources/application.yml` with your `app.name`, `app.regNo`, and `app.email`.
- Build:
  ```
  mvn clean package
  ```
- Run:
  ```
  mvn spring-boot:run
  ```
- Or run the produced jar:
  ```
  java -jar target/java-qualifier-1.0.0.jar
  ```

## What this does
- On startup it POSTs to the generateWebhook endpoint, reads `webhook` and `accessToken`,
  chooses an SQL string based on your regNo, saves it to `out/finalQuery.sql`, and POSTs it to the returned webhook with JWT auth.
