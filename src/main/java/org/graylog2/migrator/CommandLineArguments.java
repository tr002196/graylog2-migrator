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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
@Parameters(commandDescription = "Graylog2 Migrator")
public class CommandLineArguments {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    @Parameter(names = {"-h", "--help"}, description = "Show usage information and exit", help = true)
    private boolean showHelp = false;

    @Parameter(names = {"--from-db"}, description = "Source database URI (Example: mongodb://localhost/graylog2old)", required = true)
    private String fromDb;

    @Parameter(names = {"--to-db"}, description = "Target database URI (Example: mongodb://localhost/graylog2new", required = true)
    private String toDb;

    @Parameter(names = {"--creator"}, description = "Value for the creator_user_id field")
    private String creator = "admin";

    @Parameter(names = {"--from-version"}, description = "Graylog2 version of the source database")
    private String fromVersion = "0.12.0";

    public boolean isShowHelp() {
        return showHelp;
    }

    public String getFromDb() {
        return fromDb;
    }

    public String getToDb() {
        return toDb;
    }

    public String getCreator() {
        return creator;
    }

    public String getFromVersion() {
        return fromVersion;
    }

    public static CommandLineArguments createInstance(String progname, String[] args) {
        CommandLineArguments commandLineArguments = new CommandLineArguments();

        try {
            JCommander jCommander = new JCommander(commandLineArguments, args);
            jCommander.setProgramName(progname);

            if (commandLineArguments.isShowHelp()) {
                jCommander.usage();
                System.exit(0);
            }
        } catch (ParameterException e) {
            LOG.error(e.getMessage());
            System.exit(1);
        }

        return commandLineArguments;
    }
}
