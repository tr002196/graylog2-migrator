package org.graylog2.migrator;

import org.bson.types.ObjectId;
import org.graylog2.database.CollectionName;
import org.graylog2.streams.StreamRuleImpl;

import java.util.Map;

/**
 * This StreamRuleImpl subclass is needed so we can use the existing ObjectId. There is no public
 * constructor which takes the ObjectId.
 *
 * @author Bernd Ahlers <bernd@torch.sh>
 */
@CollectionName("streamrules")
public class MigratedStreamRule extends StreamRuleImpl {
    public MigratedStreamRule(ObjectId id, Map<String, Object> fields) {
        super(id, fields);
    }
}
