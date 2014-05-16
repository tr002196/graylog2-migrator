/*
 * Copyright 2012-2014 TORCH GmbH
 *
 * This file is part of Graylog2.
 *
 * Graylog2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog2.  If not, see <http://www.gnu.org/licenses/>.
 */

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
