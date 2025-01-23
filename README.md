# Report Project Progettazione e Validazione di Sistemi Software

This document specifies the requirements and tests coverage for **UniPro**, a system that allows researchers to submit the hours they worked on a project, apply for days off, and generate reports. 

# Requirements

## Glossary
- **Hours worked :** A record of hours worked on a specific project by a researcher for a day given.  
- **Principal Investigator (PI) :** The lead researcher of a project responsible for approving or rejecting hours submitted.  
- **Day Off :** A day off that has been approved for a researcher. A researcher can not work on a day off (in this component the days off are automatically approved).  
- **Report :** A document summarizing time tracking data for researchers or projects for a given month.  
- **Invalid hours :** hours that have been submitted on an already approved day off, on a weekend day or a public holiday.   

## 0- Log In
Researchers shall be able to log in the system using their academic email and password.

## 1- Hours worked submission

Researchers shall be able to log and submit hours for a project they work for with this format : hours worked, date. 

## 2- Approval of hours

The principal investigator of a project shall be able to approve valid hours that have submitted by a researcher for this project.

## 3- Rejection of hours

The principal investigator of a project shall be able to reject invalid hours that have submitted by a researcher for this project.

## 4- Request of days off

Researchers shall be able to request for days off. (In this system, they are automatically approved).

## 5- Generate reports for a researcher

Researchers shall be able to generate a report summarizing their daily hours and vacation days for a specific month and year. For each day of the month, they can see how many (approved) hours they worked on each project, or an X if they had a day off. They can also see the total of hours worked for the month for each project and the total of hours worked for each day.  
The hours yet to be approved are not visible.

## 6- Generate project-specific reports for a researcher

Principal investigator shall be able to generate reports for the project they are leading for a given month and year.  
For a specific researcher who works on the project, the PI can see how many (approved) hours the researcher worked on the project each day, as well as how many hours they worked on other projects lead by the same funding agency or other projects. Days off are also shown with an X
The hours yet to be approved are not visible.

## Scenarios :

### 1 - Submission of hours 
Researcher James Smith logs 5 hours on January 13, 2025, for the project NeuroPuls and submits the hours.

### 2 - Approval of hours 
Researcher John William, PI of the project NeuroPuls, approves the 3 hours of January 14, 2025 submitted by James Smith.

### 3 - Rejection of hours 
Researcher John William, PI of the project NeuroPuls, rejects the 3 hours of January 6, 2025 submitted by James Smith because it is a public holiday.

### 4 - Ask a day off
Researcher John William ask for a day off for January 16, 2025.

### 5- Generate a report
Researcher James Smith generates a report for January 2025. He can see how many hours he spent on each of his projects for every day of the month.

### 6- Generate a project report
Researcher John William, Pi of the project NeuroPuls, generates a report for January 2025 for James Smith. He can see how many hours James spent on each of the project for every day of the month and also how many hours he spent in total on other projects and other projects lead by the same funding agency.


# Test Cases
Data : we created 3 projects and 3 researchers so that we could test different configurations (projects with the same funding agency or not, researchers with projects as PI or not etc).  
In total, we have 34 tests.
### Unit Tests :
For every HTML page of the system, we have a Java class with several tests. Each test tests a functionality that appears on the page. We have 28 unit tests.  
Test Coverage : we achieve an instruction coverage of 99% and a branch coverage of 88%. The missing branches being mostly error cases that we did not write a test for, as testing already takes a long time.
### Acceptance test cases :
We wrote 6 tests implementing the different scenarios. Each test starts from the login page so that we test the full scenario usage of the system.
