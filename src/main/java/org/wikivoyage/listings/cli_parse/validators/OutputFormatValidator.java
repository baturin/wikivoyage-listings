package org.wikivoyage.listings.cli_parse.validators;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;
import org.wikivoyage.listings.output.OutputFormat;
import java.util.Set;

public class OutputFormatValidator implements IParameterValidator {

    public static Set<String> allowedOutputsExtensions = OutputFormat.getExtensionsToFormatType().keySet();

    @Override
    public void validate(String s, String s1) throws ParameterException {
        if (!allowedOutputsExtensions.contains("." + s1.toLowerCase())) {
            String message = String.format( "The output format supplied is not one we support. Please use one of " +
                                            "the following formats: \n %s", allowedOutputsExtensions.toString());
            throw new ParameterException(message);
        }
    }
}