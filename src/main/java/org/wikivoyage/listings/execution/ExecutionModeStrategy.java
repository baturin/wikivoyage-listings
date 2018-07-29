package org.wikivoyage.listings.execution;

import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wikivoyage.listings.ProjectFolders;
import org.wikivoyage.listings.cli_parse.CommandLine;
import org.wikivoyage.listings.retrieval.RetrievalStrategy;

public abstract class ExecutionModeStrategy {

    // Execution Types. Add here if you've added another execution strategy
    public static final int SINGLE = 0;
    public static final int BATCH = 1;
    public              int type = SINGLE; // Default type of execution

    protected static Log log = LogFactory.getLog(ExecutionModeStrategy.class);

    @Setter protected ProjectFolders projectFolders;
    @Setter protected CommandLine cli;

    // Strategy for retrieving the data for each execution mode
    RetrievalStrategy retrievalMethod;

    public ExecutionModeStrategy() {}

    // ExecutionMode is composed of some

    // retrieval method - http download or cache download
    // parsing method (based upon some websites schema)
    // output method (based upon some websites schema)

    public abstract void execute();
}