package org.mskcc.pathdb.tool;

import org.mskcc.pathdb.model.*;
import org.mskcc.pathdb.schemas.binary_interaction.assembly.BinaryInteractionAssembly;
import org.mskcc.pathdb.schemas.binary_interaction.assembly.BinaryInteractionAssemblyFactory;
import org.mskcc.pathdb.schemas.binary_interaction.util.BinaryInteractionUtil;
import org.mskcc.pathdb.sql.assembly.AssemblyException;
import org.mskcc.pathdb.sql.assembly.XmlAssembly;
import org.mskcc.pathdb.sql.assembly.XmlAssemblyFactory;
import org.mskcc.pathdb.sql.dao.DaoCPath;
import org.mskcc.pathdb.sql.dao.DaoException;
import org.mskcc.pathdb.sql.dao.DaoExternalDbSnapshot;
import org.mskcc.pathdb.sql.dao.DaoExternalLink;
import org.mskcc.pathdb.sql.JdbcUtil;
import org.mskcc.pathdb.task.ProgressMonitor;
import org.mskcc.pathdb.util.ExternalDatabaseConstants;
import org.mskcc.pathdb.util.tool.ConsoleUtil;
import org.mskcc.pathdb.xdebug.XDebug;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hp.hpl.jena.shared.JenaException;

/**
 * Command Line Utility to Dump all Manually Curated Human Interactions in SIF-Like
 * output file format.
 */
public class DumpNetworks {
    private ProgressMonitor pMonitor;
    private File outFile;
    private HashSet curatedDbSet = new HashSet();
    private final static String TAB = "\t";
    private static final int BLOCK_SIZE = 1000;

    /**
     * Constructor.
     *
     * @param pMonitor Progress Monitor.
     */
    public DumpNetworks(ProgressMonitor pMonitor, File outFile) throws IOException {
        this.pMonitor = pMonitor;
        this.outFile = outFile;
        curatedDbSet.add("REACTOME");
        curatedDbSet.add("CELL_MAP");
        curatedDbSet.add("NCI_NATURE");
        curatedDbSet.add("HPRD");
        ToolInit.initProps();
    }

    /**
     * Dump the reactions to the specified text file.
     * @throws IOException          IO Error.
     * @throws DaoException         Database Error.
     * @throws AssemblyException    XML/SIF Assembly Error.
     */
    public void dump() throws IOException, DaoException, AssemblyException {
        FileWriter fileWriter = new FileWriter(outFile);
        try {
            DaoCPath dao = DaoCPath.getInstance();
            Connection con = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            CPathRecord record = null;
            long maxIterId = dao.getMaxCpathID();
            pMonitor.setMaxValue((int)maxIterId);
            for (int id = 0; id <= maxIterId; id = id + BLOCK_SIZE + 1) {
                //  System.out.println("Starting batch, starting at cPath id= " + id);

                // setup start/end id to fetch
                long startId = id;
                long endId = id + BLOCK_SIZE;
                if (endId > maxIterId) endId = maxIterId;

                try {
                    con = JdbcUtil.getCPathConnection();
                    pstmt = con.prepareStatement("select * from cpath WHERE "
                            + " CPATH_ID BETWEEN " + startId + " and " + endId
                            + " order by CPATH_ID ");
                    rs = pstmt.executeQuery();

                    while (rs.next()) {
                        if (pMonitor != null) {
                            pMonitor.incrementCurValue();
                            ConsoleUtil.showProgress(pMonitor);
                        }
                        record = dao.extractRecord(rs);
                        if (record.getType() == CPathRecordType.INTERACTION) {
                            dumpInteractionRecord(record, fileWriter);
                        }
                    }
                } catch (Exception e1) {
                    throw new DaoException(e1);
                }
                JdbcUtil.closeAll(con, pstmt, rs);
            }
        } finally {
            fileWriter.flush();
            fileWriter.close();
        }
    }

