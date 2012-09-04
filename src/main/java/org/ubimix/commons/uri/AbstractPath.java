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
package org.ubimix.commons.uri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author kotelnikov
 */
public abstract class AbstractPath implements Comparable<AbstractPath> {

    protected final static boolean DECODE_PATH = true;

    protected final static boolean ENCODE_PATH = true;

    public static void appendPath(
        AbstractPath path,
        StringBuffer buf,
        boolean escape,
        boolean encode) {
        List<String> segments = path.getPathSegments();
        appendPath(path, buf, segments.size(), escape, encode);
    }

    protected static void appendPath(
        AbstractPath path,
        StringBuffer buf,
        int count,
        boolean escape,
        boolean encode) {
        if (path.isAbsolutePath()) {
            buf.append("/");
        }
        List<String> segments = path.getPathSegments();
        for (int pos = 0; pos < count; pos++) {
            if (pos > 0) {
                buf.append("/");
            }
            String segment = segments.get(pos);
            if (escape || encode) {
                segment = encode(segment, escape, encode);
            }
            buf.append(segment);
        }
        if (path.hasPathTrailingSeparator()) {
            buf.append("/");
        }
    }

    public static String decode(String value) {
        UriEncoder encoder = UriEncoder.getInstance();
        return encoder != null ? encoder.decode(value) : value;
    }

    public static String encode(String value, boolean escape, boolean encode) {
        if (value == null) {
            return null;
        }
        UriEncoder encoder = UriEncoder.getInstance();
        return encoder != null ? encoder.encode(value, escape, encode) : value;
    }

    /**
     * Compares the given parameters and returns <code>true</code> if they are
     * <code>null</code> or equal.
     * 
     * @param first the first parameter to compare
     * @param second the second parameter to compare
     * @return <code>true</code> if the both given parameters are
     *         <code>null</code> or they are equal
     */
    public static boolean equals(String first, String second) {
        return (first == null || second == null) ? first == second : first
            .equals(second);
    }

    /**
     * @param second the path to check
     * @return the number of the common path segments
     */
    public static int getCommonPathSegments(
        AbstractPath first,
        AbstractPath second,
        boolean begin) {
        List<String> firstSegments = first.getPathSegments();
        List<String> secondSegments = second.getPathSegments();
        int thisLen = firstSegments.size();
        int pathLen = secondSegments.size();
        int len = Math.min(thisLen, pathLen);
        int result = 0;
        if (begin) {
            for (int i = 0; i < len; i++) {
                String a = firstSegments.get(i);
                String b = secondSegments.get(i);
                if (!a.equals(b)) {
                    break;
                }
                result++;
            }
        } else {
            for (int i = thisLen - 1, j = pathLen - 1; i >= 0 && j >= 0; i--, j--) {
                String a = firstSegments.get(i);
                String b = secondSegments.get(j);
                if (!a.equals(b)) {
                    break;
                }
                result++;
            }
        }
        return result;
    }

    /**
     * Returns the path without file names.
     * 
     * @param escapeSegments if this flag is <code>true</code> then this method
     *        will escape path segments
     * @return the path without file name
     */
    public static String getDirectory(
        AbstractPath path,
        boolean escape,
        boolean encode) {
        List<String> pathSegments = path.getPathSegments();
        int len = pathSegments.size();
        if (!path.hasPathTrailingSeparator()) {
            len--;
        }
        StringBuffer buf = new StringBuffer();
        appendPath(path, buf, len, escape, encode);
        return buf.toString();
    }

    /**
     * @return the file extension
     */
    public static String getFileExtension(AbstractPath path) {
        if (path.hasPathTrailingSeparator()) {
            return null;
        }
        String segment = getLastPathSegment(path);
        if (segment == null) {
            return null;
        }
        int id = segment.lastIndexOf('.');
        return (id >= 0) ? segment.substring(id + 1) : null;
    }

    /**
     * Returns the name of the file without extension. If this url has the
     * trailing separator then this method returns <code>null</code>.
     * 
     * @return the name of the file without extension
     */
    public static String getFileNameWithoutExtension(AbstractPath path) {
        if (path.hasPathTrailingSeparator()) {
            return null;
        }
        String segment = getLastPathSegment(path);
        if (segment == null) {
            return null;
        }
        int id = segment.lastIndexOf('.');
        return (id >= 0) ? segment.substring(0, id) : segment;
    }

    /**
     * Returns the last segment of this path
     * 
     * @return the last segment of this path
     */
    public static String getLastPathSegment(AbstractPath path) {
        List<String> pathSegments = path.getPathSegments();
        if (pathSegments.isEmpty()) {
            return null;
        }
        String segment = pathSegments.get(pathSegments.size() - 1);
        return segment;
    }

    /**
     * @param escape if this flag is <code>true</code> then this method returns
     *        escaped spaces in segments
     * @param encode if this flag is <code>true</code> then all extended
     *        characters will be UTF-8 encoded (see URL encoding)
     * @return the path as a string
     */
    public static String getPath(
        AbstractPath path,
        boolean escape,
        boolean encode) {
        List<String> fPathSegments = path.getPathSegments();
        if (!path.isAbsolutePath() && fPathSegments.isEmpty()) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        appendPath(path, buf, fPathSegments.size(), escape, encode);
        return buf.toString();
    }

