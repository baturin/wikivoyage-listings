package org.wikivoyage.listings.cli_parse.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class ModeValidator implements IParameterValidator {

    public static final String  single = "single";
    public static final String  batch = "batch";

    @Override
    public void validate(String s, String s1) throws ParameterException {
        if (!s1.equalsIgnoreCase(single) &&
            !s1.equalsIgnoreCase(batch)) {
            String message = String.format("[%s] is not a correct mode of execution, you need to enter %s or %s", s1, single, batch);
            throw new ParameterException(message);
        }
    }
}