# 🏗️ Extensive File Manager (EFM) - Architecture Guidelines

This document establishes the structural standards for the Extensive File Manager (EFM) project and serves as a comprehensive guiding manifesto for developers contributing to the codebase.

EFM is a meticulously designed Java project that strictly adheres to **Object-Oriented Programming (OOP)** paradigms and the **SOLID** software design philosophy.

## 🧱 The SOLID Philosophy in EFM

* **S - Single Responsibility Principle (SRP):** A class must have one, and only one, specific job. "God Objects"—complex classes that handle multiple responsibilities—are strictly prohibited. There should never be more than one reason to modify a class.
* **O - Open/Closed Principle (OCP):** EFM is open for extension but closed for modification. When adding a new feature, developers must introduce new modules or classes rather than altering existing, tested code.
* **L - Liskov Substitution Principle (LSP):** Derived classes must be completely substitutable for their base classes without altering the correctness of the program. In EFM, if a method expects an `EntityRemover`, passing a `PermanentRemover` or a `TrashRemover` should work seamlessly without the parent method needing to know which specific child class is operating.
* **I - Interface Segregation Principle (ISP):** A class should never be forced to implement interfaces and methods it does not use. EFM strictly prohibits overly broad, "fat" interfaces. Interfaces must be granular, lean, and highly specific, acting as modular puzzle pieces.
* **D - Dependency Inversion Principle (DIP):** High-level modules must not depend on low-level modules; both must depend on abstractions (interfaces). In EFM, structures are connected like plugs and sockets via flexible interfaces, minimizing tight coupling between modules.

## 🧩 Module Characteristics & Independence

The EFM architecture is built on the philosophy of **High Cohesion and Loose Coupling**. Modules are highly independent.

For instance, the `core` module is designed so robustly that it could be completely detached and integrated into an entirely different Java application as a library. It exclusively handles low-level file system operations and is entirely blind to the existence of other EFM components. Similarly, the `command` module is strictly responsible for executing commands; it has no knowledge of whether the input came from a CLI terminal or a GUI. This plug-and-play modularity ensures maximum maintainability.

### Standard Module Components

Each major domain in EFM is divided into the following sub-packages:

* **api/ (Application Programming Interface):** The "contract" layer. It contains interfaces and rules. This is the only part of a module exposed to the outside world.
* **impl/ (Implementation):** The "worker" layer. It contains the concrete classes that perform the actual logic defined in the API. These classes are strictly hidden from external modules and can only be accessed indirectly via interfaces.
* **dto/ (Data Transfer Object):** The "courier" layer. These are pure data structures used for inter-module communication, ensuring that logic and data transfer remain separated.
* **exception/:** The "business rules" layer. Contains custom exceptions that define what operations are illegal within that specific domain.

### Domain Isolation (Separation of Concerns)

Every module must perform its specialized task exclusively. For example:

* Only the `presentation` (UI/CLI) layer is permitted to use `System.out.print()`. If a core service needs to report information, it must package it into a DTO and send it up the chain.
* Only the `core` module is permitted to interact with the `java.nio` library. No other class in the system should import file system libraries.

## 🍽️ The Michelin-Star Restaurant Analogy: EFM Data Flow in Action

To fully grasp the separation of concerns and the strict boundaries within EFM, imagine the application as a highly efficient, Michelin-star restaurant. Every class and package has a specific role, and they never step on each other's toes.

Here is the complete lifecycle of a customer's request (a file operation) passing through the EFM architecture:

### 1. The Front of House (UI / Presentation Layer)
* **The Customer (User):** Enters the restaurant and asks for something complex, like *"I want you to aggressively throw away this folder and everything inside it!"* (Types: `rm -r -v myFolder`).
* **The Waiter (ui.CommandLineInterface):** The only person allowed to talk to the customer. The Waiter takes the order and will eventually bring back the result. The Waiter never enters the kitchen and has no idea how to cook.
* **The Translator / Notepad (ui.CommandParser):** The Customer's words are messy. The Waiter uses this tool to translate the raw request into a standardized, easy-to-read ticket.
* **The Order Ticket (command.api.CommandContext):** The finalized ticket. It clearly states the requested action (`rm`), the modifiers/flags (`-r`, `-v`), and the target (`myFolder`).

