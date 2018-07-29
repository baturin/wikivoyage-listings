package org.wikivoyage.listings.cli_parse.converters;

import com.beust.jcommander.IStringConverter;
import org.wikivoyage.listings.output.OutputFormat;

public class OutputFormatConverter implements IStringConverter<OutputFormat> {

    @Override
    public OutputFormat convert(String s) {
        // If we're at this point, we've validated that there's an valid OutputFormat extension that matches 's'
        return OutputFormat.getExtensionsToFormatType().get("." + s.toLowerCase());
    }
}