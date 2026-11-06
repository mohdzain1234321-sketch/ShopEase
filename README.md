# 🛒 ShopEase – Java Full Stack E-Commerce Web Application

ShopEase is a Java Full Stack E-Commerce Web Application designed to provide a convenient online shopping experience. It includes customer shopping functionalities and a dedicated admin panel for managing products, categories, orders, customers, inventory, and reviews.

## 🚀 Features

### 👤 Customer Module

- User Registration and Login
- Browse and Search Products
- Product Details and Related Products
- Shopping Cart Management
- Buy Now Functionality
- Wishlist Management
- Address Management
- Secure Checkout
- Order Placement and Order Tracking
- Payment Status Management (Demo Payment)
- Product Reviews and Ratings
- Customer Profile Management

### 🛠️ Admin Module

- Admin Authentication
- Admin Dashboard with Statistics
- Category Management (CRUD)
- Product Management (CRUD)
- Order Management and Status Updates
- Customer Management
- Inventory and Stock Management
- Customer Review Management

### 🔐 Security Features

- BCrypt Password Hashing
- Admin Authorization Filter
- Session-Based Authentication
- CSRF Protection for Admin Requests
- Session Fixation Protection
- Environment-Based Database Credentials

## 💻 Technologies Used

| Technology         | Purpose                   |
| ------------------ | ------------------------- |
| Java 21            | Backend Development       |
| JSP                | Dynamic Web Pages         |
| Servlets           | Request Handling          |
| JDBC               | Database Connectivity     |
| MySQL              | Database                  |
| HTML5              | Page Structure            |
| CSS3               | Styling                   |
| JavaScript         | Client-Side Functionality |
| Maven              | Build Management          |
| Apache Tomcat 10.1 | Application Server        |
| Eclipse IDE        | Development Environment   |
| Git & GitHub       | Version Control           |

## 🏗️ Project Architecture

```text
ShopEase/
│
├── src/main/java/com/shopease/
│   ├── controller/
│   ├── controller/admin/
│   ├── dao/
│   ├── daoimpl/
│   ├── filter/
│   ├── model/
│   ├── service/
│   └── utility/
│
├── src/main/webapp/
│   ├── admin/
│   ├── views/
│   ├── css/
│   ├── js/
│   ├── images/
│   └── WEB-INF/
│
├── pom.xml
└── README.md
```

## ⚙️ Installation and Setup

### Prerequisites

- Java JDK 21
- Apache Maven
- MySQL Server
- Apache Tomcat 10.1
- Eclipse IDE or VS Code

### Step 1: Clone the Repository

```bash
git clone https://github.com/mohdzain1234321-sketch/ShopEase.git
```

### Step 2: Configure the Database

Create a MySQL database named:

```sql
CREATE DATABASE shopease;
```

Import or execute the ShopEase database schema and configure the required tables.

### Step 3: Configure Environment Variables

Configure the following environment variables:

```text
DB_URL=jdbc:mysql://localhost:3306/shopease
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

Replace the database username and password with your own credentials.

Do not commit actual database passwords to GitHub.

### Step 4: Build the Project

Run the following Maven command:

```bash
mvn clean package
```

### Step 5: Deploy on Apache Tomcat

1. Generate the WAR file using Maven.
2. Copy `ShopEase.war` from the `target` folder.
3. Deploy it to Apache Tomcat 10.1.
4. Start the Tomcat server.
5. Open the application in your browser.

## 💳 Payment Information

ShopEase currently demonstrates the payment workflow using simulated payment statuses. It does not process real financial transactions through a payment gateway.

## 🎯 Project Objective

The objective of ShopEase is to demonstrate practical Java Full Stack development skills, including MVC-based application structure, Servlets, JSP, JDBC, database operations, authentication, authorization, CRUD operations, and e-commerce business workflows.

## 👨‍💻 Developer

**Mohammed Zain**

Java Full Stack Developer

## 📌 Repository

[ShopEase – GitHub Repository](https://github.com/mohdzain1234321-sketch/ShopEase)
