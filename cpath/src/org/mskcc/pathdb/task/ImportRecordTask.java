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
package org.mskcc.pathdb.task;

import org.mskcc.pathdb.model.ImportRecord;
import org.mskcc.pathdb.model.ImportSummary;
import org.mskcc.pathdb.sql.dao.DaoException;
import org.mskcc.pathdb.sql.dao.DaoImport;
import org.mskcc.pathdb.sql.transfer.ImportException;
import org.mskcc.pathdb.sql.transfer.ImportPsiToCPath;

/**
 * Task to Import New Data Records into cPath.
 *
 * @author Ethan Cerami.
 */
public class ImportRecordTask extends Task {
    private long importId;
    private ImportSummary summary;

    /**
     * Constructor.
     *
     * @param importId Import ID.
     * @param consoleMode Console Mode.
     */
    public ImportRecordTask(long importId, boolean consoleMode) {
        super("Import PSI-MI Record", consoleMode);
        this.importId = importId;
        ProgressMonitor pMonitor = this.getProgressMonitor();
        pMonitor.setConsoleMode (consoleMode);
        pMonitor.setCurrentMessage("Import PSI-MI Record");
    }

    /**
     * Runs Task.
     */
    public void run() {
        try {
            transferRecord(importId);
        } catch (Exception e) {
            setException(e);
            e.printStackTrace();
        }
    }

    /**
     * Transfers Single Import Record.
     */
    private void transferRecord(long importId) throws ImportException,
            DaoException {
        ProgressMonitor pMonitor = this.getProgressMonitor();
        DaoImport dbImport = new DaoImport();
        ImportRecord record = dbImport.getRecordById(importId);
        String xml = record.getData();
        ImportPsiToCPath importer = new ImportPsiToCPath();
        summary = importer.addRecord(xml, true, pMonitor);
        pMonitor.setCurrentMessage("Importing Complete<BR>-->  Total Number "
                + "of Interactions Processed:  "
                + summary.getNumInteractionsSaved());
        dbImport.markRecordAsTransferred(record.getImportId());
    }
}