# Notification Service:

## Overview
This is a Spring Boot Service that is a part of [E-Commerce Website](https://github.com/amira921/e-commerce-website) which is the final project at JobHacker Community Training.  
Notification Service facilitates the communication between [Order Service](https://github.com/omarsamir1999/E-commerce/tree/order-service/order-service) and customers by enabling the transmission of order-related information through email.

## Features
- Listens to a RabbitMQ queue for incoming order-information messages from Order Service
- Create [Email Template]() for sending emails
- Sends emails to customers using a SMTP server based on the content of the messages
- Perform CRUD operations: 
   - Add order-information email
   - Update order-information email
   - Get Failed order-information emails
- Schedule task to resend failed emails

## Email Sample
<p align="center"><img src="asset/Email Sample.jpg" width="500" length="600"></p>

## Json Message Format between Order Service and Notification Service
<p align="center"><img src="asset/order-information message.jpg" height="600" width="400"></p>

## Notification Service ERD
<p align="center"> <img src="asset/notification service erd.png" width="400" height="400"> </p>

## Technologies Used
- Spring Boot 3.1.5
- Spring Web 3.1.5
- Java 17
- Maven
- MySQL Server Driver 8.0.33
- Spring Data JPA 3.1.5
- Spring for RabbitMQ 3.0.10
- Thymeleaf 3.1.5
- Java Mail Sender 3.1.5
- MapStruct
- AspectJ
- Lombok
- Spring Validation

## Prerequisites
Before you begin, ensure you have met the following requirements:
- Java Development Kit (JDK) 8 or higher installed
- Maven build tool installed
- Your preferred IDE (e.g., IntelliJ IDEA, Eclipse)
- Docker Desktop
- Xampp Control Panel

## Getting Started
1. Clone the repository: `git clone https://github.com/amira921/E-commerce-Notification-Service`
2. Navigate to the project directory: `cd ./E-commerce-Notification-Service`
3. Install dependencies: `mvn install`
4. Update the `application.properties` file with your RabbitMQ, MYSQL, SMTP server details,Thymeleaf.
5. Start Apache, MySQL, Docker Desktop, and Tomcat from Xampp
6. Run RabbitMQ using CMD: `docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.13.0-rc.2-management`
7. Acccess RabbitMQ via `http://localhost:15672`
8. Run the application: `mvn spring-boot:run`

## Design Patterns Applied
- **Adapter** : DataFormatAdapter
   - RabbitMQ Message to Domain Object
   - Database Object to Html Content
- **Proxy** : Logging & Exception
   - Implemented by AOP (AspectJ) 

## All 5 SOLID Principles Applied