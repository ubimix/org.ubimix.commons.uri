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
import java.util.Collections;
import java.util.List;

/**
 * @author kotelnikov
 */
public class ExtensionRegistry<E extends Comparable<E>>
    extends
    AbstractExtensionRegistry<E, List<E>> {

    /**
     * 
     */
    public ExtensionRegistry() {
        super();
    }

    @Override
    protected void insert(List<E> list, E extension) {
        int pos = Collections.binarySearch(list, extension);
        if (pos < 0) {
            pos = -(pos + 1);
        }
        list.add(pos, extension);
    }

    @Override
    protected List<E> newCollection() {
        return new ArrayList<E>();
    }

}
