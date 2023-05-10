import java.io.*;
import java.sql.*;
import java.util.Scanner;
public class InputReaders {
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
