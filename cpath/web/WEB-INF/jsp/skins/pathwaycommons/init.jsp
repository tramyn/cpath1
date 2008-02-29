<%@ page import="org.mskcc.pathdb.servlet.CPathUIConfig"%>
<%@ page import="org.mskcc.pathdb.form.WebUIBean"%>
<%@ page import="org.mskcc.pathdb.protocol.ProtocolConstantsVersion2"%>
<%@ page import="org.mskcc.pathdb.util.ExternalDatabaseConstants"%>
<%
    CPathUIConfig.setShowDataSourceDetails(true);
    CPathUIConfig.setWebMode(CPathUIConfig.WEB_MODE_BIOPAX);
    WebUIBean webUIBean = new WebUIBean();
    webUIBean.setApplicationName("Pathway Commons");
    webUIBean.setDisplayBrowseByOrganismTab(false);
    webUIBean.setDisplayBrowseByPathwayTab(false);
    webUIBean.setWantCytoscape(true);
    webUIBean.setDisplayWebServiceTab(true);
    webUIBean.setDisplayFilterTab(true);
    webUIBean.setDefaultUserMessage("<table><tr>" +
		"<td width=\"40%\" align=\"left\">Help us improve Pathway Commons.  Send us your <a href='get_feedback.do'>feedback</a>.</td>" +
		"<td width=\"60%\" align=\"right\"><form action=\"http://groups-beta.google.com/group/pathway-commons-announce/boxsubscribe\">" + 
		"Subscribe to pathway-commons-announce, email:&nbsp&nbsp<input type=text name=email>&nbsp&nbsp<input type=submit name=\"sub\" value=\"subscribe\">" +
		"</form></td>" +
		"</tr></table>");
    webUIBean.setBaseURL("pathwaycommons.org");
    webUIBean.setSmtpHost("cbio.mskcc.org");
    webUIBean.setFeedbackEmailTo("pc-info@pathwaycommons.org");
    webUIBean.setWebApiVersion(ProtocolConstantsVersion2.VERSION_2);
    webUIBean.addSupportedIdType(ExternalDatabaseConstants.UNIPROT);
    webUIBean.addSupportedIdType(ExternalDatabaseConstants.INTERNAL_DATABASE);
    webUIBean.setTagLine("Search and visualize public biological pathway information.  Single point of access. [<a href='faq.do'>more...</a>]");
    CPathUIConfig.setWebUIBean(webUIBean);
%>
