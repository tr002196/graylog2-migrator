package org.graylog2.migrator;

import org.bson.types.ObjectId;
import org.graylog2.database.CollectionName;
import org.graylog2.streams.StreamImpl;

import java.util.Map;

/**
 * This StreamImpl subclass is needed so we can use the existing ObjectId. There is no public
 * constructor which takes the Objectid.
 *
 * @author Bernd Ahlers <bernd@torch.sh>
 */
@CollectionName("streams")
public class MigratedStream extends StreamImpl {
    public MigratedStream(ObjectId id, Map<String, Object> fields) {
        super(id, fields);
    }
}
