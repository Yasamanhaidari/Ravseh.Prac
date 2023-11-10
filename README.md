# ToDo List Application

A Java-based to-do list application with task management features.

## Features
- Add tasks to the "To Do" column.
- Start and move tasks to the "Doing" column.
- Complete tasks and move them to the "Done" column.
- ...

## Technologies Used
- Java
- MySQL

## How to Use
1. Clone the repository.
2. Set up the database using the provided SQL script.
3. Run the application.

## Code Structure
- `Main.java`: Main class for launching the program.
- `ToDo.java`, `Doing.java`, `Done.java`: Task management classes.

## Database Structure
- `users` table: Stores user information.
- `tasks_<user_id>` tables: Store tasks for each user.
