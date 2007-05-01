<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><title>Cytoscape Pathway Commons Plugin: READ ME</title></head>

<body style="font-family: Arial,Helvetica,sans-serif; font-size: 11pt;">
<h3>1. Introduction</h3>
The Pathway Commons plugin enables Cytoscape users to visualize pathways and network neighborhood maps provided by Pathway Commons.  In this context pathways include biochemical reactions, complex assembly, transport and catalysis events, and physical interactions involving proteins, DNA, RNA, small molecules and complexes.  In addition network neighborhood maps are networks which include those proteins and interactions that involve the protein or small molecule referred to on the protein or search result page in which the network neighborhood map link resides.
<p>
Updates regarding this plugin will be posted to the Cytoscape-announce mailing list. You can easily <a href="http://groups-beta.google.com/group/cytoscape-announce/subscribe">subscribe</a> to the mailing list or <a href="http://groups-beta.google.com/group/cytoscape-announce">browse the archives</a>. 
</p>
<h3>2. Using the Pathway Commons Plugin</h3> 
<ul>
	<li>
	    Load a pathway by clicking on the "View this pathway in Cytoscape" link found on both the Search Result page and Pathway page.
	    Below is a sample screenshot of the Pathway page which includes the "View this pathway in Cytoscape" link on the lefthand panel:
	    <p>
	    <a href="jsp/images/plugin/pc_pathway.png"><img alt="Sample Screenshot of the Pathway page" src="jsp/images/plugin/pc_pathway_thumb.png" border="0"></a> 
	    <br>
	    <font size="-1">[<a href="jsp/images/plugin/pc_pathway.png">Click to enlarge</a>]</font> 
	    </p>
	</li>
		<li>
	    Load a network neighborhood map by clicking on the "View network neighborhood map in Cytoscape" link found on both the Search Result page and Physical Entity page.
	    Below is a sample screenshot of the Search Results page which includes the "View network neighborhood map in Cytoscape" link under each search result:
	    <p>
	    <a href="jsp/images/plugin/pc_search_results.png"><img alt="Sample Screenshot of the Search Results page" src="jsp/images/plugin/pc_search_results_thumb.png" border="0"></a> 
	    <br>
	    <font size="-1">[<a href="jsp/images/plugin/pc_search_results.png">Click to enlarge</a>]</font> 
	    </p>
	</li>
		</li>
		<li>
	    After clicking on one of the previously described links, Cytoscape might display the a merge dialog window.  This occurs if there is one or more networks loaded in Cytoscape that can be merged with the pathway or network neighborhood map about to be loaded in Cytoscape.  The dialog will give you the option to merge the pathway or network neighborhood map with one of the networks within Cytoscape or create a new network.
	    Below is a sample screenshot of the merge dialog window displayed within Cytoscape:
	    <p>
	    <a href="jsp/images/plugin/pc_plugin_merge.png"><img alt="Sample Screenshot of the Pathway Commons Plugin Merge Dialog Window" src="jsp/images/plugin/pc_plugin_merge_thumb.png" border="0"></a> 
	    <br>
	    <font size="-1">[<a href="jsp/images/plugin/pc_plugin_merge.png">Click to enlarge</a>]</font> 
	    </p>
	</li>
</ul>

