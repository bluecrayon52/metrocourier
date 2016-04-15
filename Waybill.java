package metrocourier;

import java.io.Serializable;

public class Waybill implements Serializable { 

    private int waybillNumber;
    private String toAddress;
    private String fromAddress;
    
    // empty constructor 
    public Waybill() {
        this(0, null, null);
    }
    // full constructor 
    public Waybill(int number, String receiver, String sender) {
        waybillNumber = number;
        toAddress = receiver;
        fromAddress = sender;
    }
    
    // accessor methods 
    public int getNumber() {
        return waybillNumber;
    }

    public String getDestination() {
        return toAddress;
    }

    public String getSender() {
        return fromAddress;
    }
    // mutator methods 
    public void setNumber(int num){
        waybillNumber = num; 
    }
    
    public void setDestination(String rec){
        toAddress = rec; 
    }
    
    public void setSender(String send){
        fromAddress = send; 
    }

    @Override
    public String toString() {
        return "Waybill:" + waybillNumber + "\n"
                + fromAddress + "\n"
                + toAddress + "\n";
    }
}
