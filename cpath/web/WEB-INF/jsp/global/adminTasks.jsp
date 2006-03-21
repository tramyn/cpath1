<%@ page import="org.mskcc.pathdb.servlet.CPathUIConfig"%>
<%
	// get admin mode active
	int adminModeActive = CPathUIConfig.getAdminModeActive();
%>
<div id="docs" class="toolgroup">
    <div class="label">
        <strong>Administration Tasks</strong>
    </div>

    <div class="body">
        <div>
            <A HREF="adminHome.do">Admin Home</A>
        </div>
        <div>
            <A HREF="adminRunFullTextIndexer.do">Run Full Text Indexer</A>
        </div>
        <div>
            <A HREF="adminWebLogging.do">Toggle Web Diagnostics</A>
        </div>                
        <div>
            <A HREF="adminResetCache.do">Purge In-Memory / Persistent Disk Cache</A>
        </div>
        <div>
            <A HREF="adminDiagnostics.do">Run cPath Diagnostics</A>
        </div>
<% if (adminModeActive == CPathUIConfig.ADMIN_MODE_ACTIVE){ %>
        <div>
            <A HREF="adminWebUIConfig.do">Configure User Interface</A>
        </div>
<% } %>
    </div>
    
    <div class="label">
        <strong>XML Cache</strong>
    </div>
    <div class="body">
        <div>
            <A HREF="adminViewXmlCache.do">View XML Cache</A>
        </div>
        <div>
            <A HREF="adminPurgeXmlCache.do">Purge XML Cache</A>
        </div>
    </div>    

    <div class="label">
        <strong>Error Log</strong>
    </div>
    <div class="body">
        <div>
            <A HREF="adminViewLogRecords.do">View Error Log</A>
        </div>
        <div>
            <A HREF="adminPurgeLogRecords.do">Purge Error Log</A>
        </div>
        <div>
            <A HREF="adminHome.do?testError=1">Test Error Page</A>
        </div>
    </div>
</div>