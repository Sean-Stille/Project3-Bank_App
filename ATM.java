import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class ATM{
    public static void main(String[] args){
        int x;
        int intake = -1;
        while(intake != 6){
            try{
                System.out.println("1.  Balance Inquiry \n2.  Mini Statement\n3.  Cash Withdrawal \n4.  Deposit\n5.  PIN Change\n6.  Quit");
                System.out.print("Input: ");
                intake = getInput(1,6);
                System.out.println();
                switch(intake){
                    case 1:
                        //Show balance
                        //probably can just call the SQL straight from here
                        break;
                    case 2:
                        //show 30 day transactions/balance list
                        //probably can just call the SQL straight from here
                        break;
                    case 3:
                        withdraw();
                        break;
                    case 4:
                        deposit();
                        break;
                    case 5:
                        changePin();
                        break;
                    default:
                        break;
                }
            }
            catch(Exception e){
                System.out.println("Please enter a valid input");
                intake = -1;
            }
               
        }
          
    }

    public static boolean callSQL(String query){
        String db = Login.DB;
        String user = Login.USER;
        String password = Login.PASSWORD;
        String url = Login.URL;
        boolean queryWorked = false;
        // create a connection to the database using try-with-resources (auto-closes resources in try() parentheses)
        System.out.println("Attempting connection");
        try (
            Connection conn = DriverManager.getConnection(url + db, user, password);
            Statement stmt = conn.createStatement();
        ) {
            System.out.println("Connection successful ");
            queryWorked = stmt.execute(query);
            ResultSet results = stmt.getResultSet();
            System.out.println("Results:");
            while (results.next()) { // next positions me at the next tuple/row
                String name = results.getString("patron"); // can pass String attribute name
                //String name = results.getString(1); // or can pass int position in tuple, 1-based (not zero-based)

                int age = results.getInt("age"); // can get age as an int or a String
                //String age = results.getString("age");

                System.out.println(" Name = " + name + ", Age = " + age);
            }
            // stmt.close(); // done for you with try-with-resources construct
            // conn.close(); // done for you with try-with-resources construct
        } catch (SQLException e) {
            System.out.println("SQL Error " + e.getMessage());
        }
        return false;
    }

    public static void changePin(){
        int intake = changePinHelper();
        //call SQL
    }

    public static int changePinHelper(){
        System.out.print("Enter new PIN: ");
        int intake = getPINInput(0, 9999);
        System.out.print("Confirm new PIN: ");
        int intake2 = getPINInput(0, 9999);
        if(intake == intake2){
            return intake;
        }
        else{
            System.out.println("PINS did not match, please try again");
            return changePinHelper();
        }
        //change PIN SQL call
    }

    public static void deposit(){
        System.out.print("Please enter the amount to deposit: ");
        double intake = getDoubleInput(0, Double.MAX_VALUE);
        //run SQL to deposit
    }

    public static double OtherWithdraw(){
        System.out.print("Please enter withdrawel amount: ");
        double intake = getDoubleInput(0, Double.MAX_VALUE);
        return intake;
    }

    public static void withdraw(){
        int intake = -1;
        double withdrawel;
        System.out.println("1   $20\n2   $40\n3   $60\n4   $100\n5   $150\n6   OTHER");
        System.out.print("Input: ");
        intake = getInput(1,6);
        switch(intake){
            case 1:
                withdrawel = 20;
                break;
            case 2:
                withdrawel = 40;
                break;
            case 3:
                withdrawel = 60;
                break;
            case 4:
                withdrawel = 100;
                break;
            case 5:
                withdrawel = 150;
                break;
            case 6:
                withdrawel = OtherWithdraw();
                break;
            default:
                break;
        }     
            //call SQL to find out balance and check if withdrawel is over balance  
            //call SQL function to withdraw if valid
    }

    public static int getInput(int min, int max){
        Scanner scan = new Scanner(System.in);
        int input = -1;
        String inputString = "";
        boolean valid = false;
        while(!valid){
            try{
                
                inputString = scan.nextLine();
                input = Integer.parseInt(inputString);
                if(input <= max && input >= min){
                    return input;
                }
                else{
                    throw new Exception();
                }
            }
            catch(Exception e){
                System.out.println("Please enter a valid input");
            }
            
        }
        return -1;
    }

    public static int getPINInput(int min, int max){
        Scanner scan = new Scanner(System.in);
        int input = -1;
        String inputString = "";
        boolean valid = false;
        while(!valid){
            try{
                inputString = scan.nextLine();
                if(inputString.length() != 4){
                    throw new Exception();
                }
                input = Integer.parseInt(inputString);
                if(input <= max && input >= min){
                    return input;
                }
                else{
                    throw new Exception();
                }
            }
            catch(Exception e){
                System.out.println("Please enter a valid input");
            }
            
        }
        return -1;
    }

    public static double getDoubleInput(double min, double max){
        Scanner scan = new Scanner(System.in);
        double input = -1;
        String inputString = "";
        boolean valid = false;
        while(!valid){
            try{
                inputString = scan.nextLine();
                input = Double.parseDouble(inputString);
                if(input <= max && input >= min){
                    return input;
                }
                else{
                    throw new Exception();
                }
            }
            catch(Exception e){
                System.out.println("Please enter a valid input");
            }
            
        }
        return -1;
    }
}
