<%@ page import="org.mskcc.pathdb.action.BaseAction,
                 org.mskcc.pathdb.form.WebUIBean,
                 org.mskcc.pathdb.servlet.CPathUIConfig"%>
<%
	// set title
	WebUIBean webUIBean = CPathUIConfig.getWebUIBean();
	String title = webUIBean.getApplicationName() + "::Legal Disclaimer / Privacy Notice";
	request.setAttribute(BaseAction.ATTRIBUTE_TITLE, title);
%>

<jsp:include page="../global/header.jsp" flush="true" />

<div id="apphead">
    <h2>Legal Disclaimer / Privacy Notice</h2>
</div>

<div class="h3">
    <h3>Legal Disclaimer</h3>
</div>

<P>
The information in Memorial Sloan-Kettering Cancer Center's website is not
intended as a substitute for medical professional help or advice but is to
be used only as an aid in understanding current medical knowledge. A physician
should always be consulted for any health problem or medical condition.
Inquiries about the contents of our website should be addressed to the contact
information located herein. Our website provides links to other organizations
as a service to our users; Memorial Sloan-Kettering Cancer Center is not
responsible for the information provided in other websites.

<div class="h3">
    <h3>Privacy Notice</h3>
</div>
<P>
Memorial Sloan-Kettering Cancer Center collects certain forms of information
through standard Web logs for the purpose of site management. Of the
information we learn about you from your visit to our websites we store only
the following: the IP address from which you access the Internet, the date,
your web browser version, terms entered into our search engine, and the
Internet address of the web site that referred you to our site. This
information is used to measure the number of visitors to the various
sections of our site and to help us make our site more useful. Unless it is
specifically stated otherwise, no additional information will be collected
about you. Except for authorized law enforcement investigations, no attempt
is made to identify individual users or usage patterns. We do not give, share,
sell or transfer any personal information to a third party.
<P>
When inquiries are emailed to us, we store the question and the email address
information so that we may respond electronically. Unless otherwise required
by statute, we do not identify publicly who sends questions or comments to
our web site. We will not obtain information that will allow us to personally
identify you when you visit our site, unless you chose to provide such
information to us. We do not forward your mail nor do we collect your name
and e-mail address for any purpose other than to respond to your query.
<P>
Email requesting software support may be seen by a number of people who are
responsible for answering questions. Be aware that email is not secure against
interception. Therefore, if your communication contains sensitive or personal
information, you should send it by postal mail.

<jsp:include page="../global/footer.jsp" flush="true" />