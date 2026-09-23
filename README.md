# Expense Tracker V2 - Fixed Core Project

A simple Spring Boot + MongoDB expense tracker with separate login/register pages, USER/ADMIN roles, demo accounts, working expense CRUD, weekly/monthly tracking, Docker, and Render deployment support.

## Stack

- Java 26
- Spring Boot 4.1.1
- Spring MVC + Thymeleaf
- Spring Security
- Spring Data MongoDB
- MongoDB Atlas (`expense_db`)
- Maven Wrapper
- Docker

## Demo accounts

The application automatically creates/resets these demo accounts at startup:

- Admin: `admin@expense.local` / `Admin123!`
- User: `user@expense.local` / `User123!`

The demo USER also receives sample expenses the first time its account has no expense records.

## Main features

- Separate Login page
- Separate Registration page
- Registration creates USER accounts
- Role-based login redirect
- USER dashboard
- ADMIN dashboard
- Expense CREATE
- Expense READ/list
- Expense UPDATE
- Expense DELETE
- Each normal user can only modify their own expenses
- Weekly total
- Monthly total
- All-time total
- Filter history by all/week/month
- Admin can review all users and expenses
- Admin can enable/disable USER accounts
- Admin can delete any expense
- BCrypt password hashing
- CSRF protection remains enabled
- `/health` endpoint for Render health checks

## Local MongoDB Atlas setup

1. Copy `.env.example` to `.env`.
2. Put your working Atlas connection string in `MONGODB_URI`.
3. Keep `MONGODB_DATABASE=expense_db`.
4. `.env` is already ignored by Git and Docker.

Example shape only:

```properties
MONGODB_URI=mongodb+srv://USERNAME:PASSWORD@YOUR-CLUSTER.mongodb.net/expense_db?retryWrites=true&w=majority
MONGODB_DATABASE=expense_db
PORT=8080
```

### IntelliJ

Open the project as a Maven project and run:

`com.expensetracker.ExpenseTrackerApplication`

Then open:

`http://localhost:8080`

On Windows you can also run `run-local.bat`.

## Docker local run

Build:

```bash
docker build -t expense-tracker .
```

Run:

```bash
docker run --rm -p 8080:8080 \
  -e PORT=8080 \
  -e MONGODB_URI="YOUR_ATLAS_URI" \
  -e MONGODB_DATABASE=expense_db \
  expense-tracker
```

## Render deployment

This repository contains both `Dockerfile` and `render.yaml`.

### Easy Dashboard method

1. Push this project to GitHub.
2. In Render choose **New -> Web Service**.
3. Connect the repository.
4. Select **Docker** as the runtime.
5. Add environment variable `MONGODB_URI` with your real Atlas connection string.
6. Add `MONGODB_DATABASE=expense_db`.
7. Deploy.

Render injects the `PORT` environment variable. The application listens on `${PORT:8080}`.

### Blueprint method

Create a Render Blueprint from the repository. `render.yaml` already defines a Docker web service and asks you for `MONGODB_URI`.

## MongoDB Atlas network access

Your Atlas project must allow Render to connect. Configure Atlas Network Access appropriately. Do not put the MongoDB password in GitHub, `application.properties`, `Dockerfile`, or `render.yaml`.

## Important security note

If a real database password has been pasted into chat, source code, or another exposed location, rotate it in MongoDB Atlas before a public deployment. Keep the replacement value only in local `.env` and Render Environment Variables.
