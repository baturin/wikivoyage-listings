package org.wikivoyage.listings.cli_parse.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class InputFileValidator implements IParameterValidator {

    private static final List<String> allowedInputFileFormats = new ArrayList<String>(){{
        add("bz2");
        add("xml");
    }};

    @Override
    public void validate(String s, String value) throws ParameterException {
        String extension = getFileExtension(value); // Should be lowercase

        if (!allowedInputFileFormats.contains(extension)) {
            String message = String.format("[%s] is not a valid input file", value);
            throw new ParameterException(message);
        }

        Path pathToValidate = Paths.get(value);
        if (!exists(pathToValidate)) {
            String message = String.format("[%s] does not exist: ", value);
            throw new ParameterException(message);
        }
    }

    private boolean exists(Path p) {
        return (Files.exists(p, LinkOption.NOFOLLOW_LINKS));
    }

    static String getFileExtension(String value) {
        int i = value.lastIndexOf('.');
        if (i >= 0) {
            return value.substring(i+1).toLowerCase();
        }
        return "";
    }
}
