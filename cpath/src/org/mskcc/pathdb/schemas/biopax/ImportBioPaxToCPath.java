package org.mskcc.pathdb.schemas.biopax;

import org.jdom.Element;
import org.jdom.JDOMException;
import org.mskcc.dataservices.bio.ExternalReference;
import org.mskcc.pathdb.model.CPathRecord;
import org.mskcc.pathdb.model.ImportSummary;
import org.mskcc.pathdb.model.XmlRecordType;
import org.mskcc.pathdb.model.CPathRecordType;
import org.mskcc.pathdb.sql.assembly.CPathIdFilter;
import org.mskcc.pathdb.sql.dao.DaoCPath;
import org.mskcc.pathdb.sql.dao.DaoException;
import org.mskcc.pathdb.sql.dao.DaoExternalLink;
import org.mskcc.pathdb.sql.dao.DaoInternalLink;
import org.mskcc.pathdb.sql.transfer.ImportException;
import org.mskcc.pathdb.task.ProgressMonitor;
import org.mskcc.pathdb.util.rdf.RdfValidator;
import org.mskcc.pathdb.util.xml.XmlUtil;
import org.mskcc.pathdb.util.tool.ConsoleUtil;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Imports BioPAX Data into cPath.
 *
 * @author Ethan Cerami.
 */
public class ImportBioPaxToCPath {
    private HashMap idMap = new HashMap();
    private ArrayList resourceList;
    private ArrayList cPathRecordList;
    private ArrayList oldIdList;
    private BioPaxUtil bpUtil;
    private ProgressMonitor pMonitor;
    private ImportSummary importSummary;

    /**
     * Adds BioPAX Data to cPath.
     *
     * @param xml      BioPAX XML String.
     * @param pMonitor ProgressMonitor Object.
     * @return ImportSummary Object.
     * @throws ImportException Error Importing BioPAX Data.
     */
    public ImportSummary addRecord(String xml, ProgressMonitor pMonitor)
            throws ImportException {
        this.pMonitor = pMonitor;
        this.importSummary = new ImportSummary();
        try {
            validateRdf(new StringReader(xml));
            massageBioPaxData(new StringReader(xml));
            storeRecords();
            storeLinks();
        } catch (IOException e) {
            throw new ImportException(e);
        } catch (DaoException e) {
            throw new ImportException(e);
        } catch (JDOMException e) {
            throw new ImportException(e);
        } catch (SAXException e) {
            throw new ImportException(e);
        }
        return importSummary;
    }

    /**
     * Validates RDF Data.
     */
    private void validateRdf(Reader reader) throws IOException, SAXException,
            ImportException {
        RdfValidator rdfValidator = new RdfValidator(reader);
        boolean hasErrors = rdfValidator.hasErrorsOrWarnings();
        pMonitor.setCurrentMessage("Validating BioPAX with RDF Validator");
        if (hasErrors) {
            pMonitor.setCurrentMessage(rdfValidator.getReadableErrorList());
            throw new ImportException("RDF Validation Errors");
        }
        pMonitor.setCurrentMessage("   --->  BioPAX RDF is Valid");
    }

    /**
     * Prepares data via BioPaxUtil and TransformBioPaxToCPathRecords.
     */
    public void massageBioPaxData(Reader reader) throws ImportException,
            DaoException, IOException, JDOMException {

        bpUtil = new BioPaxUtil(reader, pMonitor);

        //  Check for Errors in BioPAX Transformation
        ArrayList errorList = bpUtil.getErrorList();
        if (errorList != null && errorList.size() > 0) {
            for (int i = 0; i < errorList.size(); i++) {
                String errMsg = (String) errorList.get(i);
                pMonitor.setCurrentMessage(errMsg);
            }
            throw new ImportException("BioPAX Import Error");
        }

        resourceList = bpUtil.getTopLevelComponentList();

        TransformBioPaxToCPathRecords transformer =
                new TransformBioPaxToCPathRecords(resourceList);
        cPathRecordList = transformer.getcPathRecordList();
        oldIdList = transformer.getIdList();
    }

    /**
     * Stores RDF Resource Placeholders (without XML) to MySQL.
     */
    private void storeRecords() throws DaoException {
        DaoCPath dao = new DaoCPath();
        pMonitor.setCurrentMessage("Storing records to MySQL:");
        pMonitor.setMaxValue(cPathRecordList.size());
        for (int i = 0; i < cPathRecordList.size(); i++) {
            CPathRecord record = (CPathRecord) cPathRecordList.get(i);
            String oldId = (String) oldIdList.get(i);
            long cPathId = dao.addRecord(record.getName(),
                    record.getDescription(),
                    record.getNcbiTaxonomyId(),
                    record.getType(),
                    record.getSpecificType(),
                    XmlRecordType.BIO_PAX,
                    "[PLACE_HOLDER]");

            //  Store a mapping between the old ID and the new ID
            idMap.put(oldId, new Long(cPathId));

            //  Store cPath ID directly, for later reference
            record.setId(cPathId);

            if (record.getType().equals(CPathRecordType.PATHWAY)) {
                importSummary.incrementNumPathwaysSaved();
            } else if (record.getType().equals(CPathRecordType.INTERACTION)) {
                importSummary.incrementNumInteractionsSaved();
            } else {
                importSummary.incrementNumPhysicalEntitiesSaved();
            }
            pMonitor.incrementCurValue();
            ConsoleUtil.showProgress(pMonitor);
        }
    }

    /**
     * 1)  Update all Internal Links to point to correct cPath IDs.
     * 2)  Stores the newly modified XML to MySQL.
     * 3)  Store Internal Links to cPath.
     * 4)  Store External Links to cPath.
     */
    private void storeLinks() throws JDOMException, IOException,
            DaoException {
        //  Update all JDOM Elements in Resource List to reference new
        //  cPath IDs.
        UpdateRdfLinks linker = new UpdateRdfLinks();
        linker.updateInternalLinks
                (resourceList, idMap, CPathIdFilter.CPATH_PREFIX);

        //  1)  Store the newly modified XML to MySQL
        //  2)  Store the Internal Links to MySQL
        //  3)  Store the External Links to MySQL
        DaoCPath daoCPath = new DaoCPath();
        DaoInternalLink internalLinker = new DaoInternalLink();
        DaoExternalLink externalLinker = new DaoExternalLink();

        pMonitor.setCurrentMessage("Storing Internal / External "
            + "Links to MySQL:");
        pMonitor.setMaxValue(cPathRecordList.size());
        for (int i = 0; i < cPathRecordList.size(); i++) {
            CPathRecord record = (CPathRecord) cPathRecordList.get(i);
            Element resource = (Element) resourceList.get(i);
            String xml = XmlUtil.serializeToXml(resource);
            daoCPath.updateXml(record.getId(), xml);

            //  Store Internal Links
            long internalLinks[] = linker.getInternalLinks(record.getId());
            if (internalLinks != null && internalLinks.length > 0) {
                internalLinker.addRecords(record.getId(), internalLinks);
            }

            //  Store External Links
            ExternalReference refs[] =
                    bpUtil.extractExternalReferences(resource);
            externalLinker.addMulipleRecords(record.getId(), refs, false);
            pMonitor.incrementCurValue();
            ConsoleUtil.showProgress(pMonitor);
        }
    }
}