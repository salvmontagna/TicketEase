# TicketEase Installation Guide

## Prerequisites

1. **PostgreSQL Database**: This project uses PostgreSQL. Make sure you have PostgreSQL installed on your system.

2. **Java SE**: The project was realized using Java SE with JDK 20.

## Steps to Set Up the Project

### 1. Install PostgreSQL
Download and install PostgreSQL from the official [PostgreSQL website](https://www.postgresql.org/download/).

### 2. Set Up the Database

1. **Create the Database**:
   - Open your terminal or PostgreSQL client.
   - Run the following command to create the `ticketease` database:
     ```sql
     CREATE DATABASE ticketease;
     ```

2. **Import the Database Schema**:
   - The project includes a dump file (`dump.sql`) that contains all necessary tables and sample data.
   - To import the dump into the `ticketease` database, run:
     ```bash
     psql -U your_username -d ticketease -f /path/to/dump.sql
     ```

### 3. Run the Backend

- The backend is implemented in Java SE with JDK 20. The main entry point of the project is the class `TicketEase`.

