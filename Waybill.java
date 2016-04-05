package metrocourier; 
 

public class Waybill{ 
    private int waybillNumber; 
    private String toAddress; 
    private String fromAddress; 
    
    public Waybill(){
     this(0,null,null);  
    }
    
    public Waybill(int number, String receiver, String sender){
    waybillNumber = number; 
    toAddress = receiver; 
    fromAddress = sender; 
    }
    
    public int getNumber(){
       return waybillNumber; 
    } 
   
    public String getDestination(){
     return toAddress; 
    }
   
    public String getSender(){
       return fromAddress; 
    }
   
    @Override
    public String toString(){
       return "Waybill: <"+waybillNumber+">\n"
             +"Sender: <"+fromAddress+">\n"
             +"Destination: <"+toAddress+">\n"; 
    }
}
