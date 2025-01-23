# Report Project Progettazione e Validazione di Sistemi Software

This document specifies the requirements and tests coverage for **UniPro**, a system that allows researchers to submit the hours they worked on a project, apply for days off, and generate reports. 

# Requirements

## Glossary
- **Hours worked :** A record of hours worked on a specific project for a day given.  
- **Principal Investigator (PI) :** The lead researcher responsible for approving hours.  
- **Day Off :** A day off approved for a researcher. A researcher can not work on a day off (in this component the days off are automatically approved).  
- **Report :** A document summarizing time tracking data for researchers or projects for a given month.  
- **Invalid hours :** hours that have been submitted on an already approved day off, on a weekend day or a public holiday.   

## 0- Log In
Researchers shall be able to log in the system using their academic email and password.

## 1- Hours worked submission

Researchers shall be able to log and submit hours for a specific project with this format : hours worked, date. 

## 2- Approval of hours

The principal investigator of a project shall be able to approve valid hours that have submitted for this project.

## 3- Rejection of hours

The principal investigator of a project shall be able to reject invalid hours that have submitted for this project.

## 4- Request of days off

Researchers shall be able to request for days off. (In this system, they are automatically approved).

## 5- Generate reports for a researcher

Researchers shall be able to generate a report summarizing their daily project hours and vacation days for a specific month and year. For each project they worked on this month, they can see how many hours they spent working on it each day, as well as a total for each day and each project.

## 6- Generate project-specific reports for a researcher

Principal investigator shall be able to generate reports for the project they are leading for a given month and year. They can see for a given researcher how many hours they spent on the project each day. They can also see how many hours they spent on other project and other project lead by the same funding agency, as well as a total for each day.

## Scenarios :

### 1 - Submission of hours 
Researcher Mario Rossi logs 5 hours on January 13, 2025, for the project NeuroPuls and submits the hours.

### 2 - Approval of hours 
Researcher Jean Martin, PI of the project NeuroPuls, approves the 3 hours of January 14, 2025 submitted by Mario Rossi.

### 3 - Rejection of hours 
Researcher Jean Martin, PI of the project NeuroPuls, rejects the 3 hours of January 6, 2025 submitted by Mario Rossi because it is a public holiday.

### 4 - Ask a day off
Researcher Jean Martin ask for a day off for January 16, 2025.

### 5- Generate a report
Researcher Mario Rossi generates a report for January 2025. He can see how many hours he spent on each of his projects for every day of the month.

### 6- Generate a project report
Researcher Jean Martin, Pi of the project NeuroPuls, generates a report for January 2025 for Mario Rossi. He can see how many hours Mario spent on each of the project for every day of the month and also how many hours he spent in total on other projects and other projects lead by the same funding agency.


# Test Cases
Data : we created 3 projects and 3 researchers so that we could test different configuration (projects with the same funding agency or no, researchers with projects as PI or no etc).  
In total, we have 34 tests.
### Unit Tests :
For every HTML page of the system, we have a java class with several tests. Each test tests a functionality that appears on the page. We have 28 unit tests.
Test Coverage : we achieve an instruction coverage of 99% and a branch coverage of 88%. The missing branches being mostly error cases that we did not write a test for, as testing already takes a lot of time.
### Acceptance test cases :
We wrote 6 tests implementing the different scenarios. Each test starts from the login page so that we test the full scenario usage of the system.
