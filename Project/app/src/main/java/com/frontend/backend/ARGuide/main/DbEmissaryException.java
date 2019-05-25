package com.frontend.backend.ARGuide.main;

/**
 * @author Paul-Reftu
 * class that is a descendant of the 'Exception' type, whose instance is thrown during failures in a DatabaseEmissary object
 */
public class DbEmissaryException extends Exception {
    public DbEmissaryException(String errMsg) {
        super(errMsg);
    }
}
