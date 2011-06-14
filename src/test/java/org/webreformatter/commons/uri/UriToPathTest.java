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

import junit.framework.TestCase;

/**
 * @author kotelnikov
 */
public class UriToPathTest extends TestCase {

    /**
     * @param name
     */
    public UriToPathTest(String name) {
        super(name);
    }

    /**
     * 
     */
    public void test() {
        test("a b", "/a+b/");

        Uri uri = new Uri(
            "toto://login:pwd@www.titi.com:123/x/y/z.ext?a=b&c=d#frag");
        assertEquals(
            new Path("/toto/www_titi_com_123/x/y/z_ext/a=b&c=d/"),
            UriToPath.getPath(uri));

        uri = new Uri("toto:///x/y/z.ext?a=b&c=d#frag");
        assertEquals("/toto/x/y/z_ext/a=b%26c=d/", UriToPath
            .getPath(uri)
            .toString());

        test("a b", "/a+b/");
        test("a:", "/a/");
        test("a:b.txt", "/a/b_txt/");
        test("/a:b.txt", "/a:b_txt/");
        test("titi:/a:b.txt", "/titi/a:b_txt/");
        test("toto", "/toto/");
        test("toto.txt", "/toto_txt/");
        test("toto/index.ext", "/toto/index_ext/");
        test("a.b/", "/a_b/");

        test("http://www.foo.bar:8080/a/b/c", "/http/www_foo_bar_8080/a/b/c/");
        test("http://www.foo.bar:8080/a/b/c/", "/http/www_foo_bar_8080/a/b/c/");
        test(
            "http://www.foo.bar:8080/a/b/c/?x=y&a=b",
            "/http/www_foo_bar_8080/a/b/c/x=y%26a=b/");
        test(
            "http://www.foo.bar:8080/a/b/file.pdf",
            "/http/www_foo_bar_8080/a/b/file_pdf/");
        test(
            "http://www.foo.bar:8080/a/b/file.pdf/",
            "/http/www_foo_bar_8080/a/b/file_pdf/");

        test(
            "http://www.foo.bar:8080/a/b/file.pdf?a=b&c=d",
            "/http/www_foo_bar_8080/a/b/file_pdf/a=b%26c=d/");
        test(
            "http://www.foo.bar:8080/мама мыла раму",
            "/http/www_foo_bar_8080/мама+мыла+раму/");

    }

    private void test(String str, String resourcePath) {
        Uri uri = new Uri(str);
        Path test = UriToPath.getPath(uri);
        String strTest = test.getPath(true, false);
        assertEquals(resourcePath, strTest);
    }

}