<h3>3. Understanding Visual Rendering Clues</h3>By default, nodes and edges are rendered as follows: 
<p>
</p>
<table cellpadding="3" cellspacing="3">
	<tbody><tr bgcolor="#dddddd">
		<td>
			Entity 
		</td>
		<td>
			Visual Shape 
		</td>
	</tr>
	<tr>
		<td>
			Proteins, small molecules, complexes and other physical entities (as defined in the <a href="http://biopax.org">BioPAX pathway format</a>).
		</td>
		<td>
			<img alt="Ellipse Shape" src="jsp/images/plugin/ellipse.jpg"> 
		</td>
	</tr>
	<tr>
		<td>
			Conversions, biochemical reactions, modulations and other interaction entities (as defined in the <a href="http://biopax.org">BioPAX pathway format</a>).
		</td>
		<td>
			<img alt="Square Shape" src="jsp/images/plugin/square.jpg"> 
		</td>
	</tr>
	<tr bgcolor="#dddddd">
		<td>
			Interaction Type 
		</td>
		<td>
			Arrow Shape 
		</td>
	</tr>
	<tr>
		<td>
			Inhibition 
		</td>
		<td>
			<img alt="Black inhibition arrow type" src="jsp/images/plugin/BLACK_T.jpg"> 
		</td>
	</tr>
	<tr>
		<td>
			Containment, e.g. a complex can contain one or more proteins. 
		</td>
		<td>
			<img alt="Black circle arrow type" src="jsp/images/plugin/BLACK_CIRCLE.jpg"> 
		</td>
	</tr>
	<tr>
		<td>
			All other interaction types, e.g. activation, left, right, etc. 
		</td>
		<td>
			<img alt="Black arrow type" src="jsp/images/plugin/BLACK_DELTA.jpg"> 
		</td>
	</tr>
	<tr bgcolor="#DDDDDD">
		<td VALIGN=TOP COLSPAN="2">
			<FONT FACE="ARIAL">Labels for Physical Entities</FONT>
		</td>
	</tr>
	<tr>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">All physical entities will be labeled as follows:</FONT>
		</td>
	</tr>
	<tr>
		<td VALIGN=TOP NOWRAP>
			<FONT FACE="ARIAL" SIZE="2">[NAME] [-CHEMICAL_MODIFICATION_ABBR] [(CELULAR_LOCATION_ABBR)]</FONT>
		</td>
	</tr>
	<tr>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">where [NAME] is determined by the following order of precedence: NAME, SHORT NAME, or Shortest Synonym,</FONT>
		</td>
	</tr>
	<tr>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">and [CHEMICAL_MODIFICATION_ABBR] and [CELLULAR_LOCATION_ABBR] are one of the abbreviations described below.</FONT>
		</td>
	</tr>
	<tr bgcolor="#DDDDDD">
		<td VALIGN=TOP COLSPAN="2">
			<FONT FACE="ARIAL">Example:</FONT>
		</td>
	</tr>
	<tr>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">MITF:</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">MITF</FONT>
		</td>
	</tr>
	<tr>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">MITF Phosphorylated:</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">MITF-P</FONT>
		</td>
	</tr>
	<tr>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">MITF Phosphorylated, in the cytoplasm:</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">MITF-P (CP)</FONT>
		</td>
	</tr>
	<tr bgcolor="#DDDDDD">
		<td VALIGN=TOP COLSPAN="2">
			<FONT FACE="ARIAL">Abbreviations for Chemical Modifications</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">acetylation site</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">A</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">glycosylation site</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">G</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">phosphorylation site site</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">P</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">proteolytic cleavage site</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">PCS</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">sumoylation site</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">S</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">ubiquitination site</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">U</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">[All others]</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">No Abbreviation will be provided.</FONT>
		</td>
	</tr>
	<tr bgcolor="#DDDDDD">
		<td VALIGN=TOP COLSPAN="2">
			<FONT FACE="ARIAL">Abbreviations for Cellular Locations</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">cellular component unknown</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">No Abbreviation will be provided.</FONT>
	</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">centrosome</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">CE</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">cytoplasm</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">CY</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">endoplasmic reticulum</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">ER</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">endosome</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">EN</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">extracellular</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">EM</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">golgi apparatus</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">GA</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">mitochondrion</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">MI</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">nucleus</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">NU</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">plasma membrane</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">PM</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">ribosome</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">RI</FONT>
		</td>
	</tr>
	<tr>
	    <td VALIGN=TOP>
		    <FONT FACE="ARIAL">[All others]</FONT>
		</td>
		<td VALIGN=TOP>
			<FONT FACE="ARIAL">No Abbreviation will be provided.</FONT>
		</td>
	</tr>
</tbody></table>
<h3>4. Bugs / Feature Requests</h3> If you encounter a bug with this plugin, email Benjamin Gross or Ethan Cerami at the address(es) below, or log a bug directly to the <a href="http://www.cbio.mskcc.org/cytoscape/bugs/">Cytoscape Bug Tracker</a>. <h3>5. Contacts</h3> 
<p>
	Sander Group, <a href="http://www.cbio.mskcc.org/">Computational Biology Center</a> 
	<br>
	<a href="http://www.mskcc.org/">Memorial Sloan-Kettering Cancer Center</a>, New York City 
	<br>
</p>
<p>
	For any questions concerning this plugin, please contact: 
</p>
<p>
<IMG SRC="jsp/images/emailimage.jpg" BORDER=0>
</p>
<p>
	This software is made available under the <a href="http://www.gnu.org/licenses/lgpl.html">LGPL (Lesser General Public License)</a>. 
</p>
<h3>6. Release Notes / Current Limitations</h3> 
<p>
	Version: 0.1 
</p>
<ul>
	<li>
		Date: March 15, 2006 
	</li>
	<li>
		Features: 
		<ul>
			<li>
				Enables Cytoscape users to visualize expression data in the context of Biological Pathways.
			</li>
			<li>
				Works with Cytoscape 2.2. 
			</li>
			<li>
				Supports all Human Affymetrix Chipsets.
			</li>
		</ul>
	</li>
</ul>
<p>
	Version: 0.2
</p>
<ul>
	<li>
		Date: June 8, 2006 
	</li>
	<li>
		Features: 
		<ul>
			<li>
				Includes Simplified Visual Language to describe Biological Pathways in BioPAX format.
			</li>
			<li>
				Works with Cytoscape 2.3. 
			</li>
		</ul>
	</li>
</ul>
Known Limitations / Known Bugs: 
<ul>
	<li>
	    It is possible (in fact, probable), that there will be multiple expression data values per network node.  Which value gets used?  The Expression Viewer will map over the value with the largest absolute value.
	</li>
	<li>
	    The Expression Viewer makes no distinction between expression data in real or log values.
	</li>
	<li>
	    Only one set of expression data can be loaded at a time.  When loaded, the data will be mapped to all Pathways.
	</li>
</ul>