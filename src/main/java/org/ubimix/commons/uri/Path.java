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
import java.util.List;

/**
 * @author kotelnikov
 */
public class Path extends AbstractPath {

    public static class Builder extends AbstractPathBuilder<Builder> {

        public Builder() {
            super(null);
        }

        public Builder(AbstractPath path) {
            super(path);
        }

        public Builder(String path) {
            setPath(path);
        }

        public Path build() {
            return new Path(this);
        }

        @Override
        public Builder getCopy() {
            return new Builder(this);
        }

        public Builder getResolved(String path) {
            Builder p = new Builder(path);
            return getResolved(p);
        }

    }

    public final static Path EMPTY = new Path("");

    protected final boolean fPathAbsolute;

    protected final List<String> fPathSegments;

    protected final boolean fPathTrailingSeparator;

    public Path(AbstractPath path) {
        if (path != null) {
            fPathAbsolute = path.isAbsolutePath();
            fPathTrailingSeparator = path.hasPathTrailingSeparator();
            fPathSegments = Collections
                .unmodifiableList(path.getPathSegments());
        } else {
            fPathSegments = new ArrayList<String>();
            fPathAbsolute = false;
            fPathTrailingSeparator = false;
        }
    }

    /**
     * 
     */
    public Path(String path) {
        this(new Builder(path));
    }

    public Builder getBuilder() {
        return new Builder(this);
    }

    /**
     * Returns all segments of this path
     * 
     * @return all segments of this path
     */
    @Override
    public List<String> getPathSegments() {
        return fPathSegments;
    }

    /**
     * @param anotherPath an another path
     * @return a relative path
     */
    public Path getRelative(AbstractPath anotherPath) {
        return getBuilder().makeRelative(anotherPath).build();
    }

    public Path getResolved(AbstractPath relativePath) {
        return getBuilder().getResolved(relativePath).build();
    }

    public Path getResolved(String relativePath) {
        return getBuilder().getResolved(relativePath).build();
    }

    /**
     * Returns <code>true</code> if this path is absolute, i.e. it starts with
     * "/" symbol.
     * 
     * @return <code>true</code> if this path is absolute, i.e. it starts with
     *         "/" symbol.
     * @see #hasTrailingSeparator()
     */
    public boolean hasAbsolutePath() {
        return fPathAbsolute;
    }

    protected boolean hasAuthority() {
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean hasPathTrailingSeparator() {
        return fPathTrailingSeparator;
    }

    public boolean hasSchema() {
        return false;
    }

    /**
     * Returns <code>true</code> if this path has a trailing separator ("/"
     * symbol at the end). Note that if this is an absolute empty path ("/")
     * then this method returns <code>false</code>.
     * 
     * @return <code>true</code> if this path has a trailing separator ("/"
     *         symbol at the end).
     * @see #hasAbsolutePath()
     */
    public boolean hasTrailingSeparator() {
        return fPathTrailingSeparator;
    }

    @Override
    public boolean isAbsolutePath() {
        return fPathAbsolute;
    }

    public boolean isEmpty() {
        return fPathSegments.isEmpty();
    }

}
