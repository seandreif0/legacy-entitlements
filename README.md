Entitlement recalculation service (ENT-SVC). See Confluence for runbook.

## Build and run

Requires Java 17.

    mvn package
    java -jar target/ent-svc.jar

Profiles (`--spring.profiles.active=...`):

- `local` (default): in-memory H2 in Oracle mode, loaded from `sql/schema.sql` and `sql/seed.sql`.
- `prod`: Oracle at `ent-ora-p01.corp.meridian.example:1521/ENTP`; the password comes from the
  `ENT_JDBC_PASSWORD` environment variable. SQL init is off (the DBA owns the schema).

System properties still override any setting, e.g. `-Dent.extract.dir=/path/to/drop`.

## Nightly schedule

The schedule is unchanged: the recalc runs at `ent.batch.cron=0 15 2 * * *` (02:15) in the zone
given by `ent.batch.zone` (empty = the JVM's default zone). Containers usually default to UTC, so
prod must set `ent.batch.zone` to the current server's timezone (e.g. `-Dent.batch.zone=America/New_York`).

## Manual rerun

    java -Dent.extract.dir=/path/to/drop -jar ent-svc.jar --spring.profiles.active=prod,rerun

Runs one recalc for today without the web server or scheduler, prints the result and exits
0 on OK, 2 otherwise (NO_FILE / FAILED). Use `local,rerun` against the H2 database.

## Endpoints

- SOAP (unchanged wire contract, used by the research portal and index publishing tool):
  `POST /services/EntitlementService` (WSDL in `src/main/resources/wsdl/EntitlementService.wsdl`).
- REST/JSON (alongside SOAP, must be covered by the same F5 rule before exposing):
  - `GET /api/v1/users/{userId}/entitlements`
  - `GET /api/v1/users/{userId}/access/{datasetCode}` returns `{"allowed": true|false}`
