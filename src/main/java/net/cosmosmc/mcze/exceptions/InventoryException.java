package net.cosmosmc.mcze.exceptions;


public class InventoryException extends Exception {

    private String msg;

    public InventoryException(String msg) {
        this.msg = msg;
    }

}
