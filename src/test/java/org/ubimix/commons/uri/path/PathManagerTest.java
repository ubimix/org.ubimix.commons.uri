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
package org.ubimix.commons.uri.path;

import java.util.Map.Entry;

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class PathManagerTest extends TestCase {

    /**
     * @param name
     */
    public PathManagerTest(String name) {
        super(name);
    }

    private void add(PathManager<String> p, String string) {
        p.add(string, string);
    }

    /**
     * 
     */
    public void test() {
        PathManager<String> p = new PathManager<String>();
        add(p, "/a/b/c/");
        add(p, "/a/");
        Entry<String, String> entry = p.remove("/a/b/c/");
        assertEquals("/a/b/c/", entry.getKey());
        entry = p.remove("/a/");
        assertEquals("/a/", entry.getKey());

        add(p, "/a/b/c/");
        add(p, "/a/b/c/d/");
        add(p, "/a/");
        add(p, "/x/y/z/");
        assertEquals("/a/", p.getNearestPath("/a/"));
        assertEquals("/a/", p.getNearestPath("/a/b/"));
        assertEquals("/a/", p.getNearestPath("/a/b/d/"));
        assertEquals("/a/b/c/", p.getNearestPath("/a/b/c/"));
        assertEquals("/a/b/c/", p.getNearestPath("/a/b/c/x/"));
        assertEquals("/a/b/c/d/", p.getNearestPath("/a/b/c/d/"));
        assertEquals("/", p.getNearestPath("/A/B/"));
        assertEquals("/", p.getNearestPath("/"));
        assertEquals("/x/y/z/", p.getNearestPath("/x/y/z/A/B/C/"));
    }

    /**
     * 
     */
    public void testOne() {
        PathManager<String> pathManager = new PathManager<String>();
        add(pathManager, "/");
        add(pathManager, "/edit/");
        add(pathManager, "/resources/img/");
        assertEquals("/resources/img/", pathManager
            .getNearestPath("/resources/img/toto"));
    }

    /**
     * 
     */
    public void testRealPaths() {
        PathManager<String> p = new PathManager<String>();
        p.add("/a/", "/1/2/3");
        p.add("/a/b/c/", "/x/y/z/");

        assertEquals("/a/", p.getNearestPath("/a/b/C/D"));
        Entry<String, String> entry = p.getNearestEntry("/a/");
        assertNotNull(entry);
        assertEquals("/a/", entry.getKey());
        assertEquals("/1/2/3", entry.getValue());

        assertEquals("/", p.getNearestPath("/x/"));
        entry = p.getNearestEntry("/x/");
        assertNull(entry);

        entry = p.remove("/a/b/c/");
        assertEquals("/a/b/c/", entry.getKey());
        assertEquals("/x/y/z/", entry.getValue());
        entry = p.remove("/a/");
        assertEquals("/a/", entry.getKey());
        assertEquals("/1/2/3", entry.getValue());
    }

    public void testTwo() {
        PathManager<String> p = new PathManager<String>();
        p.add("/n", "N");
        p.add("/x", "X");
        p.add("", "A");
        p.add("", "B");

        String s = p.getNearestValue("/m");
        assertEquals("A", s);

        s = p.getNearestValue("/n/m");
        assertEquals("N", s);

        s = p.getNearestValue("/b");
        assertEquals("A", s);
    }

}
