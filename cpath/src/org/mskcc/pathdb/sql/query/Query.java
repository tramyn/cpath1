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
package org.mskcc.pathdb.sql.query;

import org.mskcc.pathdb.protocol.ProtocolConstants;
import org.mskcc.pathdb.protocol.ProtocolRequest;
import org.mskcc.pathdb.sql.assembly.XmlAssembly;
import org.mskcc.pathdb.sql.dao.DaoException;
import org.mskcc.pathdb.sql.dao.DaoXmlCache;
import org.mskcc.pathdb.util.Md5Util;
import org.mskcc.pathdb.xdebug.XDebug;

import java.security.NoSuchAlgorithmException;

/**
 * Executes Interaction Queries.
 * <p/>
 * <P>Note:  JUnit testing for this class is performed in:
 * org.mskcc.pathdb.test.sql.TestImportPsiToCPath.
 *
 * @author Ethan Cerami.
 */
public class Query {
    private XDebug xdebug;

    /**
     * Constructor.
     *
     * @param xdebug XDebug Object.
     */
    public Query(XDebug xdebug) {
        this.xdebug = xdebug;
    }

    /**
     * Execute Query.
     *
     * @param request    ProtocolRequest object.
     * @param checkCache If set to true, method will check the XML cache for
     *                   pre-computed results.
     * @return XmlAssembly object.
     * @throws QueryException Indicates Query Error.
     */
    public XmlAssembly executeQuery(ProtocolRequest request,
            boolean checkCache) throws QueryException {
        DaoXmlCache dao = new DaoXmlCache(xdebug);
        XmlAssembly xmlAssembly = null;
        XmlAssembly cachedXml = null;
        try {
            String hashKey = getHashKey(request);
            xdebug.logMsg(this, "Checking cache for pre-computed XML");
            xdebug.logMsg(this, "Using HashKey:  " + hashKey);
            cachedXml = dao.getXmlAssemblyByKey(hashKey);
            if (cachedXml == null) {
                xdebug.logMsg(this, "No Match Found");
            } else {
                xdebug.logMsg(this, "Match Found");
            }
            if (checkCache && cachedXml != null) {
                xdebug.logMsg(this, "Using Cached XML Document");
                xmlAssembly = cachedXml;
            } else {
                xdebug.logMsg(this, "Executing New Interaction Query");
                xmlAssembly = executeQuery(request);
                if (!xmlAssembly.isEmpty()) {
                    if (cachedXml == null) {
                        xdebug.logMsg(this, "Storing XML to Database Cache");
                        dao.addRecord(hashKey, request.getUrlParameterString(),
                                xmlAssembly);
                    } else {
                        xdebug.logMsg(this, "Updating XML in Database Cache");
                        dao.updateXmlAssemblyByKey(hashKey, xmlAssembly);
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            throw new QueryException(e.getMessage(), e);
        } catch (DaoException e) {
            throw new QueryException(e.getMessage(), e);
        } catch (QueryException e) {
            throw new QueryException(e.getMessage(), e);
        }
        return xmlAssembly;
    }

    /**
     * Gets the HashKey for Specified Protocol Request.
     *
     * @param request ProtocolRequest Object.
     * @return Hash Key.
     * @throws NoSuchAlgorithmException No Such Algorithm Exception
     */
    private String getHashKey(ProtocolRequest request)
            throws NoSuchAlgorithmException {
        String originalFormat = request.getFormat();

        //  Set Format to PSI (no matter what).
        //  This enables us to reuse the same XML Content for requests
        //  for HTML and PSI.
        request.setFormat(ProtocolConstants.FORMAT_XML);
        String hashKey = Md5Util.createMd5Hash(request.getUrlParameterString());

        //  Set Back to Original Format.
        request.setFormat(originalFormat);
        return hashKey;
    }

    private XmlAssembly executeQuery(ProtocolRequest request)
            throws QueryException {
        InteractionQuery query = determineQueryType(request);
        return query.execute(xdebug);
    }

    /**
     * Instantiates Correct Query based on Protocol Request.
     */
    private InteractionQuery determineQueryType(ProtocolRequest request) {
        InteractionQuery query = new GetInteractionsViaLucene(request);
        return query;
    }
}