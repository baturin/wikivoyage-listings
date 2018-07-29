package org.wikivoyage.listings.cli_parse.converters;

import com.beust.jcommander.IStringConverter;
import org.wikivoyage.listings.execution.BatchMode;
import org.wikivoyage.listings.execution.ExecutionModeStrategy;
import org.wikivoyage.listings.execution.SingleMode;

import static org.wikivoyage.listings.cli_parse.validators.ModeValidator.single;

/**
 * The mode is only being passed in as a string just for readabilities concern.
 * This converts that string to the correct integer mode value. We can assume that
 * the string passed in are either single or batch as it has already been through
 * the ModeValidator.class which would've thrown an except if it wasn't and would reach this far.
 */
public class ModeToExecutionStrategyConverter implements IStringConverter<ExecutionModeStrategy> {

    @Override
    public ExecutionModeStrategy convert(String s) {
        if (s.equalsIgnoreCase(single)) {
            return new SingleMode();
        }
        else {
            return new BatchMode();
        }
    }
}