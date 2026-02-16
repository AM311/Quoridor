![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![jUnit](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/AM311/Quoridor/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/AM311/Quoridor/tree/main)
[![codecov](https://codecov.io/gh/AM311/Quoridor/graph/badge.svg?token=6OHSRpqSwY)](https://codecov.io/gh/AM311/Quoridor)

# Quoridor

This repository contains an implementation of the **[Quoridor](https://en.wikipedia.org/wiki/Quoridor)** board game developed in **Java**, with support for:

- Matches with **2 or 4 players**
- **Local** mode (CLI or GUI)
- **Client/Server** mode (CLI or GUI)

---

## Requirements

In order to compile and run the project, the following minimum requirements are needed:

- **Java 21**
- **Gradle 8.5**

> ℹ️ To execute the **Gradle Wrapper** included in the repository, it is sufficient to have Java 8 (or higher) available on your system.
> 
> The project is configured to use **Java 21** via **Gradle Toolchains**.
> If Java 21 is not installed locally, Gradle 8.5 can automatically provide a compatible Java 21 toolchain for compilation and execution tasks.

---

## Game modes overview

The application can be started in three main modes:

### 1. Local Mode

- Started via the `Starter` class
- Supports **CLI** or **GUI**

### 2. Server Mode

- Started via the `ServerStarter` class
- Handles multiple clients; necessary for playing from multiple devices
- Must be started **before** the clients

### 3. Client Mode

- Started via the `ClientStarter` class
- Supports **CLI** or **GUI**
- Connects to a remote server

> ⚠️ **Client** and **Server** modes must cooperate with each other. The **Local** mode is standalone.

---

## Compiling the application

To compile the application given the source code, it is sufficient to call the proper Gradle task.

```bash
./gradlew build
```

It is not necessary to explicitly call the `build` task when starting the application via the Gradle Wrapper as indicated below, since the building process in implicitly executed.

When the `build` task is called, also 3 JAR files are created in the `build/libs` directory:
- `Quoridor-XXX-local.jar` 
- `Quoridor-XXX-server.jar` 
- `Quoridor-XXX-client.jar`

where `XXX` represent the current application version.

---

## Running the application using the Gradle Wrapper

The application can be started via the **Gradle Wrapper**, calling one of the three dedicated tasks:

- `runLocal`, which starts the application in **local mode**, calling the `Starter` class
- `runServer`, which starts the application in **server mode**, calling the `ServerStarter` class
- `runClient`, which starts the application in **client mode**, calling the `ClientStarter` class

The required parameters for each call are describer below. 

### runLocal (Local Mode)

```bash
./gradlew runLocal --args="<NUMBER_OF_PLAYERS> <CLI|GUI>"
```

- `<NUMBER_OF_PLAYERS>`: `2` or `4`
- `<CLI|GUI>`: user interface mode

**Example:**

```bash
./gradlew runLocal --args="2 CLI"
```

### runServer (Server Mode)

```bash
./gradlew runServer --args="<PORT> <NUMBER_OF_PLAYERS>"
```

- `<PORT>`: TCP port on which the server listens for connections
- `<NUMBER_OF_PLAYERS>`: `2` or `4`


**Example:**

```bash
./gradlew runServer --args="4444 2"
```

### runClient (Client Mode)

```bash
./gradlew runClient --args="<SERVER_IP> <PORT> <CLI|GUI>"
```

- `<SERVER_IP>`: IP address of the server
- `<PORT>`: port on which the server is listening
- `<CLI|GUI>`: user interface mode

**Example:**

```bash
./gradlew runClient --args="127.0.0.1 4444 CLI"
```

---

## Running the application using the JAR files

To run the application using the JAR files generated during the building process, a compatible JRE must be available on the local machine.

The following syntax can be used to run the application from the proper JAR:

```bash
java -jar build/libs/<PROPER_JAR_NAME>.jar <args>
```

where the JAR names are the ones defined above and the arguments are the same passed to the correspondent Gradle tasks.  