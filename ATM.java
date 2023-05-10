import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
public class ATM{
    public static void main(String[] args){
        int accountNum = -1;
        int pin = -1;
        boolean valid = false;
        ArrayList<Integer> accounts = SQL_calls.getAccountNums();

        while(!valid){
            System.out.print("Enter Account Number: ");
            accountNum = InputReaders.getInput(0, Integer.MAX_VALUE);
            if (accounts.contains(accountNum)){
                System.out.print("Please enter PIN: ");
                pin = InputReaders.getPINInput(0, 9999);
                if (pin == SQL_calls.getPINByAccount(accountNum) ){
                    System.out.println("Login Successful\n");
                    loggedIn(accountNum);
                }
                else{
                    System.out.println("Invalid PIN");
                }
            }
            else{
                System.out.println("Invalid Account Number"); //This might not be the best way from a security standpoint
             }
            
        }
        

        //SQL_calls.callSQL("SELECT acc_num, pin from Account ac INNER JOIN BankCustomer bc on ac.cust_name = bc.cust_name;");
        
          
    }

    public static void loggedIn(int acc_num){
        int intake = -1;
        while(intake != 6){
            try{
                System.out.println("1.  Balance Inquiry \n2.  Mini Statement\n3.  Cash Withdrawal \n4.  Deposit\n5.  PIN Change\n6.  Quit");
                System.out.print("Input: ");
                intake = InputReaders.getInput(1,6);
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

    public static void changePin(){
        int intake = changePinHelper();
        //call SQL
    }

    public static int changePinHelper(){
        System.out.print("Enter new PIN: ");
        int intake = InputReaders.getPINInput(0, 9999);
        System.out.print("Confirm new PIN: ");
        int intake2 = InputReaders.getPINInput(0, 9999);
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
        double intake = InputReaders.getDoubleInput(0, Double.MAX_VALUE);
        //run SQL to deposit
    }

    public static double OtherWithdraw(){
        System.out.print("Please enter withdrawel amount: ");
        double intake = InputReaders.getDoubleInput(0, Double.MAX_VALUE);
        return intake;
    }

    public static void withdraw(){
        int intake = -1;
        double withdrawel;
        System.out.println("1   $20\n2   $40\n3   $60\n4   $100\n5   $150\n6   OTHER");
        System.out.print("Input: ");
        intake = InputReaders.getInput(1,6);
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

    
}
