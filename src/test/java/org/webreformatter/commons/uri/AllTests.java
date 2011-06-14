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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.webreformatter.commons.uri.path.PathManagerTest;


/**
 * Test suite for all classes in the "org.webreformatter.commons.uri" package.
 * 
 * @author kotelnikov
 */
public class AllTests {

    /**
     * @return a test suite for all classes in the
     *         "org.webreformatter.commons.uri" package.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
            "Test for org.webreformatter.commons.uri");
        // $JUnit-BEGIN$
        suite.addTestSuite(PathTest.class);
        suite.addTestSuite(UriTest.class);
        suite.addTestSuite(UriToPathTest.class);
        suite.addTestSuite(PathManagerTest.class);
        // $JUnit-END$
        return suite;
    }

}
