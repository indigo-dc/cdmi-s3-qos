#!/bin/bash

docker-compose -f docker-compose-life-ex-redis.yml down
docker rmi docker_cdmi-life-ex-redis
docker rmi docker_redis-persistent-ex
