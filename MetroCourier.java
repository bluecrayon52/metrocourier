package metrocourier;

import java.util.Stack;
import java.io.*;
import java.util.Scanner;


public class MetroCourier {

    private static Stack<Waybill> stack = new Stack<>(); // should this be global? 

    public static void main(String[] args) {
        //main method presents menu to the user
        Scanner scan = new Scanner(System.in); //used to read input from the console
        String input = "0"; //default value
        loadSavedStack(); 
        
        //add logic to load saved waybills from saved_waybills file
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

    private static void readWaybills() {
        //method will read all waybills from waybill folder
        System.out.print("Reading waybills...");

        File directory = new File("new_waybills"); // find the directory? 
        File[] wb_Array = directory.listFiles();
        int count = 0;

        for (File new_wb : wb_Array) {

            try (Scanner readFile = new Scanner(new_wb)) {

                while (readFile.hasNext()) {

                    Waybill wayBill = new Waybill(
                            stringToInt(readFile.nextLine().substring(9)),
                            readFile.nextLine().substring(8),
                            readFile.nextLine().substring(13));

                    stack.push(wayBill);

                }

                readFile.close();
                count++;

            } catch (FileNotFoundException e) {
            }
        }

        //add logic here
        System.out.println("done!");
        System.out.println(count + " waybills read, " + count + " total waybills ready.");
        System.out.print("Press any key to continue>");
        //add logic here
    }

    private static void dispatch() {
        //method will dispatch the newest waybill
        System.out.println("Dispatching 1 waybill...");

        Waybill dispatchFile = stack.pop();

        System.out.println("Waybill #: <" + dispatchFile.getNumber() + ">");
        System.out.println("Waybill Address: <" + dispatchFile.getDestination() + ">");
        System.out.print("Press any key to continue>");
        //add logic here
    }

    private static void endOfDay() {

        try (PrintWriter outputFile = new PrintWriter("waybill_queue.txt")) {

            while (!stack.empty()) {
                Waybill dispatchFile = stack.pop();

                outputFile.println(dispatchFile.toString());
                outputFile.println();
            }

        } catch (FileNotFoundException e) {

        }

        //method will queue the waybills to the output directory
        System.out.println("Y outstanding waybills have been queued and sent.");
        //add logic here 
        System.out.print("Press any key to continue>");
        //add logic here
    }

    private static void save() {
        
      try{
          
        FileOutputStream fos = new FileOutputStream("saved_waybills.obj"); // obj file? 
        
        ObjectOutputStream oos = new ObjectOutputStream(fos); // IOException 
			oos.writeObject(stack);  // InvalidClassException
			oos.close(); // IOException 
                       
        System.out.println("Saved. Goodbye!");
        
         
        
      } catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        catch(InvalidClassException e){
            System.out.println(e.getMessage());
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }

    private static int stringToInt(String s) {
        return Integer.parseInt(s);
    }
    
    private static void loadSavedStack(){
     // if file doesn't exist, make it, 
     // if the file is empty, do nothing 
     // else do the following 
     try{
       FileInputStream fis = new FileInputStream("saved_waybills.obj");
       
       ObjectInputStream ois = new ObjectInputStream(fis);
			stack  = (Stack) ois.readObject();
			ois.close(); 
                        
     }catch(FileNotFoundException e){
         System.out.println(e.getMessage());
      }
      catch(IOException | ClassNotFoundException e){
         System.out.println(e.getMessage());
      }
    }
}
