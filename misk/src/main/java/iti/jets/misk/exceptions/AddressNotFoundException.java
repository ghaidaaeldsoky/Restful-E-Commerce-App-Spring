package iti.jets.misk.exceptions;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException(String message){
        super(message);
    }
}
