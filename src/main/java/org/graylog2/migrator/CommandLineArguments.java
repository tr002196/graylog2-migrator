package org.graylog2.migrator;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
@Parameters(commandDescription = "Graylog2 Migrator")
public class CommandLineArguments {
    @Parameter(names = {"-h", "--help"}, description = "Show usage information and exit", help = true)
    private boolean showHelp = false;

    @Parameter(names = {"--from-db"}, description = "Source database", required = true)
    private String fromDb;

    @Parameter(names = {"--to-db"}, description = "Target database", required = true)
    private String toDb;

    public boolean isShowHelp() {
        return showHelp;
    }

    public String getFromDb() {
        return fromDb;
    }

    public String getToDb() {
        return toDb;
    }
}
