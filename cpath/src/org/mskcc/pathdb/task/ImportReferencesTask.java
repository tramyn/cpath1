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

import org.mskcc.dataservices.bio.ExternalReference;
import org.mskcc.pathdb.sql.dao.DaoException;
import org.mskcc.pathdb.sql.dao.DaoExternalDbCv;
import org.mskcc.pathdb.sql.transfer.UpdatePsiInteractor;
import org.mskcc.pathdb.util.ConsoleUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Task to Import External References.
 *
 * @author Ethan Cerami.
 */
public class ImportReferencesTask extends Task {
    private Reader reader;
    private static final String EMPTY_FLAG = "---";
    private String db1, db2;
    private int numMatching = 0;
    private int numUpdates = 0;
    private ArrayList lines;

    /**
     * Constructor.
     *
     * @param verbose Verbose Flag.
     * @param reader  Reader object with file contents.
     */
    public ImportReferencesTask(boolean verbose, Reader reader) {
        super("Import External Refs");
        this.setVerbose(verbose);
        pMonitor = new ProgressMonitor();
        pMonitor.setCurrentMessage("Running");
        this.reader = reader;
        this.lines = new ArrayList();
    }

    /**
     * Runs Task.
     */
    public void run() {
        try {
            importReferences();
        } catch (Exception e) {
            setException(e);
            e.printStackTrace();
        }
    }

    /**
     * Imports External References.
     * This public is public, if you want to run within the existing thread.
     *
     * @throws IOException  Error Reading File.
     * @throws DaoException Error Accessing Database.
     */
    public void importReferences() throws IOException, DaoException {
        readFile(reader);
        pMonitor.setMaxValue(lines.size());
        outputMsg("Importing External References");
        outputMsg("Total # of Lines in file:  " + pMonitor.getMaxValue());
        processData();
        pMonitor.setCurrentMessage("Importing Complete -->  Number of "
                + "Refs Saved:  " + numUpdates);
        outputMsg("\nImporting Complete");
        displayResults();
    }

    /**
     * Gets the Progress Monitor.
     *
     * @return Progress Monitor Object.
     */
    public ProgressMonitor getProgressMonitor() {
        return pMonitor;
    }

    private void displayResults() {
        outputMsg("Total Number of References:  "
                + pMonitor.getCurValue());
        outputMsg("Total Number of Matching Interactors:  "
                + numMatching);
        outputMsg("Total Number of Refs Saved:  "
                + numUpdates);
    }

    /**
     * Count Total Number of Lines in the File.
     */
    private void readFile(Reader reader) throws IOException {
        BufferedReader buffered = new BufferedReader(reader);
        String line = buffered.readLine();
        while (line != null) {
            lines.add(line);
            line = buffered.readLine();
        }
    }

    /**
     * Read In File Data.
     */
    private void processData() throws DaoException {
        boolean dbNamesSet = false;
        for (int i = 0; i < lines.size(); i++) {
            String line = (String) lines.get(i);
            StringTokenizer tokenizer = new StringTokenizer(line, " \t");
            String token1 = (String) tokenizer.nextElement();
            String token2 = (String) tokenizer.nextElement();
            if (!dbNamesSet) {
                setDbNames(token1, token2);
                dbNamesSet = true;
            } else {
                ExternalReference ref1 =
                        new ExternalReference(db1, token1.trim());
                ExternalReference ref2 =
                        new ExternalReference(db2, token2.trim());
                updateRefs(ref1, ref2);
            }
        }
    }

    /**
     * Sets Global Database Names.
     */
    private void setDbNames(String token1, String token2) throws DaoException {
        DaoExternalDbCv daoCv = new DaoExternalDbCv();
        db1 = daoCv.getFixedCvTerm(token1);
        db2 = daoCv.getFixedCvTerm(token2);
    }

    /**
     * Conditionally Add External References.
     */
    private void updateRefs(ExternalReference ref1, ExternalReference ref2)
            throws DaoException {
        pMonitor.incrementCurValue();
        ConsoleUtil.showProgress(verbose, pMonitor);
        int index1 = ref1.getId().indexOf(EMPTY_FLAG);
        int index2 = ref2.getId().indexOf(EMPTY_FLAG);
        if (index1 == -1 && index2 == -1) {
            UpdatePsiInteractor updater = new UpdatePsiInteractor
                    (ref1, ref2, true);
            boolean needsUpdating = updater.needsUpdating();
            long id = updater.getcPathId();
            if (id > -1) {
                this.numMatching++;
            }
            if (needsUpdating) {
                updater.doUpdate();
                numUpdates++;
            }
        }
    }
}