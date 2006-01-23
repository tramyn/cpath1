/** Copyright (c) 2005 Memorial Sloan-Kettering Cancer Center.
 **
 ** Code written by: Benjamin Gross
 ** Authors: Ethan Cerami, Benjamin Gross, Gary Bader, Chris Sander
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

// package
package org.mskcc.pathdb.schemas.biopax;


// imports
import java.util.HashSet;
import java.util.ArrayList;

import org.mskcc.pathdb.model.CPathRecord;
import org.mskcc.pathdb.model.InternalLinkRecord;
import org.mskcc.pathdb.schemas.biopax.BioPaxConstants;
import org.mskcc.pathdb.sql.dao.DaoInternalLink;
import org.mskcc.pathdb.sql.dao.DaoCPath;

/**
 * This class parses interaction data
 * given a cPath record id.
 *
 * @author Benjamin Gross.
 */
public class MemberPathways {

	/**
	 * Finds all member pathways given CPathRecord.
	 *
	 * @return HashSet
	 */
	public static HashSet getMemberPathways(CPathRecord record) throws Exception {

		// vector to return
		HashSet pathways = new HashSet();

		// get internal links
		DaoInternalLink daoInternalLinks = new DaoInternalLink();
		ArrayList sources = daoInternalLinks.getSources(record.getId());

		if (sources.size() > 0){
			for (int lc = 0; lc < sources.size(); lc++){
				InternalLinkRecord link = (InternalLinkRecord)sources.get(lc);
				DaoCPath cPath = DaoCPath.getInstance();
				CPathRecord sourceRecord = cPath.getRecordById(link.getSourceId());
				pathways.addAll(getMemberPathways(sourceRecord));
			}
		}
		else{
			BioPaxConstants biopaxConstants = new BioPaxConstants();
			if (biopaxConstants.isPathway(record.getSpecificType())){
				String pathway = BioPaxRecordUtil.getEntity(record.getId(), record.getXmlContent());
				if (pathway != null){
					pathways.add(pathway);
				}
			}
		}

		// outta here
		return pathways;
	}
}