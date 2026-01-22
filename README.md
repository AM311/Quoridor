![Gradle](https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white)
![jUnit](https://img.shields.io/badge/Junit5-25A162?style=for-the-badge&logo=junit5&logoColor=white)
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/AM311/Quoridor/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/AM311/Quoridor/tree/main)
[![codecov](https://codecov.io/gh/AM311/Quoridor/graph/badge.svg?token=6OHSRpqSwY)](https://codecov.io/gh/AM311/Quoridor)

# Quoridor

This repository contains an implementation of the **Quoridor** board game developed in **Java**, with support for:

- **Local** mode (CLI or GUI)
- **Client/Server** mode (CLI or GUI)
- Matches with **2 or 4 players**

The project is built using **Gradle** and can be started either by running dedicated Java `main` classes or via the **Gradle Wrapper**.

---

## Requirements

- **Java 21 (OpenJDK)**
- **Gradle 8.5**

> ℹ️ If you use the **Gradle Wrapper** included in the repository, it is sufficient to have a **JRE compatible with Gradle 8.5** available on your system.

---

## Game modes overview

The application can be started in three main modes:

### 1. Local Mode

- Started via the `` class
- Supports **CLI** or **GUI**

### 2. Server Mode

- Started via the `` class
- Handles multiple clients; necessary for playing from multiple devices
- Must be started **before** the clients

### 3. Client Mode

- Started via the `` class
- Supports **CLI** or **GUI**
- Connects to a remote server

> ⚠️ **Client** and **Server** modes must cooperate with each other. The **Local** mode is standalone.

---

## Running the application using Java

The application can be started by directly executing the corresponding `main` classes.

### Starter (Local Mode)

```bash
java Starter <NUMBER_OF_PLAYERS> <CLI|GUI>
```

- `<NUMBER_OF_PLAYERS>`: `2` or `4`
- `<CLI|GUI>`: user interface mode

**Example:**

```bash
java Starter 2 CLI
```

---

### ServerStarter (Server Mode)

```bash
java ServerStarter <PORT> <NUMBER_OF_PLAYERS>
```

- `<PORT>`: TCP port on which the server listens for connections
- `<NUMBER_OF_PLAYERS>`: `2` or `4`

**Example:**

```bash
java ServerStarter 4444 2
```

---

### ClientStarter (Client Mode)

```bash
java ClientStarter <SERVER_IP> <PORT> <CLI|GUI>
```

- `<SERVER_IP>`: IP address of the server
- `<PORT>`: port on which the server is listening
- `<CLI|GUI>`: user interface mode

**Example:**

```bash
java ClientStarter 127.0.0.1 4444 CLI
```

---

## Running the application using Gradle Wrapper

The same features are available via the **Gradle Wrapper**, which exposes three dedicated tasks:

- `runLocal`
- `runServer`
- `runClient`

The parameters are **exactly the same** as described above and must be passed using the standard Gradle syntax.

### Local Mode

```bash
./gradlew runLocal --args="<NUMBER_OF_PLAYERS> <CLI|GUI>"
```

**Example:**

```bash
./gradlew runLocal --args="2 CLI"
```

---

### Server Mode

```bash
./gradlew runServer --args="<PORT> <NUMBER_OF_PLAYERS>"
```

**Example:**

```bash
./gradlew runServer --args="4444 2"
```

---

### Client Mode

```bash
./gradlew runClient --args="<SERVER_IP> <PORT> <CLI|GUI>"
```

**Example:**

```bash
./gradlew runClient --args="127.0.0.1 4444 CLI"
```

