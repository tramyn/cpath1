package org.mskcc.pathdb.taglib;

import org.mskcc.dataservices.schemas.psi.NamesType;
import org.mskcc.dataservices.schemas.psi.ProteinInteractorType;
import org.mskcc.pathdb.lucene.LuceneConfig;
import org.mskcc.pathdb.model.ProteinWithWeight;
import org.mskcc.pathdb.protocol.ProtocolConstants;
import org.mskcc.pathdb.protocol.ProtocolRequest;

import java.util.Iterator;
import java.util.List;

/**
 * Custom JSP Tag for Displaying Matching Interactors.
 *
 * @author Ethan Cerami
 */
public class InteractorTable extends HtmlTable {
    private ProtocolRequest protocolRequest;
    private List interactorList;

    /**
     * Sets Interactor Set
     *
     * @param interactorList Interactor List
     */
    public void setInteractorList(List interactorList) {
        this.interactorList = interactorList;
    }

    /**
     * Sets Protocol Request Parameter.
     *
     * @param request Protocol Request
     */
    public void setProtocolRequest(ProtocolRequest request) {
        this.protocolRequest = request;
    }

    /**
     * Start Tag Processing.
     *
     * @throws org.mskcc.pathdb.sql.dao.DaoException
     *          Database Access Error.
     */
    protected void subDoStartTag() throws Exception {
        protocolRequest.setFormat(ProtocolConstants.FORMAT_PSI);
        String query = protocolRequest.getQuery();
        if (query != null
            && (query.indexOf(LuceneConfig.FIELD_INTERACTOR_ID) == -1)
            && interactorList != null
            && interactorList.size() > 0) {
            append("<div id=\"highlight\" class=\"toolgroup\">\n"
                    + "<div class=\"label\">\n"
                    + "<strong>Matching Proteins</strong>\n"
                    + "</div>"
                    + "<div class=\"body\">");
            outputInteractors();
            append("</div>\n</div>");
        }
    }

    /**
     * Outputs All Interactors.
     */
    private void outputInteractors() {
        Iterator iterator = interactorList.iterator();
        while (iterator.hasNext()) {
            ProteinWithWeight proteinWithWeight = (ProteinWithWeight)
                    iterator.next();
            ProteinInteractorType protein = proteinWithWeight.getProtein();
            NamesType names = protein.getNames();
            String proteinId = protein.getId();
            append("<div>");
            String href = getInteractionLink(LuceneConfig.FIELD_INTERACTOR_ID
                    + ":" + proteinId, ProtocolConstants.FORMAT_HTML);
            String toolTip = TagUtil.getLabel(names);
            String label = TagUtil.truncateLabel(toolTip);
            String link = TagUtil.createLink(toolTip, href, label);
            append (link);
            append("</div>");
        }
    }
}