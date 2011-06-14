/* ************************************************************************** *
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * This file is licensed to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ************************************************************************** */
package org.webreformatter.commons.uri;

import java.util.List;

/**
 * This class contains methods transforming an uri into a corresponding path.
 * 
 * @author kotelnikov
 */
public class UriToPath {

    /**
     * This class is used to filter uri parts while transformation of an uri
     * into a corresponding path
     */
    public static class Filter {

        /**
         * @param authority the authority part of the uri to filter
         * @return a filtered authority part of the uri
         */
        public String filterAuthority(String authority) {
            return null;
        }

        /**
         * @param fileName the name of the file to filter
         * @return the filtered name of the file
         */
        public String filterFileName(String fileName) {
            if (fileName == null) {
                return fileName;
            }
            return fileName.replace('.', '_');
        }

        /**
         * @param fragment the fragment value to filter
         * @return a filtered value of the fragment
         */
        public String filterFragment(String fragment) {
            return null;
        }

        /**
         * @param host the host to filter
         * @return a filtered value of the host
         */
        public String filterHost(String host) {
            if (host == null) {
                return host;
            }
            return host.replace('.', '_');
        }

        /**
         * @param pathSegment a segment of the path to filter
         * @return a filtered value of the given path segment
         */
        public String filterPathSegment(String pathSegment) {
            if (pathSegment == null) {
                return null;
            }
            return pathSegment.replace('.', '_');
        }

        /**
         * @param query a query to filter
         * @return a path segment corresponding to the given query
         */
        public String filterQuery(String query) {
            if (query == null) {
                return null;
            }
            return query.replace('?', '_').replace('#', '_').replace('/', '_');
        }

    }

    /**
     * Returns a path corresponding to the given uri.
     * 
     * @param uri the uri to transform to a path
     * @return a path corresponding to the given uri
     */
    public static Path getPath(AbstractUri uri) {
        return getPath(uri, new Filter());
    }

    public static void getPath(AbstractUri uri, AbstractPathBuilder<?> builder) {
        getPath(uri, new Filter(), builder);
    }

    /**
     * Returns a path corresponding to the given uri.
     * 
     * @param uri the uri to transform to a path
     * @param filter this instance is used to filter all parts of the given uri
     * @return a path corresponding to the given uri
     */
    public static Path getPath(AbstractUri uri, Filter filter) {
        Path.Builder builder = new Path.Builder();
        getPath(uri, filter, builder);
        Path result = builder.build();
        return result;
    }

    public static void getPath(
        AbstractUri uri,
        Filter filter,
        AbstractPathBuilder<?> builder) {
        String scheme = uri.getScheme();
        builder = builder.makeAbsolutePath();
        builder = builder.appendPath(scheme);
        StringBuffer buf = new StringBuffer();
        String auth = filter.filterAuthority(uri.getAuthority());
        if (auth != null) {
            buf.append(auth);
            buf.append("@");
        }
        String host = filter.filterHost(uri.getHost());
        if (host != null) {
            buf.append(host);
        }
        int port = uri.getPort();
        if (port > 0) {
            buf.append("_" + port);
        }
        builder = builder.appendPath(buf.toString());
        buf.delete(0, buf.length());
        Path path = uri.getPath();
        List<String> segments = path.getPathSegments();
        int counter = 0;
        boolean hasFile = !path.hasTrailingSeparator();
        for (String segment : segments) {
            counter++;
            if (hasFile && counter == segments.size()) {
                segment = filter.filterFileName(segment);
            } else {
                segment = filter.filterPathSegment(segment);
            }
            builder = builder.appendPath(segment);
        }
        String query = filter.filterQuery(uri.getQuery());
        builder = builder.appendPath(query);
        String fragment = filter.filterFragment(uri.getFragment());
        builder = builder.appendPath(fragment);
        builder.makeAbsolutePath().appendTrailingSeparator();
    }
}
