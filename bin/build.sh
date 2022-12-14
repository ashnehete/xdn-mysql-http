#!/bin/sh

mvn clean install
docker build -t xdn-mysql-http:latest .