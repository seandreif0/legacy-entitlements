Entitlement recalculation service (ENT-SVC). See Confluence for runbook.

## Nightly schedule

The recalc runs at `ent.batch.cron=0 15 2 * * *` (02:15) in the zone given by `ent.batch.zone`
(empty = the JVM's default zone). Containers usually default to UTC, so prod must set
`ent.batch.zone` to the current server's timezone (e.g. `-Dent.batch.zone=America/New_York`).

## Manual rerun

    java -Dent.extract.dir=/path/to/drop -jar ent-svc.jar --spring.profiles.active=prod,rerun

Runs one recalc for today without the web server or scheduler, prints the result and exits
0 on OK, 2 otherwise (NO_FILE / FAILED). Use `local,rerun` against the H2 database.
