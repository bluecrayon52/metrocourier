package metrocourier;

import java.util.Stack;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class MetroCourier {

    private static Stack<Waybill> stack = new Stack<>();

    public static void main(String[] args) {
        //main method presents menu to the user
        Scanner scan = new Scanner(System.in); //used to read input from the console
        String input = "0"; //default value

        //load saved waybills from saved_waybills.obj file
        loadSavedStack();

        //continue to present the menu until the user enters 4
        while (!input.equals("4")) {
            //print the menu
            System.out.println("********************************************************");
            System.out.println("*                   Metro Courier                      *");
            System.out.println("********************************************************");
            System.out.println("*              Select an option below:                 *");
            System.out.println("* 1 - Read Waybills                                    *");
            System.out.println("* 2 - Immediate Dispatch                               *");
            System.out.println("* 3 - End of Day Dispatch                              *");
            System.out.println("* 4 - Save and Exit                                    *");
            System.out.println("********************************************************");
            System.out.print(">");

            //try and read menu selection
            try {
                input = scan.nextLine();
                input = input.trim();
            } catch (Exception ex) {
                //unknown exception
                System.out.println("Unknown error: " + ex);
                System.exit(1);//exit the program will error of 1
            }

            //process menu action and call coresponding method
            switch (input) {
                case "1":
                    readWaybills();
                    break;
                case "2":
                    dispatch();
                    break;
                case "3":
                    endOfDay();
                    break;
                case "4":
                    save();
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }
    }

    //method will read all waybills from waybill folder

    private static void readWaybills() {

        // to avoid NullPointerException, search for waybill directory first
        // if "new_waybills" doesn't exist...
        if (!Files.isReadable(Paths.get("new_waybills"))) {
            System.out.println("The waybill directory cannot be found");
            System.out.print("Press enter to continue>");

            Continue();
            return;
        }

        // begin insanciating waybills from txt doc Strings and push to stack
        System.out.print("Reading waybills...");
        
        // only checks in local directory 
        File directory = new File("new_waybills");
        
        // an array of file paths 
        File[] wb_Array = directory.listFiles();
       
        int count = 0; // number of waybills read 

        for (int i = 0; i < wb_Array.length; i++) {
            
            File new_wb = wb_Array[i];
            
            // instanciate a waybill obj 
            Waybill wayBill = new Waybill(); 
            int lineCount = 0; // number of properly read lines from txt
            
            try (Scanner readFile = new Scanner(new_wb)) {
       
             // only accept properly formatted txt files 
             // with non empty data fields 
                
             if(readFile.hasNext()){
                String number = readFile.nextLine().trim();
                   
                if(number.length()>= 9){
                    wayBill.setNumber(stringToInt(number.substring(8)));
                    lineCount++;
                    }
             }
             if(readFile.hasNext()){   
                String dest = readFile.nextLine().trim(); 
                
                if(dest.length() >= 8){
                    wayBill.setDestination(dest);
                    lineCount++;
                    }
             }
             if(readFile.hasNext()){
                String sender = readFile.nextLine().trim(); 
                
                if(sender.length()>= 13){
                    wayBill.setSender(sender);
                    lineCount++; 
                    }
             }  
                // if all three waybill variables are set 
                if(lineCount == 3){
                    stack.push(wayBill); 
                    new_wb.delete(); 
                    count++; 
                }

                readFile.close();

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("done!");
        
        if(count < wb_Array.length){
            // calculates number of txt files not read and not deleted 
            int junkFileCount = wb_Array.length - count; 
            
            // alternative print statement 
            System.out.println(junkFileCount+" waybill(s) have missing data/n"
                    + "or were not properly formatted.\n"
            +"They have been left in the waybill folder for review and editing.");  
        }
        
        System.out.println(count + " waybills read, " + stack.size() + " total waybills ready.");
        System.out.print("Press enter to continue>");

        Continue();

    }

    //method will dispatch the newest waybill

    private static void dispatch() {

        // avoid EmptyStackException  
        if (!stack.empty()) {

            System.out.println("Dispatching 1 waybill...");

            Waybill dispatchFile = stack.pop();

            System.out.println("Waybill #:" + dispatchFile.getNumber());
            System.out.println(dispatchFile.getDestination());
        } else {
            // alternative print statement 
            System.out.println("No waybills to dispatch.");
        }

        System.out.print("Press enter to continue>");

        Continue();

    }

    //method will queue the waybills to the output directory

    private static void endOfDay() {

        // increment for print statement 
        int count = 0;

        try (PrintWriter outputFile = new PrintWriter("waybill_queue.txt")) {

            // avoid EmptystackException 
            while (!stack.empty()) {
                Waybill dispatchFile = stack.pop();

                outputFile.println(dispatchFile.toString());
                outputFile.println();

                count++;
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        if (count == 0) {
            // alternative print statement 
            System.out.println("There are no waybills left to dispatch.");
        } else {
            System.out.println(count + " outstanding waybills have been queued and sent.");
        }

        System.out.print("Press enter to continue>");

        Continue();
    }
    // method saves stack object in local directory and exits 
    // new obj overwrites previously saved stack 
    private static void save() {

        try {
            FileOutputStream fos = new FileOutputStream("saved_waybills.obj"); // or .sir

            ObjectOutputStream oos = new ObjectOutputStream(fos); // IOException 
            oos.writeObject(stack);  // InvalidClassException
            oos.close(); // IOException 

            System.out.println("Saved. Goodbye!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (InvalidClassException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
    //method attempts to load a stack object from local directory 
    // nothing is done is the obj doesn't yet exist 
    // loads empty stack if empty stack was saved 
    private static void loadSavedStack() {

        try {
            FileInputStream fis = new FileInputStream("saved_waybills.obj"); // or .sir

            ObjectInputStream ois = new ObjectInputStream(fis);
            stack = (Stack) ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            // do nothing if there is no saved stack in local directory 
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }
   // method waits for user to press enter before returning to main 
    private static void Continue() {
        Scanner cont = new Scanner(System.in);
        cont.nextLine();
    }
    
   // method converts a string to an integer 
    private static int stringToInt(String s) {
        return Integer.parseInt(s);
    }
}
