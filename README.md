# Securatta

`Securatta` is an authentication service

## Documentation

https://pacotheai.github.io/securatta/

## Development

Because `Securatta` needs a Cassandra database, a `docker-compose.yml`
has been created to run a docker environment where a Cassandra
database is available for development purposes.

In order to launch the aforementioned environment make sure you have
installed `Docker` in your machine and then execute:

    docker-compose run --rm -p 5050:5050 securatta && docker-compose stop

This will open a tmux session to the Docker environment.
