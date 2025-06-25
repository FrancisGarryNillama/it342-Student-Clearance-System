# Student Clearance System

## Project Overview

Before graduation, students are typically required to complete a clearance process involving various departments such as the library, accounting, guidance, registrar, and more. Often, this process is paper-based and time-consuming.

The **Student Clearance System (SCS)** aims to digitize and streamline this clearance workflow into a centralized web application. The system will support multiple user roles (students, department heads, registrar, etc.) and allow clearance tasks to be submitted, approved, or flagged with comments in a trackable and efficient manner.

---

## Key Features

### 🔐 Authentication System
- Google OAuth login for all users (students, department heads, registrar)
- Role-based access control

### 🔄 Clearance Workflow Features
- Students can view their clearance requirements and status
- Department heads can approve, reject, or comment on tasks assigned to their department
- Registrar monitors overall clearance completion per student

### 🔔 Notifications
- Email or in-app notifications when a task is approved, rejected, or commented on

### 🔧 RESTful APIs
- Endpoints for clearance tasks, status updates, and feedback comments
- Audit trail APIs to log user actions
- API documentation using Swagger or Postman

### 🖥 Frontend UI
- Dashboards for each role (student, department, registrar)
- Visual progress tracking (e.g., step indicators, status tags)
- Comments section and document attachment view (if needed)

### 🗃 Database Design
- Proper schema for users, departments, tasks, statuses, and logs
- Use foreign keys to relate tasks to users and departments

---

## Team Members

### 👨‍💼 Francis Garry Nillama  
**Project Manager, Full-stack Developer**  
- Oversees entire project lifecycle  
- Backend API development  
- Database design & logic  
- Integration & deployment  

### 👨‍🎨 Vin Allen Funcion  
**Assistant PM, UI/UX & Frontend Developer**  
- UI/UX design & frontend development (React + Tailwind)  
- Dashboard creation  
- OAuth integration  
- User feedback and testing support  