    private void dumpInteractionRecord(CPathRecord record, FileWriter fileWriter)
            throws DaoException, AssemblyException, IOException {
        DaoExternalDbSnapshot daoSnapshot = new DaoExternalDbSnapshot();
        long snapshotId = record.getSnapshotId();
        ExternalDatabaseSnapshotRecord snapshotRecord =
                daoSnapshot.getDatabaseSnapshot(snapshotId);
        String dbTerm = snapshotRecord.getExternalDatabase().getMasterTerm();

        // If this is a curated db, dump the SIF.  otherwise, skip
        if (curatedDbSet.contains(dbTerm)) {
            long ids[] = new long[1];
            ids[0] = record.getId();
            XmlAssembly xmlAssembly = XmlAssemblyFactory.createXmlAssembly(ids,
                    XmlRecordType.BIO_PAX, 1, XmlAssemblyFactory.XML_FULL, true,
                    new XDebug());
            // determine binary interaction assembly type
            BinaryInteractionAssemblyFactory.AssemblyType binaryInteractionAssemblyType =
                    BinaryInteractionAssemblyFactory.AssemblyType.SIF;

            // contruct rule types
            List<String> binaryInteractionRuleTypes = BinaryInteractionUtil.getRuleTypes();

            // get binary interaction assembly
            try {
                BinaryInteractionAssembly sifAssembly =
                        BinaryInteractionAssemblyFactory.createAssembly
                                (binaryInteractionAssemblyType, binaryInteractionRuleTypes,
                                        xmlAssembly.getXmlString());
                String sif = sifAssembly.getBinaryInteractionString();
                convertIdsToGeneSymbols(dbTerm, record.getId(), sif, fileWriter);
            } catch (JenaException e) {
                pMonitor.logWarning("Got JenaException:  " + e.getMessage() + ".  Occurred "
                    + " while getting SIF for interaction:  " + record.getId() + ", Data Source:  "
                    + dbTerm);
            }
        }
    }

    private void convertIdsToGeneSymbols(String dbSource, long interactionId,
            String sif, FileWriter fileWriter) throws DaoException, IOException {
        String lines[] = sif.split("\\n");
        DaoCPath dao = DaoCPath.getInstance();
        for (int i=0; i<lines.length; i++) {
            String line = lines[i];
            if (line.length() > 0) {
                String parts[] = lines[i].split("\\s");
                String id0 = parts[0];
                String intxnType = parts[1];
                String id1 = parts[2];
                CPathRecord record0 = dao.getRecordById(Integer.parseInt(id0));
                CPathRecord record1 = dao.getRecordById(Integer.parseInt(id1));
                // Only include human-human interactions
                if (record0.getNcbiTaxonomyId() == 9606 && record1.getNcbiTaxonomyId() == 9606) {
                    String gene0 = getGeneSymbol(Integer.parseInt(id0));
                    String gene1 = getGeneSymbol(Integer.parseInt(id1));
                    //  Only dump, if we have gene symbols for both
                    if (gene0 != null && gene1 != null) {
                        fileWriter.write(gene0 + TAB);
                        fileWriter.write(intxnType + TAB);
                        fileWriter.write(gene1 + TAB);
                        fileWriter.write(dbSource + TAB);
                        fileWriter.write(interactionId + "\n");
                    }
                }
            }
        }
    }

    private String getGeneSymbol(long cpathId) throws DaoException {
        DaoExternalLink daoExternalLink = DaoExternalLink.getInstance();
        ArrayList <ExternalLinkRecord> xrefList =
                daoExternalLink.getRecordsByCPathId(cpathId);
        for (ExternalLinkRecord xref:  xrefList) {
            if (xref.getExternalDatabase().getMasterTerm().equals
                    (ExternalDatabaseConstants.GENE_SYMBOL)) {
                return xref.getLinkedToId();
            }
        }
        return null;
    }

    /**
     * Command Line Usage.
     *
     * @param args Must include UniProt File Name.
     * @throws IOException IO Error.
     */
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("command line usage:  dumpHumanSif.pl <output.txt>");
            System.exit(1);
        }
        ProgressMonitor pMonitor = new ProgressMonitor();
        pMonitor.setConsoleMode(true);

        File file = new File(args[0]);
        System.out.println("Writing out to:  " + file.getAbsoluteFile());
        DumpNetworks dumper = new DumpNetworks(pMonitor, file);
        dumper.dump();

        ArrayList <String> warningList = pMonitor.getWarningList();
        System.out.println ("Total number of warning messages:  " + warningList.size());
        int i = 1;
        for (String warning:  warningList) {
            System.out.println ("Warning #" + i + ":  " + warning);
            i++;
        }
    }
}