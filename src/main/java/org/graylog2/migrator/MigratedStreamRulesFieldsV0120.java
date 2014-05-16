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

import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.graylog2.plugin.streams.StreamRuleType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bernd Ahlers <bernd@torch.sh>
 */
public class MigratedStreamRulesFieldsV0120 extends MigratedStreamFields {
    public MigratedStreamRulesFieldsV0120(DBObject fields, Configuration config) {
        super(fields, config);
    }

    @Override
    public Map<String, Object> getFields() throws UnsupportedFieldsError {
        Map<String, Object> newFields = new HashMap<>();

        setFieldAndType(newFields);

        if (newFields.get("value") == null) {
            newFields.put("value", fields.get("value"));
        }

        newFields.put("stream_id", new ObjectId((String) fields.get("stream_id")));
        newFields.put("inverted", fields.get("inverted"));
        newFields.put("_id", fields.get("_id"));

        return newFields;
    }

    private void setFieldAndType(Map<String, Object> newFields) throws UnsupportedFieldsError {
        int type = (int) fields.get("rule_type");

        switch (type) {
            /* TYPE_MESSAGE */
            case 1:
                newFields.put("field", "message");
                newFields.put("type", StreamRuleType.REGEX.toInteger());
                break;
            /* TYPE_HOST */
            case 2:
                newFields.put("field", "host");
                newFields.put("type", StreamRuleType.EXACT.toInteger());
                break;
            /* TYPE_SEVERITY */
            case 3:
                newFields.put("field", "level");
                newFields.put("type", StreamRuleType.EXACT.toInteger());
                break;
            /* TYPE_FACILITY */
            case 4:
                newFields.put("field", "facility");
                newFields.put("type", StreamRuleType.EXACT.toInteger());
                break;
            /* TYPE_TIMEFRAME (was only used for frontend) */
            case 5:
                markUnsupported("TIMEFRAME");
                break;
            /* TYPE_ADDITIONAL */
            case 6:
                /* The value of the additional type is "field=regex". */
                String[] parts = ((String) fields.get("value")).split("=");

                newFields.put("field", "_" + parts[0]);
                newFields.put("type", StreamRuleType.REGEX.toInteger());
                newFields.put("value", parts[1]);
                break;
            /* TYPE_HOSTGROUP (feature got removed) */
            case 7:
                markUnsupported("HOSTGROUP");
                break;
            /* TYPE_SEVERITY_OR_HIGHER */
            case 8:
                int value = Integer.parseInt((String) fields.get("value"));

                newFields.put("field", "level");

                /* There is no smaller-or-equal stream rule type anymore. So we increase the value by 1 and use the
                 * SMALLER type.
                 */
                newFields.put("value", String.valueOf(value + 1));

                /* Higher severity means a smaller value. That's why we use the SMALLER type here. 0 (EMERG), 7 (DEBUG) */
                newFields.put("type", StreamRuleType.SMALLER.toInteger());

                break;
            /* TYPE_HOST_REGEX */
            case 9:
                newFields.put("field", "host");
                newFields.put("type", StreamRuleType.REGEX.toInteger());
                break;
            /* TYPE_FULL_MESSAGE */
            case 10:
                newFields.put("field", "full_message");
                newFields.put("type", StreamRuleType.REGEX.toInteger());
                break;
            /* TYPE_FILENAME_LINE */
            case 11:
                markUnsupported("FILENAME_LINE");
                break;
            /* TYPE_FACILITY_REGEX */
            case 12:
                newFields.put("field", "facility");
                newFields.put("type", StreamRuleType.REGEX.toInteger());
                break;
            default:
                throw new RuntimeException("Unknown stream rule type: " + type);
        }
    }

    private void markUnsupported(String type) throws UnsupportedFieldsError {
        throw new UnsupportedFieldsError("Stream rule type " + type + " not supported");
    }
}
