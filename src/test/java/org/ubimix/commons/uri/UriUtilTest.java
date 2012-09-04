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

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class UriUtilTest extends TestCase {

    /**
     * @param name
     */
    public UriUtilTest(String name) {
        super(name);
    }

    /**
     * 
     */
    public void test() {
        test("a b", "a+b", "a b");
        test(
            "toto://login:pwd@www.titi.com:123/x/y/z.ext?a=b&c=d#frag",
            "toto%3a//login%3apwd@www.titi.com%3a123/x/y/z.ext%3fa=b&c=d%23frag");
        test("/toto/x/y/z_ext/a=b%26c=d/", "/toto/x/y/z_ext/a=b%2526c=d/");
        test("a b", "a+b");
        test("a:", "a%3a");
        test("a:b.txt", "a%3ab.txt");
        test("/a:b.txt", "/a%3ab.txt", "/a:b.txt");
        test("titi:/a:b.txt", "titi%3a/a%3ab.txt");
        test("toto", "toto");
        test("toto.txt", "toto.txt");
        test("toto/index.ext", "toto/index.ext");
        test("a.b", "a.b");

        test(
            "http://www.foo.bar:8080/a/b/c",
            "http%3a//www.foo.bar%3a8080/a/b/c");
        test(
            "http://www.foo.bar:8080/a/b/c/",
            "http%3a//www.foo.bar%3a8080/a/b/c/");
        test(
            "http://www.foo.bar:8080/a/b/c/?x=y&a=b",
            "http%3a//www.foo.bar%3a8080/a/b/c/%3fx=y&a=b");
        test(
            "http://www.foo.bar:8080/a/b/file.pdf",
            "http%3a//www.foo.bar%3a8080/a/b/file.pdf");
        test(
            "http://www.foo.bar:8080/a/b/file.pdf/",
            "http%3a//www.foo.bar%3a8080/a/b/file.pdf/");

        test(
            "http://www.foo.bar:8080/a/b/file.pdf?a=b&c=d",
            "http%3a//www.foo.bar%3a8080/a/b/file.pdf%3fa=b&c=d");
        test(
            "http://www.foo.bar:8080/мама мыла раму",
            "http%3a//www.foo.bar%3a8080/мама+мыла+раму");

    }

    private void test(String str, String control) {
        test(str, control, str);
    }

    private void test(String str, String control, String reverseControl) {
        String test = UriUtil.toPath(str);
        assertEquals(control, test);
        test = UriUtil.fromPath(control);
        assertEquals(reverseControl, test);
    }

}
