package net.thejrdev;

/**
 * Exception for when the configuration file is missing a critical argument
 */
public class MissingConfigArgument extends Exception{

    /**
     * @param error The error message
     */
    public MissingConfigArgument(String error){
        super(error);
    }
}
