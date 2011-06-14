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

import java.util.Collection;

/**
 * @author kotelnikov
 */
public abstract class AbstractExtensionRegistry<E extends Comparable<E>, L extends Collection<E>> {

    private PathManager<L> fManager = new PathManager<L>() {
        @Override
        protected Character getSegmentDelimiter() {
            return AbstractExtensionRegistry.this.getSegmentDelimiter();
        }
    };

    /**
     * @param extension
     */
    public synchronized void addExtension(String key, E extension) {
        L list = fManager.getExactValue(key);
        if (list == null) {
            list = newCollection();
            fManager.add(key, list);
        }
        insert(list, extension);
    }

    public synchronized Collection<E> getExtensions(String type) {
        return fManager.getNearestValue(type);
    }

    public String getNearestPath(String path) {
        return fManager.getNearestPath(path);
    }

    protected Character getSegmentDelimiter() {
        return '/';
    }

    protected abstract void insert(L list, E extension);

    protected abstract L newCollection();

    public synchronized boolean removeExtension(E extension) {
        boolean result = false;
        String[] prefixes = fManager.getAllPrefixes();
        for (String prefix : prefixes) {
            result |= removeExtension(prefix, extension);
        }
        return result;
    }

    public synchronized boolean removeExtension(String type, E extension) {
        L list = fManager.getExactValue(type);
        boolean removed = list.remove(extension);
        if (removed) {
            if (!list.isEmpty()) {
                fManager.add(type, list);
            } else {
                fManager.remove(type);
            }
        }
        return removed;
    }

}