    /**
     * Parses the given query string and returns a map containing key/value
     * pairs defined by the query.
     * 
     * @param queryString the string to parse
     * @return a map containing key/value pairs defined in the given query
     *         string
     */
    public static Map<String, List<String>> getQueryMap(String queryString) {
        if (queryString == null) {
            return Collections.emptyMap();
        }
        Map<String, List<String>> out = new HashMap<String, List<String>>();
        if (queryString != null && queryString.length() > 0) {
            String qs = queryString;
            if (queryString.startsWith("?")) {
                qs = queryString.substring(1);
            }
            for (String kvPair : qs.split("&")) {
                String[] kv = kvPair.split("=", 2);
                if (kv[0].length() == 0) {
                    continue;
                }
                List<String> values = out.get(kv[0]);
                if (values == null) {
                    values = new ArrayList<String>();
                    out.put(kv[0], values);
                }
                String value = kv.length > 0 && kv[1].length() > 0
                    ? decode(kv[1])
                    : "";
                values.add(value);
            }
        }
        for (Map.Entry<String, List<String>> entry : out.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }
        out = Collections.unmodifiableMap(out);
        return out;
    }

    /**
     * @param path the path to check
     * @return <code>true</code> if the first path starts with the second path
     */
    public static boolean startsWith(AbstractPath first, AbstractPath second) {
        List<String> fPathSegments = first.getPathSegments();
        int len = fPathSegments.size();
        int commonPath = getCommonPathSegments(first, second, true);
        int pathLen = second.getPathSegments().size();
        return commonPath == pathLen
            && (len > pathLen || first.hasPathTrailingSeparator() || !second
                .hasPathTrailingSeparator());

    }

    public int compareTo(AbstractPath o) {
        boolean absolute1 = isAbsolutePath();
        boolean absolute2 = o.isAbsolutePath();
        if (absolute1 != absolute2) {
            return absolute1 ? 1 : -1;
        }
        List<String> pathSegments1 = getPathSegments();
        List<String> pathSegments2 = o.getPathSegments();
        int len1 = pathSegments1.size();
        int len2 = pathSegments2.size();
        int len = Math.min(len1, len2);
        int i;
        int result = 0;
        for (i = 0; result == 0 && i < len; i++) {
            String segment1 = pathSegments1.get(i);
            String segment2 = pathSegments2.get(i);
            result = segment1.compareTo(segment2);
        }
        if (result != 0) {
            return result;
        }
        if (len1 != len2) {
            return len1 > len2 ? 1 : -1;
        }
        boolean trail1 = hasPathTrailingSeparator();
        boolean trail2 = o.hasPathTrailingSeparator();
        if (trail1 != trail2) {
            return trail1 ? 1 : -1;
        }
        return 0;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof AbstractPath)) {
            return false;
        }
        return compareTo((AbstractPath) obj) == 0;
    }

    public int getCommonPathSegments(AbstractPath path) {
        return getCommonPathSegments(path, true);
    }

    /**
     * @param path the path to check
     * @return the number of the common path segments
     */
    public int getCommonPathSegments(AbstractPath path, boolean begin) {
        return getCommonPathSegments(this, path, begin);
    }

    /**
     * Returns the path without file name
     * 
     * @return the path without file name
     */
    public String getDirectory() {
        return getDirectory(ENCODE_PATH);
    }

    /**
     * Returns the path without file name.
     * 
     * @param escapeSegments if this flag is <code>true</code> then this method
     *        will escape path segments
     * @return the path without file name
     */
    public String getDirectory(boolean escapeSegments) {
        return getDirectory(escapeSegments, true);
    }

    public String getDirectory(boolean escape, boolean encode) {
        return getDirectory(this, escape, encode);
    }

    /**
     * @return the file extension
     */
    public String getFileExtension() {
        return getFileExtension(this);
    }

    /**
     * Returns the name of the file. If this path has a trailing separator then
     * this method returns <code>null</code>.
     * 
     * @return the name of the file
     */
    public String getFileName() {
        if (hasPathTrailingSeparator()) {
            return null;
        }
        return getLastPathSegment();
    }

    /**
     * Returns the name of the file without extension. If this url has the
     * trailing separator then this method returns <code>null</code>.
     * 
     * @return the name of the file without extension
     */
    public String getFileNameWithoutExtension() {
        return getFileNameWithoutExtension(this);
    }

    /**
     * Returns the last segment of this path
     * 
     * @return the last segment of this path
     */
    public String getLastPathSegment() {
        return getLastPathSegment(this);
    }

    /**
     * @param encode if this flag is <code>true</code> then this method returns
     *        URL encoded segments.
     * @return the local path
     */
    public String getPath(boolean encode) {
        return getPath(true, encode);
    }

    /**
     * @param escape if this flag is <code>true</code> then this method returns
     *        escaped spaces in segments
     * @param encode if this flag is <code>true</code> then all extended
     *        characters will be UTF-8 encoded (see URL encoding)
     * @return the path as a string
     */
    public String getPath(boolean escape, boolean encode) {
        return getPath(this, escape, encode);
    }

    /**
     * Returns a path segment from the given position
     * 
     * @param pos from this position a path segment will be returned
     * @return a path segment from the given position
     */
    public String getPathSegment(int pos) {
        List<String> pathSegments = getPathSegments();
        if (pos < 0 || pos >= pathSegments.size()) {
            return null;
        }
        return pathSegments.get(pos);
    }

    /**
     * @return the number of the path segments
     */
    public int getPathSegmentCount() {
        List<String> pathSegments = getPathSegments();
        return pathSegments.size();
    }

    public abstract List<String> getPathSegments();

    public abstract boolean hasPathTrailingSeparator();

    public abstract boolean isAbsolutePath();

    /**
     * @param path the path to check
     * @return <code>true</code> if this path starts with the given path
     */
    public boolean startsWith(AbstractPath path) {
        return startsWith(this, path);
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer();
        List<String> pathSegments = getPathSegments();
        appendPath(this, result, pathSegments.size(), true, ENCODE_PATH);
        return result.toString();
    }
}
