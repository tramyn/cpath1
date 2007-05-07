<%@ page import="java.util.ArrayList"%>
<%@ page import="org.mskcc.pathdb.action.BioPaxParentChild"%>
<%@ page import="org.mskcc.pathdb.taglib.DbSnapshotInfo"%>
<%@ page import="org.mskcc.pathdb.schemas.biopax.summary.*"%>
<%@ page import="org.mskcc.pathdb.sql.dao.DaoException"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="org.mskcc.pathdb.taglib.ReactomeCommentUtil"%>
<%@ page import="org.mskcc.pathdb.taglib.ReferenceUtil"%>
<%@ page import="org.mskcc.pathdb.action.admin.AdminWebLogging"%>
<%@ page import="org.mskcc.pathdb.sql.dao.DaoInternalLink"%>
<%@ page import="org.mskcc.pathdb.model.*"%>
<%@ page import="org.mskcc.pathdb.sql.dao.DaoCPath"%>
<%@ page import="java.io.IOException"%>
<%@ taglib uri="/WEB-INF/taglib/cbio-taglib.tld" prefix="cbio" %>
<%@ page errorPage = "JspError.jsp" %>

<%
int start = 0;
String startIndex = request.getParameter(BioPaxParentChild.START_INDEX_PARAMETER);
if (startIndex != null) {
    start = Integer.parseInt(startIndex);
}
start++;
String type = request.getParameter(BioPaxParentChild.TYPE_PARAMETER);
String showHeader = request.getParameter("showHeader");
boolean headerFlag = true;
if (showHeader != null && showHeader.equals("false")) {
    headerFlag = false;
}

int max = BioPaxParentChild.MAX_RECORDS;

int total = 9999;
String totalNumRecords = request.getParameter(BioPaxParentChild.TOTAL_NUM_RECORDS_PARAMETER);
if (totalNumRecords != null) {
    total = Integer.parseInt(totalNumRecords);
}

int stop = start + max -1;
if (stop > total) {
    stop = total;
}
HashMap<String, Reference> referenceMap =
        (HashMap<String,Reference>)request.getAttribute(BioPaxParentChild.KEY_PMID_MAP);

boolean debugMode = false;
String xdebugSession = (String) session.getAttribute
        (AdminWebLogging.WEB_LOGGING);
String xdebugParameter = request.getParameter(AdminWebLogging.WEB_LOGGING);
if (xdebugSession != null || xdebugParameter != null) {
    debugMode = true;
}
%>

<% if (headerFlag) { %>
<table WIDTH=100% CELLSPACING=0 CELLPADDING=4>
    <tr bgcolor=#DDDDDD>
        <td>Showing
        <span id="<%= "start_"+type%>"><%= start %></span>-<span id="<%= "stop_"+type%>"><%= stop %></span> of
        <span id="<%= "total_"+type%>"><%= total %></span>
        </td>
        <td align=right>
        <%
            String nextStyle = "none";
            if (stop < total) {
                nextStyle = "inline";
            }
            String prevStyle = "none";
            if (start > 1) {
                prevStyle = "inline";
            }
        %>
        <span style="display:<%= prevStyle %>" class="button" id="prev_<%= type %>" onClick="getPreviousData('<%= type %>')">&lt; Previous</span>
        &nbsp;
        <span style="display:<%= nextStyle %>" class="button" id="next_<%= type %>" onClick="getNextData('<%= type %>')">Next &gt;</span>
        </td>
    </tr>
</table>
<% } %>

<div id="content_<%= type%>">
<table width=100% CELLSPACING=0 CELLPADDING=4>
<%
ArrayList <BioPaxRecordSummary>  bpSummaryList = (ArrayList<BioPaxRecordSummary>)
        request.getAttribute(BioPaxParentChild.KEY_BP_SUMMARY_LIST);
HashMap <Long, EntitySummary> interactionSummaryMap = (HashMap <Long, EntitySummary>)
        request.getAttribute(BioPaxParentChild.KEY_INTERACTION_SUMMARY_MAP);

int index = start;
for (int i = 0; i < bpSummaryList.size(); i++) {
    out.println(getStartRow(i));
    BioPaxRecordSummary bpSummary = bpSummaryList.get(i);
    out.println("<td>");
    out.println(index + ". ");
    if (debugMode) {
        out.println("[<a href='record2.do?debug=1&id=" + bpSummary.getRecordID()
            + "'>" + bpSummary.getRecordID() + "</a>]");
        out.println("[<a href='record.do?format=xml_abbrev&id=" + bpSummary.getRecordID()
            + "'>BP Abbrev</a>]");
    }
    if (interactionSummaryMap.containsKey(bpSummary.getRecordID())) {
        EntitySummary entitySummary = interactionSummaryMap.get(bpSummary.getRecordID());
        if (entitySummary instanceof InteractionSummary) {
            InteractionSummary interactionSummary = (InteractionSummary) entitySummary;
            String interactionString = InteractionSummaryUtils.createInteractionSummaryString
                (interactionSummary);
            if (interactionString != null) {
                out.println("<span class='entity_summary'>" + interactionString);
            }
            outputParentInteractions(interactionSummary, out, debugMode);
            out.println("</span>");
            out.println("</td>");
            out.println(getBioPaxDetailsHtml(bpSummary, referenceMap, i));
        } else {
            out.println(getBioPaxRecordHtml(bpSummary, referenceMap, i));
        }
    } else {
        out.println(getBioPaxRecordHtml(bpSummary, referenceMap, i));
    }
    out.println("</tr>");
    index++;
}
%>

<%!
/**
 * Outputs parent interactions, e.g. controllers.
 */
