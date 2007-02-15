<%@ page import="org.mskcc.pathdb.action.BaseAction"%>
<%@ page import="org.mskcc.pathdb.model.GlobalFilterSettings"%>
<%@ page import="org.mskcc.pathdb.protocol.ProtocolRequest"%>
<%@ page import="org.mskcc.pathdb.sql.dao.DaoExternalDbSnapshot"%>
<%@ page import="org.mskcc.pathdb.model.ExternalDatabaseSnapshotRecord"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.TreeMap"%>
<%
    final String DATA_SOURCE_FILTER_VALUE_GLOBAL_LABEL = "All";
    // grab required objects from request object
    ProtocolRequest protocolRequest = (ProtocolRequest)
            request.getAttribute(BaseAction.ATTRIBUTE_PROTOCOL_REQUEST);
    Map<String, Integer> hitByDataSourceMap =
        (Map<String, Integer>)request.getAttribute(BaseAction.ATTRIBUTE_HITS_BY_DATA_SOURCE_MAP);
    String keyType = (String)request.getParameter(GlobalFilterSettings.NARROW_BY_ENTITY_TYPES_FILTER_NAME);
    String keyDataSource = (String)request.getParameter(GlobalFilterSettings.NARROW_BY_DATA_SOURCES_FILTER_NAME);

    // we have list of snapshot id's from request object, lets get the name to go with the id's
    DaoExternalDbSnapshot dao = new DaoExternalDbSnapshot();
    Map <String, String> dataSourceNameMap = new HashMap<String, String>();
    for (String key : hitByDataSourceMap.keySet()) {
	    if (key.equals(GlobalFilterSettings.NARROW_BY_DATA_SOURCES_FILTER_VALUE_GLOBAL)) {
		    dataSourceNameMap.put(DATA_SOURCE_FILTER_VALUE_GLOBAL_LABEL, key);
			if (keyDataSource.equals(GlobalFilterSettings.NARROW_BY_DATA_SOURCES_FILTER_VALUE_GLOBAL)) {
			    keyDataSource = DATA_SOURCE_FILTER_VALUE_GLOBAL_LABEL;
            }
        }
		else {
		    ExternalDatabaseSnapshotRecord snapShotRecord = dao.getDatabaseSnapshot(Long.valueOf(key));
			String snapShotRecordName = snapShotRecord.getExternalDatabase().getName();
			dataSourceNameMap.put(snapShotRecordName, key);
			if (keyDataSource.equals(key)) {
                keyDataSource = snapShotRecordName;
            }
        }
    }
    Map sortedDataSourceNameMap = new TreeMap(dataSourceNameMap);
%>
<%
    out.println("<h3>Narrow Results by Data Source:</h3>");
    out.println("<ul>");
    for (String dataSource : (Set<String>)sortedDataSourceNameMap.keySet()) {
	    String label = (dataSource.equals(DATA_SOURCE_FILTER_VALUE_GLOBAL_LABEL)) ?
            "Current Filter Settings" : dataSource;
			label += " (" + hitByDataSourceMap.get(dataSourceNameMap.get(dataSource)) + ")";
        if (keyDataSource.equals(dataSource)) {
		    out.println("<li>" + label + "<img src=\"jsp/images/spacer.gif\" width=\"10\"><img src=\"jsp/images/arrow.gif\"></li>");
	    }
	    else {
			if (hitByDataSourceMap.get(dataSourceNameMap.get(dataSource)) > 0) {
	            out.println("<li>" +
			                "<a href='webservice2.do?version=1.0&q=" + protocolRequest.getQuery() +
			                "&format=html&cmd=get_by_keyword&" +
			                GlobalFilterSettings.NARROW_BY_ENTITY_TYPES_FILTER_NAME + "=" + keyType + "&" +
		   				    GlobalFilterSettings.NARROW_BY_DATA_SOURCES_FILTER_NAME + "=" + dataSourceNameMap.get(dataSource) +
				            "'>" + label +
				            "</a>" +
				            "</li>");
            }
			else {
			    out.println("<li>" + label + "</li>");
            }
	    }
    }
	out.println("</ul>");
%>