# 🔐 Spring Boot Authentication & Authorization System

A secure backend authentication system built using Spring Boot. 
This project implements JWT-based authentication, role-based authorization, 
and secure password recovery functionality.

---

## 🚀 Features

- User Registration & Login
- JWT-based Authentication
- Refresh Token Mechanism
- Role-Based Access Control (USER / ADMIN)
- Forgot Password with Secure Token
- Promote USER to ADMIN (Role Update)
- Secure REST APIs
- Exception Handling with Custom Exceptions
- DTO Layer (Entities not exposed directly)

---

## 🛠️ Technologies Used

- Java
- Spring Boot
- Spring Security
- JWT (JSON Web Token)
- Spring Data JPA
- MySQL
- Maven

---

## 🗄️ Database Design

The database was designed first and then mapped using JPA.
Relationships and constraints are properly implemented.

Main Tables:
- Users
- Roles
- Refresh Tokens
- Password Reset Tokens

---

## 📂 Project Structure

