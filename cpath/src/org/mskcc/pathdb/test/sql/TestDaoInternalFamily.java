// $Id: TestDaoInternalFamily.java,v 1.1 2006-08-28 16:26:13 cerami Exp $
//------------------------------------------------------------------------------
/** Copyright (c) 2006 Memorial Sloan-Kettering Cancer Center.
 **
 ** Code written by: Ethan Cerami
 ** Authors: Ethan Cerami, Gary Bader, Chris Sander
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 **
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and
 ** Memorial Sloan-Kettering Cancer Center
 ** has no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall
 ** Memorial Sloan-Kettering Cancer Center
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if
 ** Memorial Sloan-Kettering Cancer Center
 ** has been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 **
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/
package org.mskcc.pathdb.test.sql;

import junit.framework.TestCase;
import org.mskcc.pathdb.sql.dao.DaoInternalFamily;
import org.mskcc.pathdb.sql.dao.DaoException;
import org.mskcc.pathdb.model.CPathRecordType;

/**
 * Tests the DaoInternalFamily Class.
 *
 * @author Ethan Cerami
 */
public class TestDaoInternalFamily extends TestCase {
    private String testName;

    /**
     * Test access to DaoInternalFamily
     *
     * @throws DaoException     Database access error.
     */
    public void testAccess() throws DaoException {
        testName = "Test Add, Get, Delete Methods";
        DaoInternalFamily dao = new DaoInternalFamily();
        dao.deleteAllRecords();
        dao.addRecord(1, 2, CPathRecordType.PHYSICAL_ENTITY);
        dao.addRecord(1, 3, CPathRecordType.PHYSICAL_ENTITY);
        dao.addRecord(1, 4, CPathRecordType.INTERACTION);

        long ids[] = dao.getDescendentIds(1);
        assertEquals (3, ids.length);


        ids = dao.getDescendentIds(1, CPathRecordType.PHYSICAL_ENTITY);
        assertEquals (2, ids.length);
        boolean flag1 = false, flag2 = false;
        for (int i = 0; i < ids.length; i++) {
            long id = ids[i];
            if (id == 2) {
                flag1 = true;
            } else if (id == 3) {
                flag2 = true;
            }
        }
        assertTrue (flag1);
        assertTrue (flag2);
    }

    /**
     * Gets Name of Test.
     *
     * @return Name of Test.
     */
    public String getName() {
        return "Test the MySQL Internal Family Data Access Object (DAO):  "
                + testName;
    }
}