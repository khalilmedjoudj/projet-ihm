# Projet IHM - Fast Food Management System

A Java-based fast food management application with a graphical user interface.

## Features

- **Menu Management**: Browse and manage food items
- **Order Processing**: Create and manage customer orders
- **Cashier System**: Process payments and transactions
- **Database Integration**: SQLite database for data persistence

## Project Structure

- `Main.java` - Application entry point
- `MainFrame.java` - Main application window
- `MenuPanel.java` - Menu display and management
- `CommandesPanel.java` - Order management interface
- `CaissePanel.java` - Cashier/payment interface
- `DatabaseManager.java` - Database operations
- `Plat.java` - Food item model
- `Commande.java` - Order model
- `LigneCommande.java` - Order line item model

## Requirements

- Java Development Kit (JDK) 8 or higher
- SQLite JDBC driver (included in lib folder)

## How to Run

1. Compile all Java files
2. Run the Main class:
   ```bash
   java Main
   ```

## Database

The application uses SQLite database (`fastfood.db`) to store:
- Menu items (plats)
- Orders (commandes)
- Order details (lignes_commande)

## Author

Created for IHM (Interface Homme-Machine) course project
