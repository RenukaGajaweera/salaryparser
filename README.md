# salaryparser
- Spring boot application using spring batch processing to read a csv file consisting of employee salary information and write only the validated entries to in-memory hsql database. The application will print the average salary of employees in db table along with the number of invalid entries and a list of invalid entries. **Assumption:** the average salary of employees is calculated using the number of entries per employee in DB table.
- Java version 17


## Build Instructions
- open command terminal
- *cd* into the salaryparser folder location
- type *mvn clean install* create runnable jar
## Run Instructions
- the jar can be run using the following command
- *java -jar salaryparser.jar --file-location=C:\Personal\d3\salaryparser\src\main\resources\sample-data.csv*
- the location of the .csv file can be provided through the *file-location* input argument
- an example csv file can be found inside *.\salaryparser\src\main\resources\sample-data.csv*
