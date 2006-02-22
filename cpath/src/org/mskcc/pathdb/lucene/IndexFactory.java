// $Id: IndexFactory.java,v 1.10 2006-02-22 22:47:50 grossb Exp $
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
package org.mskcc.pathdb.lucene;

import org.jdom.JDOMException;
import org.mskcc.pathdb.sql.assembly.PsiAssembly;
import org.mskcc.pathdb.sql.assembly.XmlAssembly;

import java.io.IOException;

/**
 * Factory class for creating ItemToIndex classes.
 *
 * @author Ethan Cerami
 */
public class IndexFactory {

    /**
     * Creates a new ItemToIndex Object based on XML Document.
     *
     * @param cpathId     cPath ID.
     * @param xmlAssembly XML Document Assembly.
     * @return ItemToIndex Object.
     * @throws IOException   Input Output Exception.
     * @throws JDOMException JDOM XML Error.
     */
    public static ItemToIndex createItemToIndex(long cpathId,
            XmlAssembly xmlAssembly) throws IOException, JDOMException {
        if (xmlAssembly instanceof PsiAssembly) {
            return new PsiInteractionToIndex(cpathId, xmlAssembly);
        } else {
            return new BioPaxToIndex(cpathId, xmlAssembly);
        }
    }
}