private ArrayList outputParentInteractions (InteractionSummary interactionSummary,
        JspWriter out, boolean debugMode) throws DaoException, IOException, EntitySummaryException {
    long cpathId = interactionSummary.getRecordID();

    //  First, get parents of this interaction
    DaoInternalLink daoInternalLink = new DaoInternalLink();
    DaoCPath daoCPath = DaoCPath.getInstance();
    ArrayList sources = daoInternalLink.getSources(cpathId);
    ArrayList parentInteractionSummaries = new ArrayList();
    out.println("<UL>");
    for (int j=0; j<sources.size(); j++) {

        //  Only display parent interactions
        InternalLinkRecord internalLinkRecord = (InternalLinkRecord) sources.get(j);
        CPathRecord record = daoCPath.getRecordById(internalLinkRecord.getSourceId());
        if (record.getType() == CPathRecordType.INTERACTION) {
            EntitySummaryParser parser = new EntitySummaryParser
                    (internalLinkRecord.getSourceId());
            EntitySummary summary = parser.getEntitySummary();
            if (summary instanceof InteractionSummary) {
                String summaryStr = InteractionSummaryUtils.createInteractionSummaryStringTruncated
                    ((InteractionSummary) summary);
                out.println("<LI>");
                if (debugMode) {
                    out.println ("[<a href='record2.do?id=" + summary.getRecordID()
                        + "&debug=1'>" + summary.getRecordID() + "</a>]");
                }
                out.println (summaryStr + "</LI>");
                parentInteractionSummaries.add(summary);
            }
        }
    }
    out.println("</UL>");
    return parentInteractionSummaries;
}

private String getInspectorButtonHtml (long cPathId) {
    return "<td align=right><div class='toggle_details'><a "
        + "title='Toggle Record Details' onClick=\"toggleDetails('cpath_" + cPathId
        + "')\"><div id='cpath_" + cPathId + "_image' class='toggleImage'>"
        + "<img src='jsp/images/open.gif'/></div></a></td>";
}

private String getDetailsHtml (long cPathId, String label, String html) {
    return ("<div id='cpath_" + cPathId + "_" + label +"' class='details'>"
        + html + "</div>");
}

private String getStartRow (int i) {
    if (i%2 ==0) {
        return ("<tr valign=top>");
    } else {
        return("<tr valign=top bgcolor=#EEEEEE>");
    }
}

private String getBioPaxRecordHtml(BioPaxRecordSummary bpSummary,
        HashMap<String, Reference> referenceMap, int index) throws DaoException {
    StringBuffer buf = new StringBuffer();
    if (bpSummary.getCPathRecord() != null
        && bpSummary.getCPathRecord().getType() == CPathRecordType.PHYSICAL_ENTITY) {
            String entityLink = BioPaxRecordSummaryUtils.createEntityLink(bpSummary, 50);
            buf.append(entityLink);
    } else {
        buf.append ("<a href='record2.do?id=" + bpSummary.getRecordID() + "'>"
            + bpSummary.getName() + "</a>");
    }
    buf.append(getBioPaxDetailsHtml (bpSummary, referenceMap, index));
    return buf.toString();
}

private String getBioPaxDetailsHtml (BioPaxRecordSummary bpSummary,
        HashMap<String, Reference> referenceMap, int index) throws DaoException {
    ReferenceUtil refUtil = new ReferenceUtil();
    ArrayList masterList = refUtil.categorize(bpSummary);
    ArrayList<ExternalLinkRecord> referenceLinks =
            (ArrayList<ExternalLinkRecord>) masterList.get(0);

    StringBuffer buf = new StringBuffer();
    boolean hasDetails = false;
    if (bpSummary.getComments() != null
            || bpSummary.getOrganism() != null
            || referenceLinks.size() > 0) {
        hasDetails = true;
    }
    buf.append("<td>");
    if (bpSummary.getCPathRecord() != null) {
        CPathRecord record = bpSummary.getCPathRecord();
        if (record.getSnapshotId() > 0) {
            buf.append("<div class='data_source'> "
                + DbSnapshotInfo.getDbSnapshotHtmlAbbrev (record.getSnapshotId())
                + "</div>");
        }
    }
    buf.append("</td>");
    if (hasDetails) {
        buf.append(getInspectorButtonHtml(bpSummary.getRecordID()));
    } else {
        buf.append("<td></td>");
    }
    buf.append ("</tr>");
    buf.append(getStartRow(index));
    buf.append("<td colspan=3>");
    if (bpSummary.getComments() != null) {
        String comments[] = bpSummary.getComments();
        StringBuffer commentHtml = new StringBuffer();
        for (int i=0; i<comments.length; i++) {
            commentHtml.append("<p>" + ReactomeCommentUtil.massageComment(comments[i])
                + "</p>");
        }
        buf.append(getDetailsHtml(bpSummary.getRecordID(), "comment",
                commentHtml.toString()));
        hasDetails = true;
    } else {
        buf.append(getDetailsHtml(bpSummary.getRecordID(), "comment", ""));
    }
    String organism = bpSummary.getOrganism();
    if (organism != null) {
        buf.append(getDetailsHtml (bpSummary.getRecordID(), "organism",
                "<p><b>Organism:</b>&nbsp;" + organism));
        hasDetails = true;
    } else {
        buf.append(getDetailsHtml(bpSummary.getRecordID(), "organism", ""));
    }
    if (referenceLinks.size() > 0) {
        buf.append(getDetailsHtml (bpSummary.getRecordID(), "refs",
                refUtil.getReferenceHtml(referenceLinks, referenceMap)));
        hasDetails = true;
    } else {
        buf.append(getDetailsHtml(bpSummary.getRecordID(), "refs", ""));
    }
    buf.append("</td></tr>");
    return buf.toString();
}
%>
</table>
</div>
<jsp:include page="../global/redesign/xdebug.jsp" flush="true" />