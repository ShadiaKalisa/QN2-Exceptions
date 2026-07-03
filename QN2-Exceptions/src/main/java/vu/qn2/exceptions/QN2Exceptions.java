package vu.qn2.exceptions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

// --- 1. THE CUSTOM CHECKED EXCEPTION ---

import java.io.FileNotFoundException;

// Extending Exception makes it a checked exception that the compiler forces us to handle
class InsufficientFundsException extends Exception {
    public InsufficientFundsException(String message) {
        super(message);
    }
}

// --- 2. BANK ACCOUNT CLASS ---
class BankAccount {
    private String owner;
    private long balance; // Using whole long numbers for Ugandan Shillings (UGX)

    public BankAccount(String owner, long openingBalance) {
        this.owner = owner;
        this.balance = openingBalance;
    }

    // This method MUST declare 'throws InsufficientFundsException' because it's a checked exception
    public void withdraw(long amount) throws InsufficientFundsException {
        System.out.println("\n[Transaction] Attempting to withdraw UGX " + String.format("%,d", amount) + "...");
        
        if (amount > this.balance) {
            // Throw our custom exception if they don't have enough money
            throw new InsufficientFundsException("Transaction Denied! UGX " + 
                    String.format("%,d", amount) + " exceeds your balance of UGX " + String.format("%,d", balance));
        }
        
        this.balance -= amount;
        System.out.println("Success! New Balance: UGX " + String.format("%,d", balance));
    }
}

// --- 3. MAIN RUNNABLE CLASS (Matches your NetBeans project file name) ---
public class QN2Exceptions {

    public static void main(String[] args) {
        System.out.println("====== Advanced Exception Handling Demo ======");

        // --- PART 1: TESTING THE CUSTOM CHECKED EXCEPTION ---
        BankAccount account = new BankAccount("Mukasa Peter", 500000L); // Account starts with 500,000 UGX

        try {
            // This transaction will pass
            account.withdraw(200000L); 
            
            // This transaction will fail and trigger our catch block
            account.withdraw(400000L); 
            
        } catch (InsufficientFundsException e) {
            // This catches our specific bank exception
            System.err.println("Bank Error Alert: " + e.getMessage());
        }

        
        // --- PART 2: MULTIPLE CATCH BLOCKS WITH TRY-WITH-RESOURCES ---
        System.out.println("\n--- Testing Multiple Catch Blocks & Resource Auto-Closing ---");
        
        // NetBeans will auto-close 'br' even if errors happen inside the parentheses or block
        try (BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
            String line = br.readLine();
            System.out.println("File contents: " + line);
            
            // Let's force an unexpected unchecked error to test hierarchy (e.g. dividing by zero)
            int forceCrash = 10 / 0; 
            
        } catch (FileNotFoundException e) {
            // Specific Catch 1: Triggered if 'data.txt' does not exist in the root folder
            System.out.println("Catch Block 1: The system could not find your file!");
            
        } catch (IOException e) {
            // Specific Catch 2: Triggered if there is a problem reading the actual data line
            System.out.println("Catch Block 2: A general input/output error happened.");
            
        } catch (ArithmeticException e) {
            // Unchecked Catch 3: Handles math calculation blunders
            System.out.println("Catch Block 3: You cannot divide a number by zero!");
            
        } catch (Exception e) {
            // General Catch 4: A catch-all safety net for any other errors we forgot to mention
            // IMPORTANT: In Java, this broad parent Exception must ALWAYS go at the very bottom!
            System.out.println("Catch Block 4: Something else went wrong: " + e.getMessage());
        }
        
        System.out.println("\n====== Program Finished Safely Without Crashing ======");
    }
}

