package org.mskcc.pathdb.test.indexer;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Suite of all Indexer Unit Tests.
 *
 * @author Ethan Cerami
 */
public class IndexerSuite extends TestCase {

    /**
     * The suite method runs all the tests.
     * @return Suite of JUnit tests.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(TestIndexer.class);
        suite.setName("Indexer Tests");
        return suite;
    }
}