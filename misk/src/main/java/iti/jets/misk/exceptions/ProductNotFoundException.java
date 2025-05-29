package iti.jets.misk.exceptions;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException (String message){
        super(message);
    }
}
