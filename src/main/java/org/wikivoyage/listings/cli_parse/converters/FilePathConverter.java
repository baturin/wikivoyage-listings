package org.wikivoyage.listings.cli_parse.converters;

import com.beust.jcommander.IStringConverter;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathConverter implements IStringConverter<Path> {

    @Override
    public Path convert(String s) {
        return Paths.get(s);
    }

}
