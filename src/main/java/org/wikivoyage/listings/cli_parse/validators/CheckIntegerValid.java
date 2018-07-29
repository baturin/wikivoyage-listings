package org.wikivoyage.listings.cli_parse.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class CheckIntegerValid implements IParameterValidator {
    @Override
    public void validate(String s, String s1) throws ParameterException {
        try {
            Integer.parseInt(s1);
        }
        catch (NumberFormatException e) {
            String message = "The value " + s1 + " is not valid, value must be an Integer and greater than 0";
            throw new ParameterException(message);
        }
    }
}
