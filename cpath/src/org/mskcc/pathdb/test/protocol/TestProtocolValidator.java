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
package org.mskcc.pathdb.test.protocol;

import junit.framework.TestCase;
import org.mskcc.pathdb.protocol.*;

import java.util.HashMap;

/**
 * Tests the Protocol Validator.
 *
 * @author Ethan Cerami
 */
public class TestProtocolValidator extends TestCase {

    /**
     * Tests the Protocol Validator.
     *
     * @throws Exception General Error.
     */
    public void testProtocolValidator() throws Exception {
        HashMap map = new HashMap();
        map.put(ProtocolRequest.ARG_COMMAND,
                ProtocolConstants.COMMAND_GET_BY_INTERACTOR_NAME_XREF);
        ProtocolRequest request = new ProtocolRequest(map);
        ProtocolValidator validator = new ProtocolValidator(request);
        try {
            validator.validate();
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException e) {
            ProtocolStatusCode statusCode = e.getStatusCode();
            assertEquals(ProtocolStatusCode.MISSING_ARGUMENTS, statusCode);
        }
    }

    /**
     * Tests the Protocol Validator.
     *
     * @throws Exception General Error.
     */
    public void testEmptyParameterSet() throws Exception {
        HashMap map = new HashMap();
        ProtocolRequest request = new ProtocolRequest(map);
        ProtocolValidator validator = new ProtocolValidator(request);
        try {
            validator.validate();
            fail("ProtocolException should have been thrown");
        } catch (NeedsHelpException e) {
            request.getCommand();  // Do Nothing.
        }
    }

    /**
     * Tests the Max Hits Parameter
     * @throws Exception
     */
    public void testMaxHits() throws NeedsHelpException {
        HashMap map = new HashMap();
        map.put(ProtocolRequest.ARG_COMMAND,
                ProtocolConstants.COMMAND_GET_BY_KEYWORD);
        ProtocolRequest request = new ProtocolRequest(map);
        request.setFormat(ProtocolConstants.FORMAT_XML);
        int maxHits = ProtocolConstants.MAX_NUM_HITS + 1;
        request.setMaxHits(Integer.toString(maxHits));
        ProtocolValidator validator = new ProtocolValidator(request);
        try {
            validator.validate();
            fail("ProtocolException should have been thrown");
        } catch (ProtocolException e) {
            ProtocolStatusCode code = e.getStatusCode();
            assertEquals (ProtocolStatusCode.INVALID_ARGUMENT, code);
        }
    }
}