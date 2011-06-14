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
package org.webreformatter.commons.uri.path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * This class is used to register and calculates nearest common prefixes for
 * given paths.
 * 
 * @author kotelnikov
 */
public class PathManager<T> {
    /**
     * Each slot contains a path prefix and a corresponding real path.
     */
    private static class Slot<T> implements Map.Entry<String, T> {
        /**
         * The logical path prefix
         */
        public String fPrefix;

        /**
         * The real path corresponding to the prefix
         */
        public T fValue;

        /**
         * @param prefix
         * @param value
         */
        public Slot(String prefix, T value) {
            fPrefix = prefix;
            fValue = value;
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Slot<?>)) {
                return false;
            }
            Slot<?> o = (Slot<?>) obj;
            return fPrefix.equals(o.fPrefix)
                && (fValue == null || o.fValue == null
                    ? fValue == o.fValue
                    : fValue.equals(o.fValue));
        }

        /**
         * @return the path prefix
         * @see java.util.Map$Entry#getKey()
         */
        public String getKey() {
            return fPrefix;
        }

        /**
         * @return the real path corresponding to the prefix
         * @see java.util.Map$Entry#getValue()
         */
        public T getValue() {
            return fValue;
        }

        /**
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode() {
            return fPrefix.hashCode();
        }

        /**
         * @param value a new real path to set
         * @return the old value of the real path
         * @see java.util.Map$Entry#setValue(java.lang.Object)
         */
        public T setValue(T value) {
            T oldValue = fValue;
            fValue = value;
            return oldValue;
        }

        /**
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return fPrefix + "=" + fValue;
        }
    }

    protected List<Slot<T>> fList = new ArrayList<Slot<T>>();

    /**
     * Adds a new path with the corresponding logical prefix
     * 
     * @param prefix the path prefix to register
     * @param value the real path corresponding to the specified prefix
     * @return the canonical form of the removed path
     */
    public String add(String prefix, T value) {
        prefix = getCanonicalPath(prefix);
        int pos = find(prefix);
        if (pos < 0) {
            pos = -(pos + 1);
            Slot<T> slot = new Slot<T>(prefix, value);
            fList.add(pos, slot);
        }
        return prefix;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PathManager<?>)) {
            return false;
        }
        PathManager<?> o = (PathManager<?>) obj;
        return fList.equals(o.fList);
    }

    /**
     * Returns <code>true</code> if the given path is regestered.
     * 
     * @param path the path to check
     * @return <code>true</code> if the given path is regestered
     */
    public boolean exists(String path) {
        path = getCanonicalPath(path);
        int pos = find(path);
        return pos >= 0;
    }

    /**
     * @param path the path to search
     * @return the nearest position of the given path in the list of slots
     */
    private int find(String path) {
        int a = 0;
        int b = fList.size() - 1;
        while (a <= b) {
            int x = (a + b) / 2;
            Slot<T> midVal = fList.get(x);
            int comparisionResult = midVal.fPrefix.compareTo(path);
            if (comparisionResult < 0) {
                a = x + 1;
            } else if (comparisionResult > 0) {
                b = x - 1;
            } else {
                return x;
            }
        }
        return -(a + 1);
    }

    /**
     * Returns an array of all path entries registered in this manager. Each key
     * in the returend entries is a path prefix and the corresponding values are
     * the real pathes.
     * 
     * @return an array of all paths entries registered in this manager
     */
    @SuppressWarnings("unchecked")
    public Map.Entry<String, T>[] getAllEntries() {
        return fList.toArray(new Map.Entry[fList.size()]);
    }

    /**
     * Returns an array of all paths registered in this manager.
     * 
     * @return an array of all paths registered in this manager
     */
    public String[] getAllPrefixes() {
        String[] array = new String[fList.size()];
        int i = 0;
        for (Slot<T> s : fList) {
            array[i++] = s.fPrefix;
        }
        return array;
    }

    /**
     * Returns the canonical form of the given path. This method can be
     * overloaded in subclasses.
     * 
     * @param path for this path a "canonical" form will be returned
     * @return the canonical form of the given path
     */
    public String getCanonicalPath(String path) {
        Character delimiter = getSegmentDelimiter();
        if (path == null) {
            return getEmptyPath();
        }
        if (!path.startsWith(String.valueOf(delimiter))) {
            path = delimiter + path;
        }
        if (!path.endsWith(String.valueOf(delimiter))) {
            path += delimiter;
        }
        return path;
    }

    /**
     * Returns the empty path. This method can be overloaded in subclasses.
     * 
     * @return the empty path
     */
    protected String getEmptyPath() {
        return "" + getSegmentDelimiter();
    }

    /**
     * Returns an entry corresponding to the specified path. The key in the
     * returned entry is a path prefix and the corresponding value is a
     * corresponding value.
     * 
     * @param path an entry corresponding to this path will be returned
     * @return a registered path entry nearest to the given path
     */
    public Map.Entry<String, T> getExactEntry(String path) {
        path = getCanonicalPath(path);
        int pos = find(path);
        if (pos < 0) {
            return null;
        }
        return fList.get(pos);
    }

    /**
     * Returns a value corresponding to the specified path.
     * 
     * @param path a path of the value to return
     * @return a value corresponding to the specified path.
     */
    public T getExactValue(String path) {
        Entry<String, T> entry = getExactEntry(path);
        return entry != null ? entry.getValue() : null;
    }

    /**
     * Returns a registered path entry nearest to the given path. The key in the
     * returend entry is a path prefix and the corresponding value is the real
     * registered path.
     * 
     * @param path for this string a nearest registered entry will be returned
     * @return a registered path entry nearest to the given path
     */
    public Map.Entry<String, T> getNearestEntry(String path) {
        path = getCanonicalPath(path);
        Slot<T> result = null;
        String str = path;
        while (result == null && str.length() > 0) {
            int pos = find(str);
            if (pos < 0) {
                pos = -(pos + 1);
                if (pos > 0) {
                    pos--;
                }
            }
            if (pos >= fList.size()) {
                break;
            }
            Slot<T> s = fList.get(pos);
            if (path.startsWith(s.fPrefix)) {
                result = s;
            } else {
                str = removeSegment(str);
            }
        }
        return result;
    }

    /**
     * Returns a registered path prefix nearest to the given path.
     * 
     * @param path for this string a nearest registered prefix will be returned
     * @return a registered path prefix nearest to the given path.
     */
    public String getNearestPath(String path) {
        Map.Entry<String, T> result = getNearestEntry(path);
        return result != null ? result.getKey() : getEmptyPath();
    }

    public T getNearestValue(String prefix) {
        Entry<String, T> entry = getNearestEntry(prefix);
        return entry != null ? entry.getValue() : null;
    }

    /**
     * Returns a path segment delimiter. This method can be overloaded in
     * subclasses.
     * 
     * @return a segment delimiter
     */
    protected Character getSegmentDelimiter() {
        return '/';
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return fList.hashCode();
    }

    /**
     * Removes the given path from the internal registry and returns the
     * canonical form of the removed path. If the path does not exist in the
     * registry then this method returns <code>null</code>.
     * 
     * @param path for this path prefix the corresponding real path will be
     *        removed
     * @return the canonical form of the removed path
     */
    public Map.Entry<String, T> remove(String path) {
        path = getCanonicalPath(path);
        int pos = find(path);
        if (pos < 0) {
            return null;
        } else {
            return fList.remove(pos);
        }
    }

    /**
     * @param str a one segment will be removed from this string
     * @return removes one path segment from the given string
     */
    private String removeSegment(String str) {
        Character segmentDelimiter = getSegmentDelimiter();
        int id = str.length() - 2;
        for (int i = str.length() - 2; i >= 0; i--) {
            if (str.charAt(i) == segmentDelimiter) {
                id = i;
                break;
            }
        }
        return id >= 0 ? str.substring(0, id + 1) : "";
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return fList.toString();
    }
}