#!/bin/bash
mkdir -p gradle/wrapper
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://repo.gradle.org/gradle/libs-releases-local/org/gradle/gradle-wrapper/8.6/gradle-wrapper-8.6.jar
git add gradle/wrapper/gradle-wrapper.jar
git commit -m "Add gradle-wrapper.jar"
git push
