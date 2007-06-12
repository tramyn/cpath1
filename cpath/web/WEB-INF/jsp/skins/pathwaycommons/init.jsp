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
    webUIBean.setDefaultUserMessage("Pathway Commons serves the scientific community.  "
        + "We welcome your <a href='get_feedback.do'>feedback</a>.");
    webUIBean.setBaseURL("pathwaycommons.org");
    webUIBean.setSmtpHost("cbio.mskcc.org");
    webUIBean.setFeedbackEmailTo("pc-info@pathwaycommons.org");
    webUIBean.setWebApiVersion(ProtocolConstantsVersion2.VERSION_2);
    webUIBean.addSupportedInputIdType(ExternalDatabaseConstants.UNIPROT);
    webUIBean.addSupportedInputIdType(ExternalDatabaseConstants.INTERNAL_DATABASE);
    CPathUIConfig.setWebUIBean(webUIBean);
%>
