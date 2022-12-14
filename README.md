# xdn-mysql-http

An HTTP interface between XDN and MySQL.

## Build

```shell
./bin/build.sh
```

## Run

```shell
docker run -d --name xdn-mysql-http -p 8000:8000 xdn-mysql-http
```

This will start a docker container with an HTTP server exposed on port 8000.

## Endpoints

### Execute

`POST /execute`

```shell
curl --location --request POST 'localhost:8000/execute' \
--header 'Content-Type: application/json' \
--data-raw '{
    "query": "SELECT * FROM users;",
    "database": "test",
    "host": "localhost:3306"
}'
```

### Checkpoint

`POST /checkpoint`

```shell
curl --location --request POST 'localhost:8000/checkpoint' \
--header 'Content-Type: application/json' \
--data-raw '{
    "host": "127.0.0.1"
}'
```

### Restore

`POST /restore`

```shell
curl --location --request POST 'localhost:8000/restore' \
--header 'Content-Type: application/json' \
--data-raw '{
    "host": "127.0.0.1",
    "src": "/tmp/dump.sql"
}'
```