<FORM ACTION="dataservice"  METHOD="GET">
<INPUT TYPE="hidden" name="version" value="1.0">
<INPUT TYPE="hidden" name="cmd" value="retrieve_interactions">
<INPUT TYPE="hidden" name="db" value="grid">

<% String uid = request.getParameter("uid");
    if (uid == null) uid = new String("YCR038C");
%>

<TABLE WIDTH="100%" CELLPADDING=5 CELLSPACING=5 BGCOLOR="#9999cc">
    <TR>
        <TD class="table_data">UID:</TD>
        <TD><INPUT TYPE=TEXT name="uid" value="<%= uid %>"></TD>
        <TD class="table_data">Format:</TD>
        <TD>
            <SELECT NAME="format">
                <OPTION VALUE="html">HTML Format</OPTION>
                <OPTION VALUE="psi">PSI-MI XML Format</OPTION>
            </SELECT>
        </TD>
        <TD>&nbsp;</TD>
        <TD><INPUT TYPE="SUBMIT" value="Retrieve Data"></TD>
    </TR>
</TABLE>
</FORM>