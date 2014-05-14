package org.graylog2.migrator;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class Configuration {
    private final CommandLineArguments commandLineArguments;

    public Configuration(CommandLineArguments commandLineArguments) {
        this.commandLineArguments = commandLineArguments;
    }

    public String getCreator() {
        return commandLineArguments.getCreator();
    }

    public String getFromVersion() {
        return commandLineArguments.getFromVersion();
    }
}
