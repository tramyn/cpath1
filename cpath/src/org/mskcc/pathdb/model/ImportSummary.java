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
package org.mskcc.pathdb.model;

/**
 * Contains Summary Information for Import to Database.
 *
 * @author Ethan Cerami
 */
public class ImportSummary {
    private int numInteractorsProcessed;
    private int numInteractorsSaved;
    private int numInteractorsFound;
    private int numInteractionsSaved;
    private int numInteractionsClobbered;

    /**
     * Gets Total Number of Interactors Processed.
     *
     * @return integer.
     */
    public int getNumInteractorsProcessed() {
        return numInteractorsProcessed;
    }

    /**
     * Increments Number of Interactors Processed.
     */
    public void incrementNumInteractorsProcessed() {
        this.numInteractorsProcessed++;
    }

    /**
     * Gets Number of Interactors Saved to the cPath database.
     *
     * @return integer.
     */
    public int getNumInteractorsSaved() {
        return numInteractorsSaved;
    }


    /**
     * Increments Number of Interactors Saved to the cPath database.
     */
    public void incrementNumInteractorsSaved() {
        this.numInteractorsSaved++;
    }

    /**
     * Gets Number of Interactors Found in the Database.
     * These interactors were identified by their external references,
     * and will not be saved in the database.
     *
     * @return integer.
     */
    public int getNumInteractorsFound() {
        return numInteractorsFound;
    }

    /**
     * Incremenets the Number of Interactors Found in the Database.
     */
    public void incrementNumInteractorsFound() {
        this.numInteractorsFound++;
    }

    /**
     * Gets Number of Interactions Saved to the cPath Database.
     *
     * @return integer.
     */
    public int getNumInteractionsSaved() {
        return this.numInteractionsSaved;
    }

    /**
     * Increments Number of Interactions Saved to the Database.
     */
    public void incrementNumInteractionsSaved() {
        this.numInteractionsSaved++;
    }

    /**
     * Gets Number of Interactions Saved to the cPath Database.
     *
     * @return integer.
     */
    public int getNumInteractionsClobbered() {
        return this.numInteractionsClobbered;
    }

    /**
     * Increments Number of New Interactions which clobbered old interactions.
     */
    public void incrementNumInteractionsClobbered() {
        this.numInteractionsClobbered++;
    }


}