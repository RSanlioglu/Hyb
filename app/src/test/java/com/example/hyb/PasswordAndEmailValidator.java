package com.example.hyb;

import static org.junit.Assert.*;

import org.junit.Test;

public class PasswordAndEmailValidator {

    @Test
    public void passwordIsValid() {
        String password = "HybAdmin1"; //Password is between 8 char and 15. It has at least one uppercase and one lowercase char.
        assertFalse(RegisterActivity.isInvalidPassword(password));
    }

    @Test
    public void passwordIsInValidNoUpperCaseChar() {
        String password = "hybadmin1"; //Password has no uppercase characters. Not valid
        assertTrue(RegisterActivity.isInvalidPassword(password));
    }

    @Test
    public void passwordIsInValidNoLoweCaseChar() {
        String password = "HYBADMIN1"; //Password has no lowercase characters. Not valid
        assertTrue(RegisterActivity.isInvalidPassword(password));
    }

    @Test
    public void passwordIsInValidNoNumbers() {
        String password = "HybAdmin"; //Not Valid. No Numbers
        assertTrue(RegisterActivity.isInvalidPassword(password));
    }

    @Test
    public void passwordIsInvalidTooLong() {
        String password = "HybSystemAdministratorPassword"; //Not Valid. Too long
        assertTrue(RegisterActivity.isInvalidPassword(password));
    }

    @Test
    public void passwordIsInvalidTooShort() {
        String password = "Pas1";
        assertTrue(RegisterActivity.isInvalidPassword(password));
    }

    @Test
    public void validEmail() {
        String email = "ramazan@hiof.no";  //Valid email
        assertTrue(RegisterActivity.isValidEmail(email));
    }

    @Test
    public void invalidEmail() {
        String email = "notValidEmail.com";
        assertFalse(RegisterActivity.isValidEmail(email));
    }
}
