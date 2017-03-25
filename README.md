# RosteringSystem

## Version
1.0.0

## Description

This project prepares the roster based on spreadsheet data.

## Prerequisite

 - Apache Maven 3.3.1
 - Java Version 1.7
 - Eclipse IDE(optional)

## What is the starting point?

main.ClientMain is an entry point.

## How to run from command line?

 1. open command terminal
 2. execute 'mvn clean install' command (This will generate jar file in target folder)
 3. execute 'java -cp "target/RosteringSystem-jar-with-dependencies.jar; ." main.ClientMain' command
 4. If you want to pass spreadsheet file name from other than files from src/main/resources folder than 
   first argument after ClientMain in the above command will be absolute file path.
   For eg: 'java -cp "target/RosteringSystem-jar-with-dependencies.jar; ." main.ClientMain C:\Users\user1\test_pref.xlsx'
 
