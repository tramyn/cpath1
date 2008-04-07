package org.mskcc.pathdb.taglib;

import org.mskcc.pathdb.model.CPathRecord;
import org.mskcc.pathdb.model.GlobalFilterSettings;
import org.mskcc.pathdb.model.ExternalDatabaseSnapshotRecord;
import org.mskcc.pathdb.model.ExternalDatabaseRecord;
import org.mskcc.pathdb.sql.dao.DaoExternalDbSnapshot;
import org.mskcc.pathdb.sql.dao.DaoException;
import org.mskcc.pathdb.sql.dao.DaoExternalDb;
import org.mskcc.pathdb.form.WebUIBean;
import org.mskcc.pathdb.servlet.CPathUIConfig;
import org.mskcc.pathdb.servlet.CPathServlet;
import org.mskcc.pathdb.protocol.ProtocolRequest;
import org.mskcc.pathdb.lucene.LuceneQuery;
import org.mskcc.pathdb.lucene.LuceneConfig;
import org.mskcc.pathdb.xdebug.XDebug;
import org.mskcc.pathdb.sql.assembly.AssemblyException;
import org.apache.lucene.queryParser.ParseException;
import org.apache.log4j.Logger;
import org.mskcc.pathdb.sql.query.QueryException;
import org.mskcc.dataservices.util.PropertyManager;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.*;

/**
 * Custom JSP Tag for displaying a list of data sources
 *
 * @author Benjamin Gross
 */
public class DataSourceListTable extends HtmlTable {
    private Logger log = Logger.getLogger(DataSourceListTable.class);

    /**
     * Executes JSP Custom Tag
     *
     * @throws Exception Exception in writing to JspWriter.
     */
    protected void subDoStartTag() throws Exception {
        outputRecords();
    }

    /**
     * Output the Pathway List.
     */
    private void outputRecords() throws DaoException, QueryException, IOException,
										AssemblyException, ParseException {
        DaoExternalDbSnapshot dao = new DaoExternalDbSnapshot();
        ArrayList list = dao.getAllDatabaseSnapshots();
        WebUIBean webUIBean = CPathUIConfig.getWebUIBean();
        DaoExternalDb daoExternalDb = new DaoExternalDb();

        // process records
        if (list.size() == 0) {
            startTable();
            startRow();
            append("<TD COLSPAN=2>No Data Sources Available</TD>");
            endRow();
            endTable();
        } else {
			// create protocol request and filter objects
			ProtocolRequest protocolRequest = getProtocolRequest(webUIBean);
			GlobalFilterSettings filterSettings = new GlobalFilterSettings();
			// set proper type list for lucene query
			ArrayList<String> typeList = new ArrayList();
			typeList.add("pathway");
			filterSettings.setEntityTypeSelected(typeList);
            for (int i = 0; i < list.size(); i++) {
                ExternalDatabaseSnapshotRecord snapshotRecord =
                        (ExternalDatabaseSnapshotRecord) list.get(i);
                append("<table border='0' cellspacing='2' cellpadding='3' "
                    + "width='100%' class='datasource_table'>");
                append ("<tr class='c' valign=top>");
                append("<td valign=top width=50>");
                if (snapshotRecord.getExternalDatabase() != null) {
                    ExternalDatabaseRecord dbRecord = snapshotRecord.getExternalDatabase();
                    if (dbRecord.getIconFileExtension() != null) {
                        append("<img class='data_source_logo' src='jsp/images/database/"
                            + "db_" + dbRecord.getId() + "." + dbRecord.getIconFileExtension()
                            + "'/>");
                    }
                }
                append ("</td>");
                append ("<td valign=center>"
                        + DbSnapshotInfo.getDbSnapshotHtml(snapshotRecord.getId()));
                if (snapshotRecord.getExternalDatabase() != null) {
                    ExternalDatabaseRecord dbRecord = snapshotRecord.getExternalDatabase();
					// set proper data source for lucene query
					ArrayList<Long> dataSourceList = new ArrayList();
					dataSourceList.add(Long.valueOf(snapshotRecord.getId()));
					filterSettings.setSnapshotsSelected(dataSourceList);
					protocolRequest.setQuery("q=data_source:" + dbRecord.getMasterTerm());
					// do lucene query
					LuceneQuery search = new LuceneQuery(protocolRequest, filterSettings, new XDebug());
					search.executeSearch();
					// create the html
                    append ("<br><a href=\"webservice.do?version=");
                    append (webUIBean.getWebApiVersion());
                    append ("&format=html&cmd=get_by_keyword");
					append ("&" + ProtocolRequest.ARG_ENTITY_TYPE + "=" + (search.getTotalNumHits() > 0 ? "pathway" : "protein"));
					append ("&" + GlobalFilterSettings.NARROW_BY_DATA_SOURCES_FILTER_NAME + "=" + GlobalFilterSettings.NARROW_BY_DATA_SOURCES_FILTER_VALUE_GLOBAL);
					append ("&q=data_source%3A" + dbRecord.getMasterTerm() + "\">");
                    append ("Browse</a>");
                }
                append ("</td>");
                endRow();
                endTable();
            }
        }
    }

    /**
	 * Creates a ProtocolRequest Object.
	 */
	private ProtocolRequest getProtocolRequest(WebUIBean webUIBean) {
		HashMap<String,String> parameters = new HashMap<String,String>();
		parameters.put(ProtocolRequest.ARG_VERSION, webUIBean.getWebApiVersion());
		parameters.put(ProtocolRequest.ARG_FORMAT, "html");
		parameters.put(ProtocolRequest.ARG_COMMAND, "get_by_keyword");
		parameters.put(ProtocolRequest.ARG_ENTITY_TYPE, "pathway");
		return new ProtocolRequest(parameters);
	}
}
