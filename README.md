# Flow

![Java Version](https://img.shields.io/badge/Java-v23-red)
![Repository Size](https://img.shields.io/github/repo-size/dzenthai/flow?color=red)

## **Description**

**Flow** is a Spring Boot application built with a microservices architecture, designed to manage and track income and expenses.
It provides users with a powerful tool to monitor their financial activities in real-time,
leveraging event-driven design and a distributed system to ensure scalability, reliability, and insightful financial analytics.

---

## **Key Features**

- **Real-time Financial Tracking**: Integrates a command and query architecture to deliver live updates on income and expenses.
- **Data Synchronization**: Uses Debezium and Apache Kafka for real-time replication and event streaming from PostgreSQL.
- **User Authentication**: Employs an Identity Server with JWT tokens for secure access and user management.
- **Query and Analytics**: Includes a Query service for retrieving financial data and a Statistic service for generating insights.
- **Caching**: Implements caching to improve performance and reduce latency in data access.
- **Configuration Management**: Features a Config Server for centralized settings and a Vault for secure credential storage.

---

## **Technologies**

- **Java**: The core programming language.
- **Spring Boot**: Framework for building the microservices.
- **PostgreSQL**: Relational database for storing financial records.
- **Debezium**: Tool for capturing database changes in real-time.
- **Apache Kafka**: Platform for event streaming and message brokering.
- **Docker**: Containerization for consistent deployment.
- **Vault**: Secure storage for sensitive data like credentials.

---

## **How it Works**

### **Microservices Architecture**

The "Flow" application is structured as a microservices system with the following components:

- **API Gateway**: The entry point, routing commands to the Budget (Command) service and queries to the Budget (Query) service.
- **Identity Server**: Handles user authentication and issues JWT tokens.
- **Budget (Command)**: Manages write operations and updates financial data in PostgreSQL.
- **Budget (Query)**: Manages read operations, retrieving data with caching support.
- **Statistic Service**: Analyzes financial data to provide trends and insights.
- **Config Server**: Centralizes configuration for all services.
- **Vault**: Stores sensitive information securely.

The system uses Apache Kafka for event streaming, with Debezium syncing PostgreSQL changes to Kafka topics in real-time.

### **Example Workflow**

1. A user logs in through the Identity Server and receives a JWT token.
2. The API Gateway routes a command (e.g., adding an income entry) to the Budget (Command) service.
3. Debezium captures the database change and streams it to Apache Kafka.
4. The Budget (Query) service processes the event, updates its cache, and makes the data available for queries.
5. The Statistic Service analyzes the data to provide financial insights.

### **Data Storage**

Financial data is stored in PostgreSQL with the following fields:
- **Amount**: The monetary value of the income or expense.
- **Date Time**: The timestamp of the transaction.
- **Category**: The type of financial activity (e.g., income, expense).
- **Description**: Notes or details about the transaction.

---

## **Installation Guide**

### **Prerequisites**

- Java 23
- Gradle 8.10.2
- Docker 27.2.0

### **Installation and Startup Steps**

1. **Clone the Repository**
   ```bash
   git clone https://github.com/dzenthai/flow.git
   cd flow