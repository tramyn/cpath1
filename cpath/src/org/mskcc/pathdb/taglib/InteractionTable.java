/** Copyright (c) 2003 Institute for Systems Biology, University of
 ** California at San Diego, and Memorial Sloan-Kettering Cancer Center.
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
 ** documentation provided hereunder is on an "as is" basis, and the
 ** Institute for Systems Biology, the University of California at San Diego
 ** and/or Memorial Sloan-Kettering Cancer Center
 ** have no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall the
 ** Institute for Systems Biology, the University of California at San Diego
 ** and/or Memorial Sloan-Kettering Cancer Center
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if the
 ** Institute for Systems Biology, the University of California at San
 ** Diego and/or Memorial Sloan-Kettering Cancer Center
 ** have been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 **
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/
package org.mskcc.pathdb.taglib;

import org.mskcc.dataservices.bio.Interaction;
import org.mskcc.dataservices.bio.Interactor;
import org.mskcc.dataservices.bio.vocab.InteractionVocab;
import org.mskcc.dataservices.bio.vocab.InteractorVocab;
import org.mskcc.pathdb.controller.ProtocolConstants;
import org.mskcc.pathdb.controller.ProtocolRequest;
import org.mskcc.pathdb.sql.dao.DaoException;

import java.util.ArrayList;

/**
 * Custom JSP Tag for Displaying Interaction Table.
 *
 * @author Ethan Cerami
 */
public class InteractionTable extends HtmlTable {
    private ProtocolRequest protocolRequest;
    private ArrayList interactions;

    /**
     * Sets Interaction Parameter.
     * @param interactions ArrayList of Interaction objects.
     */
    public void setInteractions(ArrayList interactions) {
        this.interactions = interactions;
    }

    /**
     * Sets UID Parameter.
     * @param request Protocol Request
     */
    public void setProtocolRequest(ProtocolRequest request) {
        this.protocolRequest = request;
    }

    /**
     * Start Tag Processing.
     * @throws DaoException Database Access Error.
     */
    protected void subDoStartTag() throws DaoException {

        protocolRequest.setFormat(ProtocolConstants.FORMAT_PSI);
        String url = protocolRequest.getUri();
        String title = "Matching Interactions";

        createHeader(title);

        startTable();
        outputNumInteractions(url);
        String headers[] = {
            "Interactor", "Interactor", "Experimental System",
            "PubMed Reference"};

        createTableHeaders(headers);
        outputInteractions();
        endTable();
    }

    private void outputNumInteractions(String url) {
        if (interactions.size() > 0) {
            this.startRow(1);
            this.append("<td colspan=2>Total Number of Interactions:  "
                    + interactions.size());
            this.append("</td>");
            this.append("<td colspan=3>");
            this.append("<div class='right'>");
            this.append("<IMG SRC=\"jsp/images/xml_doc.gif\">&nbsp;");
            outputLink("View PSI-MI XML Format", url);
            this.append("</div>");
            this.append("</td>");
            this.endRow();
        }
    }

    /**
     * Outputs Interaction Data.
     */
    private void outputInteractions() throws DaoException {
        if (interactions.size() == 0) {
            append("<tr class='a'>");
            append("<td colspan=4>No Matching Interactions Found.  "
                    + "Please try again.</td>");
            append("</tr>");
        }
        for (int i = 0; i < interactions.size(); i++) {
            Interaction interaction = (Interaction) interactions.get(i);
            ArrayList interactors = interaction.getInteractors();

            startRow(i);
            Interactor interactor = (Interactor) interactors.get(0);
            outputInteractor(interactor);

            interactor = (Interactor) interactors.get(1);
            outputInteractor(interactor);

            String expSystem = (String) interaction.getAttribute
                    (InteractionVocab.EXPERIMENTAL_SYSTEM_NAME);
            outputDataField(expSystem);
            String pmid = (String) interaction.getAttribute
                    (InteractionVocab.PUB_MED_ID);
            String url = getPubMedLink(pmid);
            outputDataField(pmid, url);
            endRow();
        }
    }

    private void outputInteractor(Interactor interactor) {
        if (interactor != null) {
            String url = "interactor.do?id=" + interactor.getAttribute
                    (InteractorVocab.LOCAL_ID);
            String name = interactor.getName();
            String desc = (String) interactor.getAttribute
                    (InteractorVocab.FULL_NAME);
            String org = (String) interactor.getAttribute
                    (InteractorVocab.ORGANISM_SPECIES_NAME);
            if (org == null) {
                org = (String) interactor.getAttribute
                        (InteractorVocab.ORGANISM_COMMON_NAME);
            }
            StringBuffer interactorHtml = new StringBuffer();
            interactorHtml.append("<A HREF='" + url + "'>"
                    + name + "</A><br/><ul>");
            if (desc != null) {
                interactorHtml.append("<li>" + desc + "</li>");
            }
            if (org != null) {
                interactorHtml.append("<li>Organism:  " + org + "</li>");
            }
            interactorHtml.append("</ul>");
            outputDataField(interactorHtml.toString());
        }
    }

    /**
     * Gets PubMedLink.
     * @param pmid PMID.
     * @return URL to PubMed.
     */
    private String getPubMedLink(String pmid) {
        String url = "http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?"
                + "cmd=Retrieve&db=PubMed&list_uids=" + pmid + "&dopt=Abstract";
        return url;
    }
}