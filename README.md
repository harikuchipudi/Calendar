Calendar Management System

Main Objective:
- This project aims to provide a simple way to manage and schedule the meetings between differnt employees. Conflict of timing of any person has to be dealt with.
  The valid timeslots for arranging a meeting between two employees must be predictable from the calendars of the employees.

Execution:
- Employee profile are created by taking the required parameters and make it an entity such that it would be stored in the database.
- Whenever a meeting is scheduled between any employees , each of them is linked to the meeting object such that, their calendar now has an addition.
- Scheduling a meeting requires checking the conflict of timing for all the employees in the meeting, checking if the time is in the valid working hours.
- finding a slot for meeting between two employees also deals with the similar approach of extracting the calendar of each employee and cancel out the common exisitng slots from the total slots.

Tech Stack:
- SpringBoot for the backend functionality.
- REST API to be configured to maintain the request and responses.
- H2 In-memory database is used for the simple database operations.
- Postman is used for the testing and configuration of different API routes.

Code flow working:
1. Create the valid employee profiles
2. Schedule a meeting between two employees, check the calendar of both the employees and check if the time slot is conflicting
3. if there is no conflict, we create a new meeting object, making a new row in the meeting table
4. this meeting table is used to extract the calendar for each employee.
5. Different api routes are configured to ensure the funcitonality of the application

