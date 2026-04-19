# RestAssuredTraining

API test automation training project using Rest Assured, TestNG, and Cucumber against GoRest user endpoints.

## Prerequisites

- Java 11+
- Maven 3.9+
- A valid GoRest API token

## Configuration

This project no longer keeps secrets in source code.

Token lookup order is:

1. JVM property `gorest.token`
2. Environment variable `GOREST_TOKEN`
3. Dotenv file in project root: `.env`

Provide token and optional base URL using one of the following:

1. JVM properties

```bash
mvn -Dgorest.token=YOUR_TOKEN -Dgorest.baseUrl=https://gorest.co.in/public-api/users -PapiTest test
```

2. Environment variable

```bash
export GOREST_TOKEN=YOUR_TOKEN
mvn -PapiTest test
```

3. Dotenv file (project root)

```bash
echo 'GOREST_TOKEN=YOUR_TOKEN' > .env
mvn -PapiTest test
```

Optional template file:

```bash
cp .env.example .env
```

## Run TestNG API Suite

```bash
mvn -PapiTest test
```

## Run Cucumber Suite

```bash
mvn -Dcucumber.filter.tags="not @Ignore" -PcucumberTest test
```

## Reports

- Extent report: `reports/TestReports.html`
- Cucumber JSON: `target/cucumber.json`
- Cucumber HTML: `target/cucumber-html-report`

## Notes

- Tests generate unique email data in TestNG flow to avoid duplicate-user failures.
- For write operations, API responses may vary between `200`, `201`, `204`, or `302` based on upstream behavior.
