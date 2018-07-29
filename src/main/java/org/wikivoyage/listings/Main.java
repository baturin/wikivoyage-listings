package org.wikivoyage.listings;

import org.wikivoyage.listings.cli_parse.CommandLine;
import org.wikivoyage.listings.execution.ExecutionModeStrategy;

public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.parseAndRun(args);
    }

    private void parseAndRun(String[] args) {
        // Parse CLI
        CommandLine cli = new CommandLine();
        cli.parse(args);

        // Create project structure in FileSystem
        ProjectFolders folders = new ProjectFolders(cli.listingsDir, cli.dumpsCacheDir, cli.workingDir);
        folders.createProjectFolders();

        // Execute
        ExecutionModeStrategy executionMode = cli.mode;
        executionMode.setCli(cli);
        executionMode.setProjectFolders(folders);
        executionMode.execute();
    }
}