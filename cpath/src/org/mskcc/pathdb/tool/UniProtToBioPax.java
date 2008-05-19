package org.mskcc.pathdb.tool;

import org.mskcc.pathdb.task.ProgressMonitor;
import org.mskcc.pathdb.util.tool.ConsoleUtil;
import org.mskcc.pathdb.util.file.UniProtFileUtil;
import org.mskcc.pathdb.util.file.FileUtil;
import org.biopax.paxtools.model.BioPAXFactory;
import org.biopax.paxtools.model.BioPAXLevel;
import org.biopax.paxtools.model.Model;
import org.biopax.paxtools.model.BioPAXElement;
import org.biopax.paxtools.model.level2.*;
import org.biopax.paxtools.impl.level2.Level2FactoryImpl;
import org.biopax.paxtools.io.simpleIO.SimpleExporter;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class for parsing UniProt Flat Text Files, and converting into appropriate
 * BioPAX Physical Entities.
 * // TODO:  Add JUnit Test.
 *
 * @author Ethan Cerami.
 */
public class UniProtToBioPax {
    private int numPeRecords = 0;
    private ProgressMonitor pMonitor;
    private static Level2Factory bpFactory = new Level2FactoryImpl();

    /**
     * Empty Arg Constructor.
     */
    public UniProtToBioPax() {
    }

    /**
     * Constructor.
     *
     * @param pMonitor  Progress Monitor.
     */
    public UniProtToBioPax(ProgressMonitor pMonitor) {
        this.pMonitor = pMonitor;
    }

    /**
     * Parses a UniProt File and converts to BioPAX.
     *
     * @param uniProtFile       UniProt Flat File.
     * @param bpOutFile         BioPAX Output File.
     * @return                  Number of physical entity records created.
     * @throws java.io.IOException      Error Reading File.
     * @throws IllegalAccessException   BioPAX Output Error.
     * @throws InvocationTargetException BioPAX Output Error.
     */
    public int convertToBioPax(File uniProtFile, File bpOutFile) throws IOException,
            IllegalAccessException, InvocationTargetException {
        FileReader reader= null;
        FileWriter acWriter = null;
        try {
            reader = new FileReader (uniProtFile);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();

            protein currentProtein = null;
            Model bpModel = bpFactory.createModel();
            HashMap dataElements = new HashMap();
            while (line != null) {
                if (pMonitor != null) {
                    pMonitor.incrementCurValue();
                    ConsoleUtil.showProgress(pMonitor);
                }
                if (line.startsWith ("//")) {
                    StringBuffer name = (StringBuffer) dataElements.get("DE");
                    StringBuffer id = (StringBuffer) dataElements.get("ID");
                    StringBuffer organismName = (StringBuffer) dataElements.get("OS");
                    StringBuffer organismTaxId = (StringBuffer) dataElements.get("OX");
                    StringBuffer comments = (StringBuffer) dataElements.get("CC");
                    StringBuffer geneName = (StringBuffer) dataElements.get("GN");
                    StringBuffer acNames = (StringBuffer) dataElements.get("AC");

                    currentProtein = bpFactory.createProtein();
                    String idParts[] = id.toString().split("\\s");
                    String shortName = idParts[0];
                    currentProtein.setSHORT_NAME(shortName);
                    currentProtein.setRDFId(shortName);
                    currentProtein.setNAME(name.toString());
                    setOrganism(organismName.toString(), organismTaxId.toString(),
                            currentProtein, bpModel);
                    setComments (comments.toString(), currentProtein);
                    setGeneName (geneName.toString(), currentProtein, bpModel);
                    setUniProtAccessionNumbers(acNames.toString(), currentProtein, bpModel);
                    // TODO:  Add UniProt Accession Numbers
                    // TODO:  Add Ref Seq IDs
                    // TODO:  Add Entrez Gene IDs
                    bpModel.add(currentProtein);
                    dataElements = new HashMap();
                } else {
                    String key = line.substring (0, 2);
                    String data = line.substring(5);
                    if (data.startsWith("-------") ||
                            data.startsWith("Copyrighted") ||
                            data.startsWith("Distributed")) {
                        //  do nothing here...
                    } else {
                        if (dataElements.containsKey(key)) {
                            StringBuffer existingData = (StringBuffer) dataElements.get(key);
                            existingData.append (" " + data);
                        } else {
                            dataElements.put(key, new StringBuffer (data));
                        }
                    }
                }
                line = bufferedReader.readLine();
            }
            //  TODO:  Output to file, not String
            //  TODO:  Do batch export of ~100 proteins in each file
            SimpleExporter exporter = new SimpleExporter(BioPAXLevel.L2);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            exporter.convertToOWL(bpModel, out);
            System.out.println (out.toString());
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (acWriter != null) {
                acWriter.close();
            }
        }
        return numPeRecords;
    }

