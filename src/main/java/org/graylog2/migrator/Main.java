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

        MongoConnection fromDbConnection = createDbConnection(commandLineArguments.getFromDb());
        MongoConnection toDbConnection = createDbConnection(commandLineArguments.getToDb());

        DBCursor cursor = fromDbConnection.getDatabase().getCollection("streams").find();
        StreamService streamService = new StreamServiceImpl(toDbConnection);
        StreamRuleService streamRuleService = new StreamRuleServiceImpl(toDbConnection);

        for (DBObject obj : cursor) {
            MigratedStreamFields fields = StreamFieldsBuilder.buildStreamFields(obj, "0.12.0");

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
                streamRuleService.save(streamRule);
            } catch (ValidationException e) {
                LOG.error("Stream rule validation error {}", rule.toString());
            } catch (MigratedStreamFields.UnsupportedFieldsError e) {
                LOG.warn("Unsupported stream rule field: {}", e.getMessage());
            }
        }
    }

    private MongoConnection createDbConnection(String dbname) {
        /* TODO: Host and port should be configurable! */
        String host = "127.0.0.1";
        int port = 27017;

        LOG.info("Connecting to MongoDB {}/{}", host + ":" + port, dbname);
        MongoConnection conn = new MongoConnection();

        conn.setHost(host);
        conn.setPort(port);
        conn.setDatabase(dbname);
        conn.setMaxConnections(1);
        conn.setThreadsAllowedToBlockMultiplier(1);
        conn.connect();

        return conn;
    }
}
