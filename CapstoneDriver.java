//***************************************************************
//
//  Developer:         Cory Munselle
//
//  Program #:         Capstone
//
//  File Name:         CapstoneDriver.java
//
//  Course:            COSC 4301 - Modern Programming
//
//  Due Date:          May 15, 2022
//
//  Instructor:        Fred Kumi
//
//  Description:
//      Provides framework for letting the user interact with and
//		modify the Books database. Offers addition of authors and tables,
//		removal of authors, displaying of authors and books by authors,
//		and modifying an existing author's name.
//
//***************************************************************

import java.io.IOException;
import java.sql.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class CapstoneDriver {

    private Connection connection;
    private final Scanner input;

    private String accountID;
    private String accountPin;
    private String custName;

    //**************************************************************
    //
    //  Method:       Constructor
    //
    //  Description:  Initializes most used variables
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public CapstoneDriver() {
        input = new Scanner(System.in);
        connection = null;

        accountID = "";
        accountPin = "";
        custName = "";

        // Makes sure that if the application is terminated
        // via Ctrl+C the connection is properly shut down
        Runtime.getRuntime().addShutdownHook(new Thread(this::cleanup));
    }

    // **************************************************************
    //
    // Method:      main
    //
    // Description: The main method of the program
    //
    // Parameters:  String array
    //
    // Returns:     N/A
    //
    // **************************************************************
    public static void main(String[] args)
    {
        CapstoneDriver dbDriver = new CapstoneDriver();

        dbDriver.developerInfo();

        dbDriver.connectToDatabase();

        dbDriver.authUser();

        dbDriver.menu();
    }

    // **************************************************************
    //
    // Method:     	connectToDatabase
    //
    // Description: Connects to the oracle database
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void connectToDatabase()
    {
        ConnectToOracleDB dbConnect = new ConnectToOracleDB();

        try {
            dbConnect.loadDrivers();
            connection = dbConnect.connectDriver();
            // I want to be in full control of commits so it's set to off
            connection.setAutoCommit(false);
        }
        catch (Exception exp) {
            System.out.println("Something terrible went wrong");
        }
    }

    // **************************************************************
    //
    // Method:     	menu
    //
    // Description: Displays the different options for the user
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void menu() {

        int choice = 0;

        while (true) {
            System.out.println("Welcome, " + custName + "! Please make a selection: ");
            System.out.printf("%s%n%s%n%s%n%s%n%s%n",
                    "1) View my balance",
                    "2) Withdraw money",
                    "3) Deposit money",
                    "4) Transfer money from one account to the other",
                    "5) Exit");
            if (input.hasNextInt()) {
                try {
                    choice = input.nextInt();
                }
                catch (NoSuchElementException e) {
                    System.err.println("Not an integer. Please provide an integer.");
                    input.next();
                }
            }
            switch (choice) {
                case 1:
                    viewBalance();
                    break;
                case 2:
                    withdrawMoney();
                    break;
                case 3:
                    depositMoney();
                    break;
                case 4:
                    transferMoney();
                    break;
                case 5:
                    System.out.println("Thank you for using BigBank(tm)! Have a nice day!");
                    authUser();
                    break;
                default:
                    System.out.println("Invalid selection. Please make a selection from the options above.");
                    break;
            }
        }
    }

    // **************************************************************
    //
    // Method:     	authUser
    //
    // Description: Authenticates the user by checking the entered values
    //              against the database
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void authUser() {
        PreparedStatement user = null;
        ResultSet custAccount = null;

        // query to get the customer name of the associated account and pin
        String sqlString = "SELECT CUST_NAME FROM Accounts WHERE ACCOUNT_ID = ? AND ACCOUNT_PIN = ?";

        boolean valid = false;

        System.out.print("Thank you for using BigBank(tm)! ");
        while (!valid) {
            System.out.print("Please enter your account number: ");
            try {
                accountID = input.next();
            }
            catch (NoSuchElementException | IllegalStateException e) {
                System.err.println("Failed to capture input.");
            }
            System.out.println();
            System.out.print("Please enter your PIN number: ");
            try {
                accountPin = input.next();
            }
            catch (NoSuchElementException | IllegalStateException e) {
                System.err.println("Failed to capture input.");
            }

            try {
                user = connection.prepareStatement(sqlString);
            }
            catch (SQLException e) {
                System.err.println("Failed to prepare statement.");
            }

            if (user != null) {
                try {
                    // assign the customer ID and PIN to the query and run it
                    user.setString(1, accountID);
                    user.setString(2, accountPin);
                    custAccount = user.executeQuery();
                }
                catch (SQLException e) {
                    System.err.println("Failed to execute query.");
                }
            }

            if (custAccount != null) {
                try {
                    // if the query was successful, grab the name for later use
                    if (custAccount.next()) {
                        custName = custAccount.getString("CUST_NAME");
                        valid = true;
                    }
                    // if the name can't be acquired, user input an invalid value and prompt for more info
                    else {
                        System.out.println(custName);
                        System.out.println("Invalid information provided. Please provide a valid account number and PIN.");
                    }
                }
                catch (SQLException e) {
                    System.out.println("Invalid information provided. Please provide a valid account number and PIN.");
                }
            }
        }

        close(user, custAccount);
    }

    // **************************************************************
    //
    // Method:     	viewBalance
    //
    // Description: Displays the current account holder's checking and
    //              savings balances
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void viewBalance() {
        double checkingBalance = checkBalance(0);
        double savingsBalance = checkBalance(1);

        System.out.printf("%s%.2f%n", "Your current balance in checking is: $", checkingBalance);
        System.out.printf("%s%.2f%n", "Your current balance in checking is: $", savingsBalance);
    }

    // **************************************************************
    //
    // Method:     	checkBalance
    //
    // Description: Helper method to get the account balance of the
    //              current user's checking/savings
    //
    // Parameters:  int accountChoice
    //
    // Returns:     double currentBalance
    //
    // **************************************************************
    public double checkBalance(int accountChoice) {
        Statement query = null;
        ResultSet balance = null;

        double currentBalance = 0.0;

        // This only works because JDBC takes an entire query as a string, so I can essentially "cheat" with string
        // concatenation and sidestep the process of preparing a statement and setting the ID and PIN in the query

        // Get checking amount for current user
        String checking_sql = "SELECT Checking FROM CHECKING WHERE ACCOUNT_ID = '" + accountID + "'";

        // Get savings amount for current user
        String savings_sql = "SELECT Savings FROM SAVINGS WHERE ACCOUNT_ID = '" + accountID + "'";

        try {
            query = connection.createStatement();
        }
        catch (SQLException e) {
            System.err.println("Failed to create statement.");
        }

        // If the account choice is checking (0)
        if (accountChoice == 0 && query != null) {
            try {
                balance = query.executeQuery(checking_sql);
            }
            catch (SQLException e) {
                System.err.println("Failed to execute query.");
            }
        }
        // If the account choice is savings (1)
        else if (accountChoice == 1 && query != null) {
            try {
                balance = query.executeQuery(savings_sql);
            }
            catch (SQLException e) {
                System.err.println("Failed to execute query.");
            }
        }

        if (balance != null) {
            try {
                // grab the current balance
                while (balance.next()) {
                    currentBalance = balance.getDouble(1);
                }
            }
            catch (SQLException e) {
                System.err.println("Failed to retrieve data.");
            }
        }

        // clean up the open statements
        close(query, balance);

        return currentBalance;
    }

    // **************************************************************
    //
    // Method:     	updateBalance
    //
    // Description: Updates the current user's account based on what accountChoice
    //              is passed in. 0 = Withdraw, 1 = Deposit, 2 = Transfer
    //
    // Parameters:  int accountChoice
    //              double amount
    //              String operator1
    //              String operator2
    //
    // Returns:     boolean success
    //
    // **************************************************************
    public boolean updateBalance(int accountChoice, double amount, String operator1, String operator2) {
        Statement query = null;
        boolean success = false;
        int rows = 0;

        // I'm taking "cheating" to the next level by concatenating the operators into the SQL query
        // this lets me, if needed, subtract or add to both savings and checking
        // Right now it's only used for withdrawing and depositing from checking, but it works for savings too

        // Modify the amount in checking based on the current amount in checking
        String checking_sql = "UPDATE Checking SET CHECKING = CHECKING " + operator1 +" " + amount + " WHERE ACCOUNT_ID = '" + accountID + "'";

        // Modify the amount in savings based on the current amount in savings
        String savings_sql = "UPDATE Savings SET SAVINGS = SAVINGS " + operator1 +" " + amount + " WHERE ACCOUNT_ID = '" + accountID + "'";

        // Transfer money from checking to savings (or vice versa)
        String transfer_checking_sql = "UPDATE Checking SET CHECKING = CHECKING " + operator1 +" " + amount + " WHERE ACCOUNT_ID = '" + accountID + "'";
        String transfer_savings_sql = "UPDATE Savings SET SAVINGS = SAVINGS " + operator2 + " " + amount + " WHERE ACCOUNT_ID = '" + accountID + "'";


        try {
            query = connection.createStatement();
        }
        catch (SQLException e) {
            System.err.println("Failed to create statement.");
        }

        // If the account choice is checking (0)
        if (accountChoice == 0 && query != null) {
            try {
                rows = query.executeUpdate(checking_sql);
            }
            catch (SQLException e) {
                System.err.println("Failed to execute update.");
            }
        }
        // If the account choice is savings (1)
        else if (accountChoice == 1 && query != null) {
            try {
                rows = query.executeUpdate(savings_sql);
            }
            catch (SQLException e) {
                System.err.println("Failed to execute update.");
            }
        }
        // If the account choice is transfer (2)
        else if (accountChoice == 2 && query != null) {
            try {
                rows = query.executeUpdate(transfer_checking_sql);
                rows += query.executeUpdate(transfer_savings_sql);
            }
            catch (SQLException e) {
                System.err.println("Failed to execute update.");
                rows = 0;
            }
        }

        // We can assume the query was successful if the number of rows is greater than zero because
        // rows are only returned if they are successfully modified
        if (rows > 0) {
            success = true;
        }

        close(query, null);
        return success;
    }

    // **************************************************************
    //
    // Method:      chooseAccount
    //
    // Description: Helper method to get the account the user wants to change
    //
    // Parameters:  None
    //
    // Returns:     int accountChoice
    //
    // **************************************************************
    public int chooseAccount() {
        boolean valid = false;
        int userSelection = -1;
        int accountChoice = 0;

        // Grab either a 0 to quit, 1 for checking, or 2 for savings
        while (!valid && userSelection != 0) {
            try {
                userSelection = input.nextInt();
            }
            catch (NoSuchElementException e) {
                System.err.println("Not a number. Please provide a number.");
                input.next();
            }
            if (userSelection == 1) {
                valid = true;
            }
            else if (userSelection == 2) {
                accountChoice = 1;
                valid = true;
            }
            else if (userSelection == 0) {
                accountChoice = -1;
                valid = true;
            }
            else {
                System.out.println("Invalid selection. Please make a selection from the available options.");
            }
        }
        return accountChoice;
    }

    // **************************************************************
    //
    // Method:      withdrawMoney
    //
    // Description: Removes the selected amount from the current user's
    //              account and "dispenses" it
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void withdrawMoney() {

        // Helpful double array to make comparisons against later
        double[] amounts = {20, 40, 60, 80, 100, 120, 160, 200};
        boolean valid = false;
        int userSelection = 0;

        System.out.printf("%s%n%s%n%s%n%s", "Please select the account you would like to withdraw money from:", "1) Checking", "2) Savings", "Type 0 to quit: ");
        int accountChoice = chooseAccount();

        if (accountChoice == -1) {
            valid = true;
        }

        while (!valid && userSelection != 9) {
            double currentBalance = checkBalance(accountChoice);

            System.out.println("Please select how much you would like to withdraw: ");
            System.out.printf("%-10s%-10s%n%-10s%-10s%n%-10s%-10s%n%-10s%-10s%n%s%n", "1) $20", "5) $100", "2) $40", "6) $120", "3) $60", "7) $160", "4) $80", "8) $200", "9) Cancel Transaction");

            try {
                userSelection = input.nextInt();
            }
            catch (NoSuchElementException e) {
                //System.err.println("Not a number. Please provide a number.");
                input.next();
            }

            // If user makes a selection outside the available options
            if (userSelection < 1 || userSelection > 9) {
                System.out.println("Please enter a value between 1 and 9.");
            }
            // If user selects quit option
            else if (userSelection == 9) {
                System.out.println("Returning to the main menu...");
            }
            else {
                // If user tries to withdraw more money than they have
                if (currentBalance < amounts[userSelection-1]) {
                    System.out.println("Not enough money in account to make a withdrawal. Please make another selection.");
                }
                // Withdraw the money
                else if (updateBalance(accountChoice, amounts[userSelection-1], "-", "")){
                    System.out.println("Money successfully withdrawn. Please make sure to take the money!");
                    commitChanges();
                    // Stalls execution until enter is pressed (the value doesn't matter since input isn't captured)
                    System.out.println("Press enter to continue...");
                    try {
                        System.in.read();
                    }
                    catch(IOException ignored) {

                    }
                }
                // generic safety error if the withdrawal fails
                else {
                    System.out.println("An error occurred when trying to contact the database. Please try again.");
                }
            }
        }
    }

    // **************************************************************
    //
    // Method:      depositMoney
    //
    // Description: Adds the user provided amount to the user's account
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void depositMoney() {
        double amount = -1;

        System.out.printf("%s%n%s%n%s%n%s", "Please select the account you would like to deposit money into:", "1) Checking", "2) Savings", "Type 0 to quit: ");
        int accountChoice = chooseAccount();

        while (amount != 0 && accountChoice != -1) {
            System.out.printf("%s%n%s","Please type how much you would like to deposit.", "If you would like to return to the main menu, please type 0: ");

            try {
                amount = input.nextDouble();
            }
            catch (NoSuchElementException e) {
                //System.err.println("Not a number. Please provide a number.");
                input.next();
            }
            // if value provided is negative
            if (amount < 0.0) {
                System.out.println("Invalid amount. Please provide a positive number.");
            }
            // if user types zero to exit
            else if (amount == 0) {
                System.out.println("Returning to the main menu...");
            }
            else {
                // Deposit the money
                if (updateBalance(accountChoice, amount, "+", "")) {
                    System.out.println("Money successfully deposited.");
                    commitChanges();
                }
                // Generic error if the update fails
                else {
                    System.out.println("An error occurred when trying to contact the database. Please try again.");
                }
            }
        }
    }

    // **************************************************************
    //
    // Method:      transferMoney
    //
    // Description: Transfers money from the user's checking account
    //              to the user's savings account
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void transferMoney() {

        System.out.printf("%s%n%s%n%s%n%s", "Please select the account you would like to transfer money from:", "1) Checking", "2) Savings", "Type 0 to quit: ");
        int accountChoice = chooseAccount();

        boolean valid = false;
        double amount = -1;
        double checkingBalance = 0.0;

        if (accountChoice == -1) {
            valid = true;
        }
        else {
            checkingBalance = checkBalance(accountChoice);
        }


        while (!valid && amount != 0) {
            System.out.printf("%s%n%s","Please type how much you would like to transfer.", "If you would like to return to the main menu, please type 0: ");

            try {
                amount = input.nextDouble();
            }
            catch (NoSuchElementException e) {
                //System.err.println("Not a number. Please provide a number.");
                input.next();
            }

            // if the user tries to transfer negative money
            if (amount < 0.0) {
                System.out.println("Invalid amount. Please provide a positive number.");
            }
            // if the user tries to transfer more money than they have
            else if (checkingBalance < amount) {
                System.out.println("Amount provided exceeds current amount in account. Please enter a lower value.");
            }
            // if they quit
            else if (amount == 0) {
                System.out.println("Returning to the main menu...");
            }
            else {
                // transfer from checking to savings
                if (accountChoice == 0 && updateBalance(2, amount, "-", "+")) {
                    System.out.println("Money successfully transferred.");
                    commitChanges();
                    valid = true;
                }
                // transfer from savings to checking
                else if (accountChoice == 1 && updateBalance(2, amount, "+", "-")) {
                    System.out.println("Money successfully transferred.");
                    commitChanges();
                    valid = true;
                }
                // generic error in case the update fails
                else {
                    System.out.println("An error occurred when trying to contact the database. Please try again.");
                    try {
                        connection.rollback();
                    }
                    catch (SQLException e) {
                        System.err.println("Failed to revert changes.");
                    }
                }
            }
        }
    }

    // **************************************************************
    //
    // Method:     	commitChanges
    //
    // Description: Helper method to commit changes made to the database
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void commitChanges() {
        try {
            connection.commit();
        }
        catch (SQLException e) {
            System.out.println("Failed to commit changes to the database. Please try again.");
        }
    }

    // **************************************************************
    //
    // Method:     	close
    //
    // Description: Helper method to close unused Statements and ResultSets
    //
    // Parameters:  Statement state,
    //				ResultSet result
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void close(Statement state, ResultSet result) {
        try {
            state.close();
            result.close();
        }
        catch (SQLException | NullPointerException ignored) {

        }
    }

    // **************************************************************
    //
    // Method:     	cleanup
    //
    // Description: Closes the connection upon exiting the program
    //
    // Parameters:  None
    //
    // Returns:     N/A
    //
    // **************************************************************
    public void cleanup() {
        try {
            connection.close();
        }
        catch (SQLException | NullPointerException e) {
            System.err.println("Failed to close connection.");
        }
    }

    //**************************************************************
    //
    //  Method:       developerInfo
    //
    //  Description:  The developer information method of the program
    //
    //  Parameters:   None
    //
    //  Returns:      N/A
    //
    //**************************************************************
    public void developerInfo()
    {
        System.out.println("Name:    Cory Munselle");
        System.out.println("Course:  COSC 4301 Modern Programming");
        System.out.println("Program: Capstone \n");

    } // End of developerInfo
}
