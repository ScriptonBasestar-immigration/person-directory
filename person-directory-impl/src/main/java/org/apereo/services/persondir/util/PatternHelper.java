/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apereo.services.persondir.util;

import org.apereo.services.persondir.IPersonAttributeDao;

import java.util.regex.Pattern;

/**
 * @author Eric Dalquist

 */
public class PatternHelper {
    /**
     * Converts a String using the {@link IPersonAttributeDao#WILDCARD} into a valid regular expression
     * {@link Pattern} with the {@link IPersonAttributeDao#WILDCARD} replaced by .* and the rest of the
     * string escaped using {@link Pattern#quote(String)}
     *
     * @param queryString query String
     * @return compiled Pattern
     */
    public static Pattern compilePattern(final String queryString) {
        final var queryBuilder = new StringBuilder();

        final var queryMatcher = IPersonAttributeDao.WILDCARD_PATTERN.matcher(queryString);

        if (!queryMatcher.find()) {
            return Pattern.compile(Pattern.quote(queryString));
        }

        var start = queryMatcher.start();
        var previousEnd = -1;
        if (start > 0) {
            final var queryPart = queryString.substring(0, start);
            final var quotedQueryPart = Pattern.quote(queryPart);
            queryBuilder.append(quotedQueryPart);
        }
        queryBuilder.append(".*");

        do {
            start = queryMatcher.start();

            if (previousEnd != -1) {
                final var queryPart = queryString.substring(previousEnd, start);
                final var quotedQueryPart = Pattern.quote(queryPart);
                queryBuilder.append(quotedQueryPart);
                queryBuilder.append(".*");
            }

            previousEnd = queryMatcher.end();
        } while (queryMatcher.find());

        if (previousEnd < queryString.length()) {
            final var queryPart = queryString.substring(previousEnd);
            final var quotedQueryPart = Pattern.quote(queryPart);
            queryBuilder.append(quotedQueryPart);
        }

        return Pattern.compile(queryBuilder.toString());
    }
}