    private void setOrganism(String organismName, String organismTaxId,
            protein currentProtein, Model bpModel) {
        organismTaxId = organismTaxId.replaceAll(";", "");
        String parts[] = organismTaxId.split("=");
        String taxId = parts[1];
        parts = organismName.split("\\(");
        String name = parts[0].trim();
        Map<String, BioPAXElement> bpMap = bpModel.getIdMap();
        String rdfId = "BIO_SOURCE_NCBI_" + taxId;
        if (bpMap.containsKey(rdfId)) {
            bioSource currentBioSource = (bioSource) bpMap.get(rdfId);
            currentProtein.setORGANISM(currentBioSource);
        } else {
            bioSource currentBioSource = bpFactory.createBioSource();
            currentBioSource.setNAME(name);
            unificationXref taxonXref = bpFactory.createUnificationXref();
            taxonXref.setDB("NCBI_taxonomy");
            taxonXref.setID(taxId);
            taxonXref.setRDFId("TAXON_NCBI_" + taxId);
            currentBioSource.setTAXON_XREF(taxonXref);
            currentBioSource.setRDFId(rdfId);
            currentProtein.setORGANISM(currentBioSource);
            bpModel.add(currentBioSource);
            bpModel.add(taxonXref);
        }
    }

    private void setComments (String comments, protein currentProtein) {
        String commentParts[] = comments.split("-!- ");
        StringBuffer reducedComments = new StringBuffer();
        for (int i=0; i<commentParts.length; i++) {
            String currentComment = commentParts[i];
            //  Filter out the Interaction comments.
            if (!currentComment.startsWith("INTERACTION")) {
                currentComment = currentComment.replaceAll("     ", " ");
                reducedComments.append (currentComment);
            }
        }
        HashSet <String> commentSet = new HashSet();
        commentSet.add(reducedComments.toString());
        currentProtein.setCOMMENT(commentSet);
    }

    private void setUniProtAccessionNumbers (String acNames, protein currentProtein,
            Model bpModel) {
        String acList[] = acNames.split(";");
        Map<String, BioPAXElement> bpMap = bpModel.getIdMap();
        for (int i=0; i<acList.length; i++) {
            String ac = acList[i].trim();
            String rdfId = "UNIPROT_AC_" + ac;
            if (bpMap.containsKey(rdfId)) {
                unificationXref xref = (unificationXref) bpMap.get(rdfId);
                currentProtein.addXREF(xref);
            } else {
                unificationXref xref = bpFactory.createUnificationXref();
                xref.setRDFId(rdfId);
                xref.setDB("UNIPROT");
                xref.setID(ac);
                bpModel.add(xref);
                currentProtein.addXREF(xref);
            }
        }
    }

    private void setGeneName (String geneName, protein currentProtein, Model bpModel) {
        String parts[] = geneName.split(";");
        for (int i=0; i<parts.length; i++) {
            String subParts[] = parts[i].split("=");
            // Set HUGO Gene Name
            if (subParts[0].trim().equals("Name")) {
                geneName = subParts[1];
                Map<String, BioPAXElement> bpMap = bpModel.getIdMap();
                String rdfId = "HUGO_GENE_SYMBOL_" + geneName;
                if (bpMap.containsKey(rdfId)) {
                    relationshipXref xref = (relationshipXref) bpMap.get(rdfId);
                    currentProtein.addXREF(xref);
                } else {
                    relationshipXref xref = bpFactory.createRelationshipXref();
                    xref.setRDFId(rdfId);
                    xref.setDB("HUGO_GENE_SYMBOL");
                    xref.setID(geneName);
                    bpModel.add(xref);
                    currentProtein.addXREF(xref);
                }
            } else if (subParts[0].trim().equals("Synonyms")) {
                String synList[] = subParts[1].split(",");
                for (int j=0; j<synList.length; j++) {
                    String currentSynonym = synList[j];
                    currentProtein.addSYNONYMS(currentSynonym.trim());
                }
            }
        }
    }

    /**
     * Command Line Usage.
     * @param args          Must include UniProt File Name.
     * @throws java.io.IOException  IO Error.
     * // TODO:  Set up command line tool.
     * // TODO:  Verify that BioPAX created via this method can be imported into cPath.
     */
    public static void main(String[] args) throws IOException, IllegalAccessException,
            InvocationTargetException {
        if (args.length == 0) {
            System.out.println ("command line usage:  uniprot2biopax.pl <uniprot_file.dat>");
            System.exit(1);
        }
        ProgressMonitor pMonitor = new ProgressMonitor();
        pMonitor.setConsoleMode(true);

        File uniProtFile = new File (args[0]);
        File bpOutFile = UniProtFileUtil.getOrganismSpecificFileName(uniProtFile, "uniprot_bp");

        System.out.println ("Reading data from:  " + uniProtFile.getAbsolutePath());
        int numLines = FileUtil.getNumLines(uniProtFile);
        System.out.println (" --> total number of lines:  " + numLines);
        pMonitor.setMaxValue(numLines);
        System.out.println ("Writing out to:  " + bpOutFile.getAbsolutePath());
        UniProtToBioPax parser = new UniProtToBioPax(pMonitor);
        int numRecords = parser.convertToBioPax(uniProtFile, bpOutFile);
        System.out.println ("Total number of physical entity records created:  " + numRecords);
    }
}
