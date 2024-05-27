#!/bin/bash
mvn package

java -cp target/HTTPServer-1.0-SNAPSHOT.jar MK.HTTPServer.App
