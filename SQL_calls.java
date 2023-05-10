import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class SQL_calls {
    public static String getHistory(int acc_num){
        String outputString =   "|Timestamp          |Description    |Amount         |Balance        |\n" +
                                "+-------------------+---------------+---------------+---------------+\n";
        String query = "SELECT  transaction_timestamp, description, amount  FROM AccountTransaction WHERE acc_num = "+ acc_num +
            " AND transaction_timestamp >= DATE_SUB(NOW(), INTERVAL 30 DAY);";
        ArrayList<ArrayList<String>> results = callSQL(query, "getHistory");
        ArrayList<Double> currentBalances = new ArrayList<>();
        double balance = getAccountBalanceByAccount(acc_num);

        for(int i = 0; i < results.get(0).size(); i++){
            double sum = 0;
            for ( int j = i+1; j < results.get(0).size(); j++){
                sum += Double.parseDouble(results.get(2).get(j));
            }
            currentBalances.add(balance - sum);
        }

        for (int i = 0; i< results.get(0).size(); i++){
            outputString += String.format("|%1$-15s|%2$-15s|%3$-15s|%4$-15s|\n", 
                results.get(0).get(i), results.get(1).get(i), results.get(2).get(i), currentBalances.get(i));
        }

        return outputString;
    }

    public static boolean updatePIN(int accountNum, int newPIN){
        String formattedPIN = String.format("%04d", newPIN);
        String query = "UPDATE BankCustomer SET pin = "+ formattedPIN + 
            " WHERE cust_name IN (SELECT cust_name FROM Account WHERE acc_num = "+ accountNum +");"; 
        callSQL(query, "updatePIN");
        return true;
    }

    public static boolean depositToAccount(int accountNum, double deposit){
        double balance = getAccountBalanceByAccount(accountNum);
        balance = balance + deposit;
        String query = "INSERT INTO AccountTransaction (acc_num, description, amount, transaction_timestamp)" +
         "VALUES("+ accountNum  +", 'ATM DEPOSIT', " + deposit + ", NOW());";
        callSQL(query,"depositToAccount");
        updateBalance(accountNum, balance);
        return true;
    }

    public static boolean updateBalance(int acc_num, double newBalance){
        String query = "UPDATE Account SET balance = "+ newBalance +" WHERE acc_num = "+ acc_num +";";
        callSQL(query, "updateBalance");
        return true;
    }

    public static boolean withdrawFromAccount(int accountNum, double withdrawel){
        double balance = getAccountBalanceByAccount(accountNum);
        balance = balance - withdrawel;
        String query = "INSERT INTO AccountTransaction (acc_num, description, amount, transaction_timestamp)" +
         "VALUES("+ accountNum  +", 'ATM WITHDRAWEL', " + (0-withdrawel) + ", NOW());";
        callSQL(query,"withdrawFromAccount");
        updateBalance(accountNum, balance);
        return true;
    }

    public static double getAccountBalanceByAccount(int accountNum){
        String query = "SELECT balance FROM Account WHERE acc_num = "+ accountNum + ";";
        
        ArrayList<ArrayList<String>> results = callSQL(query, "getAccountBalanceByAccount");
        return Double.parseDouble(results.get(0).get(0));
    }

    public static int getPINByAccount(int accountNum){
        String query = "SELECT pin from Account ac  INNER JOIN BankCustomer bc on ac.cust_name = bc.cust_name WHERE acc_num = "+ accountNum + ";";
        ArrayList<ArrayList<String>> results = callSQL(query, "getPINByAccount");
        return Integer.parseInt(results.get(0).get(0));
    }

    public static ArrayList<Integer> getAccountNums(){
        String query = "SELECT acc_num FROM Account;";
        ArrayList<Integer> resultsArray = new ArrayList<>();
        ArrayList<ArrayList<String>> results = callSQL(query,"getAccountNums");
        ArrayList<String> accounts = results.get(0);
        for (int i = 0; i < accounts.size(); i++){
            resultsArray.add( Integer.parseInt(accounts.get(i)));
        }
        return resultsArray;
    }

    private static ArrayList<ArrayList<String>> callSQL(String query, String queryType){
        String db = Login.DB;
        String user = Login.USER;
        String password = Login.PASSWORD;
        String url = Login.URL;
        boolean queryWorked = false;
        ArrayList<ArrayList<String>> resultsList = new ArrayList<>();
        // create a connection to the database using try-with-resources (auto-closes resources in try() parentheses)
        //System.out.println("Attempting connection");
        try (
            Connection conn = DriverManager.getConnection(url + db, user, password);
            Statement stmt = conn.createStatement();
        ) {
            //System.out.println("Connection successful ");
            queryWorked = stmt.execute(query);
            ResultSet results = stmt.getResultSet();
            //System.out.println("Results:");
            switch(queryType){
                case "getAccountNums":
                    ArrayList<String> accountList = new ArrayList<>();
                    while (results.next()) { 
                        accountList.add(results.getString("acc_num"));
                    }
                    resultsList.add(accountList);
                    break;
                case "getPINByAccount":
                    ArrayList<String> pin = new ArrayList<>(); //This should only return one but will still be a list for the sake of code reuse 
                    while (results.next()) { 
                        pin.add(results.getString("pin"));
                    }
                    resultsList.add(pin);
                    break;
                case "getAccountBalanceByAccount":
                    ArrayList<String> balance = new ArrayList<>(); //This should only return one but will still be a list for the sake of code reuse 
                    while (results.next()) { 
                        balance.add(results.getString("balance"));
                    }
                    resultsList.add(balance);
                    break;
                case "getHistory":
                    ArrayList<String> dates = new ArrayList<>();
                    ArrayList<String> descriptions = new ArrayList<>();
                    ArrayList<String> amounts = new ArrayList<>();
                    while (results.next()) { 
                        dates.add(results.getString("transaction_timestamp"));
                        descriptions.add(results.getString("description"));
                        amounts.add(results.getString("amount"));
                    }
                    resultsList.add(dates);     
                    resultsList.add(descriptions); 
                    resultsList.add(amounts);           
                    break;
                default:
                    break;
            }
            // stmt.close(); // done for you with try-with-resources construct
            // conn.close(); // done for you with try-with-resources construct
        } catch (SQLException e) {
            System.out.println("SQL Error " + e.getMessage());
        }
        return resultsList;
    }
}
