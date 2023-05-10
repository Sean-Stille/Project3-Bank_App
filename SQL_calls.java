import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class SQL_calls {

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

    public static ArrayList<ArrayList<String>> callSQL(String query, String queryType){
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
