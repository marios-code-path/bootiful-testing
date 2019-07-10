export coord=$1; shift

java -jar runner-2.0.2.RELEASE.jar --stubrunner.stubsMode=local --stubrunner.ids=$coord
