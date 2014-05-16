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

import org.bson.types.ObjectId;
import org.graylog2.database.CollectionName;
import org.graylog2.streams.StreamImpl;

import java.util.Map;

/**
 * This StreamImpl subclass is needed so we can use the existing ObjectId. There is no public
 * constructor which takes the ObjectId.
 *
 * @author Bernd Ahlers <bernd@torch.sh>
 */
@CollectionName("streams")
public class MigratedStream extends StreamImpl {
    public MigratedStream(ObjectId id, Map<String, Object> fields) {
        super(id, fields);
    }
}
