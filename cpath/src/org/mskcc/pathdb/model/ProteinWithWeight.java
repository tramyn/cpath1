// $Id: ProteinWithWeight.java,v 1.7 2006-02-22 22:47:50 grossb Exp $
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
package org.mskcc.pathdb.model;

import org.mskcc.dataservices.schemas.psi.ProteinInteractorType;

import java.util.Comparator;

/**
 * Encapsulates a Protein With a Score/Weight.
 * Used to sort a list of proteins.
 *
 * @author Ethan Cerami.
 */
public class ProteinWithWeight implements Comparator {
    private int numHits;
    private ProteinInteractorType protein;

    /**
     * Constructor.
     */
    public ProteinWithWeight() {
        protein = null;
        numHits = 0;
    }

    /**
     * Constructor.
     *
     * @param protein Protein Object.
     * @param numHits Number of Hits.
     */
    public ProteinWithWeight(ProteinInteractorType protein, int numHits) {
        this.protein = protein;
        this.numHits = numHits;
    }

    /**
     * Gets Number of Hits.
     *
     * @return Number of Hits.
     */
    public int getNumHits() {
        return numHits;
    }

    /**
     * Gets Protein Object.
     *
     * @return Protein Object.
     */
    public ProteinInteractorType getProtein() {
        return protein;
    }

    /**
     * Compares/orders two proteins.
     *
     * @param object1 Protein1
     * @param object2 Protein2
     * @return int value indicating sort order.
     */
    public int compare(Object object1, Object object2) {
        ProteinWithWeight protein1 = (ProteinWithWeight) object1;
        ProteinWithWeight protein2 = (ProteinWithWeight) object2;
        String id1 = protein1.getProtein().getId();
        String id2 = protein2.getProtein().getId();
        return id1.compareTo(id2);
    }
}
