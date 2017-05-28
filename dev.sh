#!/usr/bin/env bash

# SETTING VARIABLES NEEDED TO MAP
# VOLUMES IN docker-compose.yml
PROJECT=$(pwd)

export PROJECT

# EXECUTING DOCKER COMPOSE
cd docker && \
    docker-compose run --rm -p 5050:5050 securatta && \
    docker-compose stop
