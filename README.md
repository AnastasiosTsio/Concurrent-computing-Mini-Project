# Producers/Consumers Buffer Implementation

This Java project focuses on the implementation of a communication buffer for producers and consumers. It is part of the Concurrent Applications course at Polytech-INFO4, instructed by F. Boyer at the University of Grenoble Alpes during the academic year 2023-2024.

## Authors
Noah Kohrs
Anastasios Tsiompanidis

## Project Overview

The primary objective of this project is to implement a buffer for producers and consumers using a bounded buffer structure. This buffer facilitates the exchange of messages between threads, where producers deposit messages into the buffer, and consumers retrieve and process them. The implementation aims to be both efficient and coherent in a concurrent environment.
Project Structure

The project is organized into the following components:

    Buffer Implementation (Objective 1 - Solution directe):
        Implement the ProdConsBuffer class that adheres to the IProdConsBuffer interface. The initial implementation uses the wait/notify mechanism in Java.

    Test Application (Objective 1 - Solution directe):
        Create the TestProdCons class, which serves as the main test application. It creates a set of producer and consumer threads that operate on the ProdConsBuffer. Thread behavior is defined in separate classes for producers (Producer) and consumers (Consumer).

    Configuration (Objective 1 - Solution directe):
        Utilize the Properties class to configure the execution parameters, reading them from an XML file (options.xml). Parameters include the number of producers, number of consumers, buffer size, production time, and consumption time.

## Implementation Objectives
### Objective 1 - Solution directe

Implement the ProdConsBuffer class using the direct solution discussed in class, involving the wait/notify mechanism. Conduct tests to ensure the expected properties of the program.

### Objective 2 - Termination

Extend the direct solution to automatically terminate the application when all produced messages have been consumed and processed. Synchronize threads accordingly to achieve this termination condition.

### Objective 3 - Semaphore-based Solution

Implement an alternative version of the ProdConsBuffer class using the Semaphore class in Java for synchronization between producers and consumers. Maximize parallelism among different threads.

### Objective 4 - Locks and Conditions

Optionally, implement another version of the ProdConsBuffer class using Locks and Conditions in Java (ReentrantLock and Condition from java.util.concurrent).

### Objective 5 - Multi-Consumption

Extend the ProdConsBuffer behavior to allow a consumer to retrieve k consecutive messages from the buffer. Update the interface (IProdConsBuffer) accordingly and implement a solution that supports this functionality.

### Objective 6 - Synchronous Multi-Copy

Transform the behavior of the ProdConsBuffer class to allow producers to deposit a message in n copies, and the production and consumption of these copies to be synchronous. Update the interface (IProdConsBuffer) and validate this functionality through tests.

## Getting Started

    Ensure you have JDK version 1.8 or higher installed.
    Use an IDE such as Eclipse for development.
    Download the project files and extract them into a directory.
    Open the project in your IDE and configure the execution parameters using the options.xml file.
