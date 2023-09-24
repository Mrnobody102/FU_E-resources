package fpt.edu.eresourcessystem.common;

public class AccountNotExistedException extends Exception{
    public AccountNotExistedException(String message) {
        super(message);
    }
}
