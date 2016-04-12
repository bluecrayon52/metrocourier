package metrocourier;

import java.util.Stack;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class MetroCourier {

    private static Stack<Waybill> stack = new Stack<>(); 

    public static void main(String[] args) {
        //main method presents menu to the user
        Scanner scan = new Scanner(System.in); //used to read input from the console
        String input = "0"; //default value
        
        //add logic to load saved waybills from saved_waybills file
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

    private static void readWaybills() {
        //method will read all waybills from waybill folder
        System.out.print("Reading waybills...");

        File directory = new File("new_waybills"); // find the directory? 
        File[] wb_Array = directory.listFiles();
        int count = 0;

        for (int i = 0; i < wb_Array.length; i++) {
            
            File new_wb = wb_Array[i]; 
            
            try (Scanner readFile = new Scanner(new_wb)) {

                while (readFile.hasNext()) {

                    Waybill wayBill = new Waybill(
                            stringToInt(readFile.nextLine().substring(8)),
                            readFile.nextLine(),
                            readFile.nextLine());

                    stack.push(wayBill);
                    
                    // delete the waybill file 
                    new_wb.delete();  
                }

                readFile.close();
                count++;
                

            } catch (FileNotFoundException e) {
            }
        }

        System.out.println("done!");
        System.out.println(count + " waybills read, " + stack.size()+ " total waybills ready.");
        System.out.print("Press any key + enter to continue>");
        
        Continue(); 
        
        }
    

    private static void dispatch() {
        //method will dispatch the newest waybill
        System.out.println("Dispatching 1 waybill...");

        Waybill dispatchFile = stack.pop();

        System.out.println("Waybill #:" + dispatchFile.getNumber());
        System.out.println(dispatchFile.getDestination());
        System.out.print("Press any key + enter to continue>");
        
        Continue(); 
        
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
        System.out.print("Press any key + enter to continue>");
        
        Continue(); 
    }

    private static void save() {
        
       // check to see if there is anything to save 
        if(stack.empty()){
            System.out.println("There are no waybills to save, Goodbye!");
            return; 
        }
        
      try{
          
        FileOutputStream fos = new FileOutputStream("saved_waybills.obj"); // or .sir
        
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
        
     // if "saved_waybill.obj" exists
     try{
       FileInputStream fis = new FileInputStream("saved_waybills.obj"); // or .sir
       
               // if(fis.available()!=0){}     
           
       ObjectInputStream ois = new ObjectInputStream(fis);
			stack  = (Stack) ois.readObject();
			ois.close(); 
                        // delete saved_waybills.obj
                        
     }catch(FileNotFoundException e){
         System.out.println(e.getMessage());
      }
      catch(IOException | ClassNotFoundException e){
         System.out.println(e.getMessage());
      }
     
    }
    
    private static void Continue(){
    Scanner cont = new Scanner(System.in); 
    cont.nextLine(); 
}
}
