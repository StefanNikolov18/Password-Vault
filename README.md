# Password Vault - MJT course FMI

A secure multi-client password management server built with **Java (Java Net)**.
It allows users to safely store, generate, and retrieve credentials with encryption and security validation.

---

## Features

* **User Authentication**

  * Register & login via `AuthenticationService`
  * Passwords stored hashed using **SHA-256**
  * User data persisted in `users.db`

*  **Secure Vault Storage**

  * Each user has an individual vault file in:

    ```
    data/vault/
    ```
  * Credentials are encrypted using **AES** before storage
  * Decrypted only when retrieved

*  **Vault Operations (VaultService)**

  * Add credentials
  * Remove credentials
  * Retrieve stored passwords
  * Generate secure passwords

*  **Security Validation**

  * Passwords are checked using **Enzoic API**
  * Prevents weak or compromised passwords

*  **Command Handling**

  * `CommandHandler` processes all client requests
  * Supports multiple simultaneous clients

*  **Error Logging**

  * All errors are logged in:

    ```
    data/logs/error.log
    ```

---

##  Server

* Runs on:

  ```
  Port: 4444
  ```
* Handles multiple clients concurrently

---

##  Security Overview

* AES encryption for stored credentials
* SHA-256 hashing for user passwords
* External API validation (Enzoic) for password strength
* Isolated vault per user

## To Started
1. Clone the repository
* git clone <your-repo-url>
* cd Password-Vault
2. Compile
* javac *.java
3. Run the server and Client