### 2. The Kitchen Pass (Command Layer)
* **The Menu Registry (config.RegistryConfig & command.api.CommandRegistry):** The routing system that knows exactly which Sous-Chef handles which dish.
* **The Sous-Chefs / Expediters (command.impl.RmCommand, CpCommand):** They receive the CommandContext ticket. 
    * **Crucial Rule:** They do not cook! Their job is to read the ticket, validate the flags (e.g., *"Is this a valid combination?"*), and pass the exact instructions to the specific Master Chefs. They act as the bridge between the Waiter and the actual Kitchen.

### 3. The Back of House (Core Layer)
* **The Kitchen Contracts (core.api.EntityRemover, EntityCopier):** The Sous-Chef (`RmCommand`) shouts, *"I need a Remover!"* They do not care who the chef is, as long as they are certified under the `EntityRemover` contract.
* **The Master Chefs (core.impl.PermanentRemover, TrashBinRemover):** The heavy lifters. These are the only staff members allowed to touch the raw ingredients and kitchen appliances (The `java.nio` library / File System).
    * The `PermanentRemover` chef uses the incinerator (permanent delete).
    * The `TrashBinRemover` chef throws it in the alley dumpster (recycle bin).
    * The Sous-Chef doesn't know which chef did it; they just know the job was done.
* **The Health Inspectors (core.exception.*):** Strict rules govern the kitchen. If a chef is told to delete a directory but the `-r` (recursive) flag wasn't on the ticket, the Health Inspector immediately shuts it down by throwing an `IllegalDirectoryOperationException`. The cooking stops instantly.

### 4. Serving the Dish (Return Flow via DTOs)
* **The Plated Tray (core.dto.OperationResult):** Once the Master Chef finishes the job (or fails), they place the raw results (list of deleted files or a failure status) onto a standard tray (`OperationResult`) and hand it back to the Sous-Chef.
* **The Receipt & Garnish (command.api.CommandResult):** The Sous-Chef (`RmCommand`) takes the raw tray, formats it nicely, adds a clear message (*"Successfully removed 5 items"* or *"Failed to remove"*), and turns it into a `CommandResult`.
* **Bon Appétit:** The Waiter (`ui.CommandLineInterface`) takes this finalized `CommandResult` and simply reads the message aloud to the Customer (`System.out.println`). The Waiter doesn't care if it's a success or an error; they just serve what they are given.

### 🌟 Why This Matters (The Ultimate Benefit)

Because of this strict separation:

* If we want to change the menu language (e.g., build a Graphical User Interface instead of a CLI), we only fire the **Waiter** (`ui`). The Chefs (`core`) keep cooking exactly as before.
* If we want to change how files are deleted (e.g., delete them from AWS Cloud instead of a local hard drive), we only hire a new **Master Chef** (`core.impl.CloudRemover`). The Waiters and Sous-Chefs won't even notice the change.

## 🛑 Developer Checklist: Ask Before You Code

Before committing any new module, class, or method, developers must evaluate the following:

* **Am I in the right module?** * *Scenario:* I am creating a `Command` class. I must not place this in the `core` module; I must navigate to `command.impl`.
* **Is this class getting bloated?** * *Scenario:* I am adding a new method, but the class is starting to feel overwhelming. This is a "Code Smell" indicating a violation of the Single Responsibility Principle. Consider breaking the class into smaller, specialized sub-classes.
* **Is this the module's responsibility?** * *Scenario:* I want to add a print statement to log an error inside the `command` module. 
    * *Correction:* This module is not authorized to print to the screen. It must return a `CommandResult` DTO, and the UI module will handle the printing.
* **Where does this helper method belong?** * *Scenario:* I am writing a helper method for the UI. You must deeply question the dependencies and purpose of the module before arbitrarily placing utility methods.
* **Static vs. Instance?** * *Scenario:* If a method dictates behavior that might be swapped or requires polymorphic communication between modules, it must be an instance method governed by an interface. If it is a pure, stateless mathematical operation (like a calculator), it should be a `static` utility.
* **Are my dependency arrows pointing the right way?** * Lower-level modules must NEVER know about higher-level modules. `EntityRemover` (`core`) must never import or know about `RmCommand` (`command`). Conversely, `RmCommand` only knows about `EntityRemover` (the Interface), absolutely never the implementation (`PermanentRemover`).
