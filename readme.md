# Spring DB Copy

Spring Konsolenapplikation zum Kopieren von Daten von einer Quell- zu einer Ziel-Postgres-DB

## Konfiguration

Datei apllication.yml im Verzeichnis [config](/config) erstellen

``` yaml
spring:
  datasource:
    source:
      url: jdbc:postgresql://localhost:5432/postgres
      username: admin
      password: pass
    target:
      url: jdbc:postgresql://localhost:5433/postgres
      username: admin
      password: pass

app.copy:
  tables:
    - tb_agent
    - tb_customer
    - tb_order
```

## Docker

```shell
# Datenbanken starten
docker-compose up -d
# Datenbanken stoppen und Volumes löschen
docker-compose down --volumes
```