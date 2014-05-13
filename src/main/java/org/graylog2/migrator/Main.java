package org.graylog2.migrator;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.graylog2.database.MongoConnection;
import org.graylog2.database.ValidationException;
import org.graylog2.plugin.streams.Stream;
import org.graylog2.streams.StreamService;
import org.graylog2.streams.StreamServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class Main {
    private final static Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        final CommandLineArguments commandLineArguments = getCommandLineArguments(args);

        MongoConnection fromDbConnection = createDbConnection(commandLineArguments.getFromDb());
        MongoConnection toDbConnection = createDbConnection(commandLineArguments.getToDb());

        DBCursor cursor = fromDbConnection.getDatabase().getCollection("streams").find();
        StreamService streamService = new StreamServiceImpl(toDbConnection);

        for (DBObject obj : cursor) {
            MigratedStreamFields fields = StreamObjectBuilder.buildStreamObject(obj, "0.12.0");
            Stream streamObject = new MigratedStream(fields.getId(), fields.getFields());

            LOG.info("Migrating Stream object {} ({})", streamObject.getId(), streamObject.getTitle());

            try {
                streamService.save(streamObject);
            } catch (ValidationException e) {
                LOG.error("Validations failed", e);
            }
        }

        LOG.info("DONE");
    }

    private static CommandLineArguments getCommandLineArguments(String[] args) {
        CommandLineArguments commandLineArguments = new CommandLineArguments();

        try {
            JCommander jCommander = new JCommander(commandLineArguments, args);
            jCommander.setProgramName("graylog2-migrator");

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

    private static MongoConnection createDbConnection(String dbname) {
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
