package org.graylog2.migrator;

import java.net.URI;
import java.net.URISyntaxException;

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

    public MongoDBConfig getFromDb() throws URISyntaxException, MongoDBConfig.MongoDBConfigError {
        return MongoDBConfig.create(URI.create(commandLineArguments.getFromDb()));
    }

    public MongoDBConfig getToDb() throws URISyntaxException, MongoDBConfig.MongoDBConfigError {
        return MongoDBConfig.create(URI.create(commandLineArguments.getToDb()));
    }
}
