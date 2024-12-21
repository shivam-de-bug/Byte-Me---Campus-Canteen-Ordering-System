# Byte Me! - Campus Canteen Ordering System

## Overview
The **"Byte Me! - Campus Canteen Ordering System"** is a CLI and GUI-based application designed to streamline food ordering and management for a college canteen. It offers a seamless experience for students, staff, and administrators to interact with the canteen system effectively. This project integrates advanced Java concepts, including collections, GUI, file handling, and JUnit testing, to provide a robust and user-friendly solution.

---

## Features

### CLI Features
1. **Customer Interface**:
    - Browse menu items, add to cart, and place orders.
    - Track orders, cancel pending orders, and view order history.
    - VIP feature: Priority ordering for customers paying Rs. 1000.
    - Provide reviews for delivered food items.

2. **Admin Interface**:
    - Manage menu items (add, update, and remove).
    - Handle orders with a priority queue for VIP customers.
    - Generate daily sales reports.
    - Manage refunds and process special customer requests.

3. **Data Persistence**:
    - User credentials and order histories are saved in files (`users.dat` and `orderHistory.dat`).
    - Proper exit ensures all data is saved, and restarting the application loads saved data.

### GUI Features

1. **Menu GUI for Unauthorized Users**:
    - Public interface for viewing the menu only.

2. **Menu and Pending Orders GUI for Customers**:
    - Menu GUI includes a "View Pending Orders" button.
    - Pending Orders GUI displays the customer's personal pending orders.

3. **Menu and Pending Orders GUI for Admins**:
    - Menu GUI includes a "View Pending Orders" button.
    - Pending Orders GUI displays all pending orders from all customers.

**Total GUIs (5 Pages)**:
1. Menu GUI for Unauthorized Users.
2. Menu GUI for Customers.
3. Pending Orders GUI for Customers.
4. Menu GUI for Admins.
5. Pending Orders GUI for Admins.

---

## Files Used
1. **users.dat**:
    - Stores user credentials.
    - Loaded on application startup, updated on user exit.

2. **orderHistory.dat**:
    - Stores order histories for all customers.
    - Loaded on application startup, updated on user exit.

---

## Assumptions

### Customer Assumptions
- VIP status requires a payment of Rs. 1000 for priority order processing.
- Customers only see available items in the menu.
- Orders can only be canceled if pending preparation.
- Reviews can only be submitted for delivered items.

### Admin Assumptions
- Admins process VIP orders before regular orders.
- Admins cannot directly change the order status, ensuring fairness.
- Removing a menu item cancels all pending orders containing that item.

---

## Test Cases Overview

1. **LoginTest.java**:
    - Validates login functionality.
    - Tests scenarios: successful login, invalid credentials.

2. **Unavailable_Item_test.java**:
    - Ensures the system handles out-of-stock items appropriately.
    - Tests scenarios: ordering unavailable items, ensuring only in-stock items are processed.

---

## Collections & Data Structures Overview

1. **Order Class**:
    - `HashMap`: Food items and quantities.
    - `String[]`: Special requests and statuses.

2. **FoodItem Class**:
    - `ArrayList`: Dynamic storage for customer reviews.

3. **Menu Class**:
    - `HashMap`: Groups food items by category.
    - `ArrayList`: Holds menu items for easy access.

4. **OrderManager Class**:
    - `PriorityQueue`: Handles pending orders with VIP prioritization.
    - `ArrayList`: Stores completed order history.

5. **Customer Class**:
    - `ArrayList`: Stores order history.
    - `Set`: Tracks delivered items for reviews.

6. **Admin Class**:
    - `HashMap`: Menu management and order tracking.
    - `ArrayList`: Sales reporting and order queues.

7. **Main Class**:
    - `HashMap`: Stores customer accounts for efficient access.

---

## Project Workflow

### Unauthorized Users
- Can only see the menu (GUI).

### Customers
1. Log in to access the Menu GUI.
2. View personal pending orders through the "View Pending Orders" button.

### Admins
1. Log in to access the Menu GUI with admin-specific features.
2. View all pending orders through the "View Pending Orders" button.

---

## Conclusion
The "Byte Me! - Campus Canteen Ordering System" provides a complete solution for managing a college canteen's food ordering process. With a combination of CLI for functionality and GUI for an intuitive display, it ensures efficient operations, prioritizing user convenience and fairness.

