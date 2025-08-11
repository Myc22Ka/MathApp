#!/bin/bash

echo "Stopping all containers..."
docker stop $(docker ps -aq) 2>/dev/null

echo "Removing all containers..."
docker rm $(docker ps -aq) 2>/dev/null

echo "Removing all images..."
docker rmi -f $(docker images -aq) 2>/dev/null

echo "Removing all volumes..."
docker volume rm $(docker volume ls -q) 2>/dev/null

echo "Removing all networks (except default)..."
docker network rm $(docker network ls | grep -v "bridge\|host\|none" | awk '{print $1}') 2>/dev/null

echo "Removing all build cache..."
docker builder prune -af 2>/dev/null

echo "Docker cleanup complete."