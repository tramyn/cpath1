/** Copyright (c) 2004 Memorial Sloan-Kettering Cancer Center.
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
package org.mskcc.pathdb.test.model;

import junit.framework.TestCase;
import org.mskcc.pathdb.model.BackgroundReferenceRecord;
import org.mskcc.pathdb.model.ReferenceType;

/**
 * Tests the IdMapRecord.
 *
 * @author Ethan Cerami
 */
public class TestIdMapRecord extends TestCase {

    /**
     * Tests the HashCode Generator for the IdMapRecord.
     */
    public void testHashCode() {
        //  These two records are functionally equivalent
        BackgroundReferenceRecord record1 = new BackgroundReferenceRecord
                (1, "ABC", 2, "XYZ", ReferenceType.IDENTITY);
        BackgroundReferenceRecord record2 = new BackgroundReferenceRecord
                (2, "XYZ", 1, "ABC", ReferenceType.IDENTITY);

        //  They should therefore result in identical hash codes
        assertTrue(record1.hashCode() == record2.hashCode());
    }
}
