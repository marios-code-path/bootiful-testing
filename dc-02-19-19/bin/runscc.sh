#!/bin/sh
export coord=$1; shift

java -jar runner-2.1.0.RELEASE.jar --stubrunner.stubsMode=local --stubrunner.ids=$coord
