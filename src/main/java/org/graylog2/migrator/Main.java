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

import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.graylog2.database.MongoConnection;
import org.graylog2.database.ValidationException;
import org.graylog2.plugin.streams.Stream;
import org.graylog2.plugin.streams.StreamRule;
import org.graylog2.streams.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class Main {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    private Main() { }

    public static void main(String[] args) {
        Main main = new Main();

        main.run(args);
    }

    public void run(String[] args) {
        final CommandLineArguments commandLineArguments = CommandLineArguments.createInstance("graylog2-migrator", args);
        final Configuration config = new Configuration(commandLineArguments);
        MongoConnection fromDbConnection = null;
        MongoConnection toDbConnection = null;

        LOG.info("Migrating data from version {}", config.getFromVersion());

        try {
            fromDbConnection = createDbConnection(config.getFromDb());
            toDbConnection = createDbConnection(config.getToDb());
        } catch(URISyntaxException e) {
            LOG.error("Invalid URI", e);
            System.exit(1);
        } catch (MongoDBConfig.MongoDBConfigError e) {
            LOG.error("Invalid MongoDB config: {}", e.getMessage());
            System.exit(1);
        }

        DBCursor cursor = fromDbConnection.getDatabase().getCollection("streams").find();
        StreamService streamService = new StreamServiceImpl(toDbConnection);
        StreamRuleService streamRuleService = new StreamRuleServiceImpl(toDbConnection);

        for (DBObject obj : cursor) {
            MigratedStreamFields fields = StreamFieldsBuilder.buildStreamFields(obj, config);

            try {
                Stream streamObject = new MigratedStream(fields.getId(), fields.getFields());

                LOG.info("Migrating Stream object {} ({})", streamObject.getId(), streamObject.getTitle());

                streamService.save(streamObject);
                storeStreamRules(streamRuleService, fields);
            } catch (ValidationException e) {
                LOG.error("Stream validation error {}", fields.toString());
            } catch (MigratedStreamFields.UnsupportedFieldsError e) {
                LOG.warn("Unsupported stream field: {}", e.getMessage());
            }
        }

        LOG.info("DONE");
    }

    private void storeStreamRules(StreamRuleService streamRuleService, MigratedStreamFields fields) {
        for (MigratedStreamFields rule : fields.getStreamRules()) {
            try {
                StreamRule streamRule = new MigratedStreamRule(rule.getId(), rule.getFields());

                LOG.info("Migrating Stream rule object {}", streamRule.getId());

                streamRuleService.save(streamRule);
            } catch (ValidationException e) {
                LOG.error("Stream rule validation error {}", rule.toString());
            } catch (MigratedStreamFields.UnsupportedFieldsError e) {
                LOG.warn("Unsupported stream rule field: {}", e.getMessage());
            }
        }
    }

    private MongoConnection createDbConnection(MongoDBConfig db) {
        LOG.info("Connecting to {}", db);
        MongoConnection conn = new MongoConnection();

        conn.setHost(db.getHost());
        conn.setPort(db.getPort());
        conn.setDatabase(db.getDatabase());
        conn.setMaxConnections(1);
        conn.setThreadsAllowedToBlockMultiplier(1);
        conn.connect();

        return conn;
    }
}
