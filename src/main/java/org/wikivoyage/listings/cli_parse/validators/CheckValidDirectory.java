package org.wikivoyage.listings.cli_parse.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.Paths;

public class CheckValidDirectory implements IParameterValidator {
    @Override
    public void validate(String s, String s1) throws ParameterException {
        if (!Files.isDirectory(Paths.get(s1))) {
            throw new ParameterException("Invalid directory");
        }
    }
}
