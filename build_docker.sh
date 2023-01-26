#!/bin/bash

# Package
mvn package

# Build the docker image
docker build -t static-file-server -f Dockerfile ./target