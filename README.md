# Expense-Manger
The Expense Manager App is a user-friendly mobile application designed to help users track their expenses and income effectively. It provides a range of features for user registration, authentication, expense/income tracking, data filtering, and profile management.

## Table of Contents
- [Features](#features)
- [Installation](#installation)
- [Usage](#usage)

## Features

### 1. User Authentication
- Users can register with their name, phone number, email address, and password.
- Input validations ensure proper data entry.

### 2. Expense and Income Tracking
- Add expenses and income with date, reason, and description.
- Update and delete expenses/income records.

### 3. Dynamic Total Display
- The app displays total income and expenses on the main fragment.
- The color of a TextView changes dynamically based on the relationship between income and expenses (green for income > expenses, red for expenses > income, white for equal).

### 4. Data Filtering
- Filter income and expenses by date.

### 5. Tab Layout and Fragments
- Utilizes fragments and a tab layout for easy navigation between different sections of the app.

### 6. Income Fragment
- View a separate list of income records.
- Display the total income at the top.

### 7. Expense Fragment
- View a separate list of expense records.
- Display the total expenses at the top.

### 8. Profile Section
- Calculate age in years, months, and days.
- Calculate BMI (Body Mass Index).

## Installation

1. Clone the repository to your local machine.
2. Install dependencies using `npm install` or `yarn install`.
3. Start the app using `npm start` or `yarn start`.

## Usage

1. Register or login to access the app's features.
2. Use the main fragment to add, update, and delete expenses and income records.
3. Navigate between the Income and Expense fragments to view separate lists.
4. Access the Profile section to calculate age and BMI.


