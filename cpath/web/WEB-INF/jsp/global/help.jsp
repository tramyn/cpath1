<%@ page import="org.mskcc.pathdb.protocol.ProtocolStatusCode,
                 java.util.ArrayList,
                 org.mskcc.pathdb.protocol.ProtocolConstants,
                 org.mskcc.pathdb.protocol.ProtocolRequest,
                 org.mskcc.pathdb.form.WebUIBean,
                 org.mskcc.pathdb.servlet.CPathUIConfig"%>

<%
	// get WebUIBean
	WebUIBean webUIBean = CPathUIConfig.getWebUIBean();

	// if we are in psi mode print the following content
    if (CPathUIConfig.getWebMode() == CPathUIConfig.WEB_MODE_PSI_MI){
        out.println("<div id=\"apphead\">");
        out.println("    <h2>Web Service API</h2>");
        out.println("</div>");
        out.println("");
        out.println("<div class=\"h3\">");
        out.println("    <h3>Introduction</h3>");
        out.println("</div>");
        out.println("This page provides a quick reference help guide to using");
        out.println("the cPath Web Service API.  The cPath Web Service provides programmatic");
        out.println("access to all data in cPath.  A client application requests data via a defined");
        out.println("set of URL parameters, and can receive data in multiple formats, including");
        out.println("XML and HTML.  The XML format is useful for those applications that want to");
        out.println("programmatically access cPath data for futher computation, whereas the HTML");
        out.println("format is useful for those web sites and/or applications that want to provide");
        out.println("link outs to cPath data.");
        out.println("<div class=\"h3\">");
        out.println("    <h3>Issuing Client Requests</h3>");
        out.println("</div>");
        out.println("Client requests to the cPath Web Service are formed by specifying");
        out.println("URL parameters.  Parameters are as follows:");
        out.println("        <UL>");
        out.println("		    <LI><B>" + ProtocolRequest.ARG_COMMAND + "</B>:  Indicates the command to execute.");
        out.println("            Current valid commands are defined in the Commands section below.");
        out.println("		    <LI><B>q</B>:  Indicates the query parameter.  Depending on the command,");
        out.println("            this is used to indicate one or more search terms or a unique ID.");
        out.println("            For example, \"dna repair\" or \"P09097\".");
        out.println(" 		    <LI><B>" + ProtocolRequest.ARG_FORMAT + "</B>:  Indicates the format of returned results.");
        out.println("            Current valid formats are as follows:");
        out.println("                <UL>");
        out.println("                <LI>" + ProtocolConstants.FORMAT_PSI_MI + ":  Data will be");
        out.println("                formatted in the");
        out.println("                <A HREF=\"http://psidev.sourceforge.net/\">Proteomics");
        out.println("                Standards Intitiative Molecular Interaction (PSI-MI)</A>");
        out.println("                XML format.");
        out.println("                <LI>" + ProtocolConstants.FORMAT_BIO_PAX + ":  Data will be");
        out.println("                formatted in the <A HREF=\"http://www.biopax.org\">BioPAX</A>");
        out.println("                XML format.");
        out.println("                <LI>" + ProtocolConstants.FORMAT_HTML + ":  Data will be");
        out.println("                formatted in HTML, using the regular cPath Look and Feel.");
        out.println("                This is useful for creating link outs to cPath data.");
        out.println("                <LI>" + ProtocolConstants.FORMAT_COUNT_ONLY + ":  Returns a");
        out.println("                single integer value, representing the total number of matches");
        out.println("                for your query.  This is useful for using paged scroll results");
        out.println("                (see <A HREF=\"#large\">Retrieving Large Sets of Data</A> below).");
        out.println("                </UL>");
        out.println("    		    <LI><B>" + ProtocolRequest.ARG_VERSION + "</B>:  Indicates the");
        out.println("                version of the web service API.");
        out.println("                Must be specified.  The only supported version is \"1.0\".");
        out.println("                <LI><B>" + ProtocolRequest.ARG_ORGANISM + "</B>:  an optional");
        out.println("                parameter used to filter for");
        out.println("                specific organisms.  The value of this parameter must be set");
        out.println("                to an <A HREF=\"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Taxonomy\">");
        out.println("                NCBI Taxonomy Identifier</A>.   For example, organism=9606 will");
        out.println("                filter for Homo Sapiens.");
        out.println("                <LI><B>" + ProtocolRequest.ARG_MAX_HITS + "</B>:");
        out.println("                Indicates the maximum number of interactions");
        out.println("                returned in the response.  If maxHits is not specified, it will");
        out.println("                default to:");
        out.println("                " + ProtocolConstants.DEFAULT_MAX_HITS + ".  To prevent overloading");
        out.println("                of the system, clients are restricted to a maximum of");
        out.println("                " + ProtocolConstants.MAX_NUM_HITS + " hits at a time.");
        out.println("                However, it is still possible to retrieve larger sets of data by");
        out.println("                using paged scroll results (see");
        out.println("                <A HREF=\"#large\">Retrieving Large Sets of Data</A> below).");
        out.println("                <LI><B>" + ProtocolRequest.ARG_START_INDEX + "</B>:  Indicates");
        out.println("                the start index to use in a set of paged results.  For full");
        out.println("                details, refer to <A HREF=\"#large\">Retrieving Large Sets of Data</A>");
        out.println("                below.).");
        out.println("                </LI>");
        out.println("	    </UL>");
        out.println("");
        out.println("<div class=\"h3\">");
        out.println("    <h3>Commands </h3>");
        out.println("</div>");
        out.println("        <TABLE>");
        out.println("            <tr>");
        out.println("                <th>Command</font></th>");
        out.println("                <th>Description</font></th>");
        out.println("                <th>XML Formats Supported</th>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_HELP + "</td>");
        out.println("                <td>Requests the current help page that you are now reading.</td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_GET_BY_KEYWORD  + "</td>");
        out.println("                <td>Finds all pathways and interactions in cPath that contain");
        out.println("                the specified keyword(s) and / or boolean search phrases.");
        out.println("                This is the most powerful search command, and can be used to");
        out.println("                perform advanced queries across multiple fields in cPath.");
        out.println("                For full details regarding keyword searches, refer to the");
        out.println("                <A HREF=\"faq.do#construct\">advanced search section of the");
        out.println("                cPath FAQ</A>.</td>");
        out.println("                <td>" + ProtocolConstants.FORMAT_BIO_PAX + ",");
        out.println("                    " + ProtocolConstants.FORMAT_PSI_MI + "");
        out.println("                </td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_GET_BY_INTERACTOR_NAME_XREF + "</td>");
        out.println("                <td>Finds all interactions in cPath that reference the specified");
        out.println("                interactor.  Interactors can be referenced by name, description");
        out.println("                or external database reference.  For example, if you want to");
        out.println("                narrow your search to all intereractions associated with a");
        out.println("                specific SWISS-PROT ID, use this command.");
        out.println("                </td>");
        out.println("                <td>");
        out.println("                    " + ProtocolConstants.FORMAT_PSI_MI + " only.");
        out.println("                </td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_GET_BY_ORGANISM + "</td>");
        out.println("                <td>Finds all interactions in cPath for the specified organism.");
        out.println("                The organism value must be specified with an <A HREF=");
        out.println("                \"http://www.ncbi.nlm.nih.gov/entrez/query.fcgi?db=Taxonomy\">NCBI");
        out.println("                Taxonomy ID</A>.  Note that you can also attach");
        out.println("                an organism filter to any search command by using the");
        out.println("                optional organism parameter (see URL parameters above).");
        out.println("                </td>");
        out.println("                <td>");
        out.println("                    " + ProtocolConstants.FORMAT_PSI_MI + " only.");
        out.println("                </td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_GET_BY_EXPERIMENT_TYPE + "</td>");
        out.println("                <td>");
        out.println("                Finds all interactions in cPath that were discovered by");
        out.println("                the specified Experiment Type.  For example, if you want to");
        out.println("                narrow your search to all interactions discovered via");
        out.println("                \"Classical Two Hybrid\", use this command.");
        out.println("                </td>");
        out.println("                <td>");
        out.println("                    " + ProtocolConstants.FORMAT_PSI_MI + " only.");
        out.println("                </td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_GET_BY_PMID + "</td>");
        out.println("                <td>Finds all interactions in cPath that are associated with");
        out.println("                the specified PubMed ID.</td>");
        out.println("                <td>");
        out.println("                    " + ProtocolConstants.FORMAT_PSI_MI + " only.");
        out.println("                </td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_GET_BY_DATABASE + "</td>");
        out.println("                <td>Finds all interactions in cPath that come from the specified");
        out.println("                database.  For example, to find all interactions from \"DIP\"");
        out.println("                (Database of Interacting Proteins), use this command.</td>");
        out.println("                <td>");
        out.println("                    " + ProtocolConstants.FORMAT_PSI_MI + " only.");
        out.println("                </td>");
        out.println("            </tr>");
        out.println("            <tr>");
        out.println("                <td>" + ProtocolConstants.COMMAND_GET_BY_INTERACTOR_ID + "</td>");
        out.println("                <td>Finds all interactions in cPath that are associated with");
        out.println("                the specified interactor.  To use this option, you must know the");
        out.println("                internal cPath ID for the interactor.</td>");
        out.println("                <td>");
        out.println("                    " + ProtocolConstants.FORMAT_PSI_MI + " only.");
        out.println("                </td>");
        out.println("            </tr>");
        out.println("            </TABLE>");
        out.println("");
        out.println("<div class=\"h3\">");
        out.println("    <h3>Error Codes</h3>");
        out.println("</div>");
        out.println("If an error occurs while processing your request, you will");
        out.println("receive an XML document with detailed information about the cause of");
        out.println("the error.  Error documents have the following format:");
        out.println("");
        out.println("<PRE>");
        out.println("&lt;error&gt;");
        out.println("    &lt;error_code&gt;[ERROR_CODE]&lt;/error_code&gt;");
        out.println("    &lt;error_msg&gt;[ERROR_DESCRIPTION]&lt;/error_msg&gt;");
        out.println("    &lt;error_details&gt;[ADDITIONAL_ERROR _DETAILS]&lt;/error_details&gt;");
        out.println("&lt;/error&gt;");
        out.println("</PRE>");
        out.println("");
        out.println("        The Table below provides a list of error codes, with their");
        out.println("        descriptions.");
        out.println("        <P>");
        out.println("        <TABLE>");
        out.println("            <tr>");
        out.println("                <th>Error Code</font></th>");
        out.println("                <th>Error Description</font></th>");
        out.println("            </TR>");

                ArrayList statusCodes = ProtocolStatusCode.getAllStatusCodes();


                for (int i=0; i<statusCodes.size(); i++) {
                    ProtocolStatusCode code =
                            (ProtocolStatusCode) statusCodes.get(i);
                    int errorCode = code.getErrorCode();
                    String errorMsg = code.getErrorMsg();

        out.println("                <TR>");
        out.println("                    <TD>" + errorCode + "</TD>");
        out.println("                    <TD>" + errorMsg + "</TD>");
        out.println("                </TR>");
                }
        out.println("        </TABLE>");


    ProtocolRequest pRequest = new ProtocolRequest();
    pRequest.setCommand(ProtocolConstants.COMMAND_GET_BY_KEYWORD);
    pRequest.setQuery("DNA");
    pRequest.setFormat(ProtocolConstants.FORMAT_PSI_MI);

        out.println("<div class=\"h3\">");
        out.println("    <h3>Examples of Usage</h3>");
        out.println("</div>");
        out.println("		The following query searches cPath for the keyword \"DNA\".");
        out.println("        Data will be formatted in the PSI-MI XML format.");
        out.println("        <UL>");
        out.println("            <LI><SMALL><A HREF=\"" + pRequest.getUri() + "\">" + pRequest.getUri() + "</A>");
        out.println("            </SMALL>");
        out.println("        </UL>");
        out.println("");
        out.println("		The following query searches cPath for the keyword \"DNA\".");
        out.println("        Data will be formatted in HTML.");
                     pRequest.setFormat(ProtocolConstants.FORMAT_HTML);
        out.println("        <UL>");
        out.println("            <LI><SMALL><A HREF=\"" + pRequest.getUri() + "\">" + pRequest.getUri() + "</A>");
        out.println("            </SMALL>");
        out.println("        </UL>");
        out.println("");
                     pRequest.setVersion(\"0.9\");
                     pRequest.setFormat(ProtocolConstants.FORMAT_PSI_MI);
        out.println("");
        out.println("        The following query is invalid.");
        out.println("        The web service will return an XML document with a specific error code");
        out.println("        and error message.");
        out.println("        <UL>");
        out.println("            <LI><SMALL><A HREF=\"" + pRequest.getUri() + "\">" + pRequest.getUri() + "</A>");
        out.println("            </SMALL>");
        out.println("        </UL>");
        out.println("<div class=\"h3\">");
        out.println("    <h3><A NAME=\"large\">Retrieving Large Sets of Data</A></h3>");
        out.println("</div>");
        out.println("    To prevent overloading of the system, clients are restricted to a maximum of");
        out.println("    " + ProtocolConstants.MAX_NUM_HITS + " hits at a time.");
        out.println("    However, it is still possible to retrieve larger sets of data");
        out.println("    (or even complete sets of data) by using an index value into a");
        out.println("    complete data set.  This functionality is identical to that provided");
        out.println("    by the cPath web site.  For example, if you want to view all interactions");
        out.println("    in cPath, you can do so, but you will have to manually scroll");
        out.println("    through the results one page at a time.  To retrieve complete data sets");
        out.println("    via the Web Service API, you follow the same procedure and");
        out.println("    retrieve results one \"page\" at a time.  This requires multiple client");
        out.println("    requests to cPath, and some more intelligent client processing.");
        out.println("");
        out.println("    pRequest = new ProtocolRequest();");
        out.println("    pRequest.setCommand(ProtocolConstants.COMMAND_GET_BY_ORGANISM);");
        out.println("    pRequest.setQuery(\"562\");");
        out.println("    pRequest.setFormat(ProtocolConstants.FORMAT_COUNT_ONLY);");
        out.println("");
        out.println("    <P>For example, assume a client wishes to download the full set");
        out.println("    of interactions for E. coli.  Here's how such client processing would work:");
        out.println("    <UL>");
        out.println("    <LI>First, find out how many interactions for E. coli exist.  To do so,");
        out.println("    issue a query with " + ProtocolRequest.ARG_FORMAT + "");
        out.println("    set to \"" + ProtocolConstants.FORMAT_COUNT_ONLY + "\".");
        out.println("    For example:");
        out.println("        <UL>");
        out.println("            <LI><SMALL><A HREF=\"" + pRequest.getUri() + "\">" + pRequest.getUri() + "</A>");
        out.println("            </SMALL>");
        out.println("        </UL>");
        out.println("    You will receive back a single integer value, indicating the total");
        out.println("    number of matching interactions.");
        out.println("    <LI>Next, create a while loop or a for loop for retrieving data");
        out.println("    sets in small bundles or \"pages\".  For example, if there are 1000");
        out.println("    interactions for E. coli, you could retrieve interactions in sets of 50.");
        out.println("    The client uses the " + ProtocolRequest.ARG_START_INDEX + " parameter to specify a");
        out.println("    starting point in the result set.  For example, if");
        out.println("    " + ProtocolRequest.ARG_START_INDEX + " is set to 100, and");
        out.println("    " + ProtocolRequest.ARG_MAX_HITS+ " is set to 50, you will retrieve interactions");
        out.println("    100-150 in the complete data set.");
        out.println("    </UL>");
        out.println("    <P>Complete psuedocode of the entire process looks like this:");
        out.println("    <PRE>");
        out.println("totalNumInteractions = [Issue search resuest with " + ProtocolRequest.ARG_FORMAT + " set to " + ProtocolConstants.FORMAT_COUNT_ONLY + ".]");
        out.println("index = 0;");
        out.println("while (index < totalNumInteractions) {");
        out.println("    [Issue request with " + ProtocolRequest.ARG_START_INDEX + " = index; and " + ProtocolRequest.ARG_MAX_HITS + " = 50.]");
        out.println("    index += 50;");
        out.println("}</PRE>");
        out.println("    After the while loop exits, you have a complete set of E. Coli data.");
        out.println("    </UL>");
        out.println("</div>");
        out.println("</BLOCKQUOTE>");
    }
	// we are in biopax mode, print the following content
	else{
        out.println("<div id=\"apphead\">");
        out.println("    <h2>Web Service API (BioPax)</h2>");
        out.println("</div>");
    }
%>