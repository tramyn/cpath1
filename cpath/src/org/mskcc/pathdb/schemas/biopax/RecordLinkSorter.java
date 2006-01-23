/** Copyright (c) 2005 Memorial Sloan-Kettering Cancer Center.
 **
 ** Code written by: Benjamin Gross
 ** Authors: Ethan Cerami, Benjamin Gross, Gary Bader, Chris Sander
 **
 ** This library is free software; you can redistribute it and/or modify it
 ** under the terms of the GNU Lesser General Public License as published
 ** by the Free Software Foundation; either version 2.1 of the License, or
 ** any later version.
 **
 ** This library is distributed in the hope that it will be useful, but
 ** WITHOUT ANY WARRANTY, WITHOUT EVEN THE IMPLIED WARRANTY OF
 ** MERCHANTABILITY OR FITNESS FOR A PARTICULAR PURPOSE.  The software and
 ** documentation provided hereunder is on an "as is" basis, and
 ** Memorial Sloan-Kettering Cancer Center 
 ** has no obligations to provide maintenance, support,
 ** updates, enhancements or modifications.  In no event shall
 ** Memorial Sloan-Kettering Cancer Center
 ** be liable to any party for direct, indirect, special,
 ** incidental or consequential damages, including lost profits, arising
 ** out of the use of this software and its documentation, even if
 ** Memorial Sloan-Kettering Cancer Center 
 ** has been advised of the possibility of such damage.  See
 ** the GNU Lesser General Public License for more details.
 **
 ** You should have received a copy of the GNU Lesser General Public License
 ** along with this library; if not, write to the Free Software Foundation,
 ** Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.
 **/

// package
package org.mskcc.pathdb.schemas.biopax;

// imports 
import java.util.Comparator; 

/**
 * This class is used to sort molecules/pathway link lists.
 * Only works with strings in the following format:
 * <a href="record.do?id=52">LinkName</a>
 * We sort on the LinkName.
 *
 * @author Benjamin Gross.
 */
public class RecordLinkSorter implements Comparator {

	/**
	 * Our implementation of compare.
	 */
	public int compare(Object o1, Object o2){

		// only work with strings
		if (o1 instanceof String && o2 instanceof String){
			String s1 = (String)o1;
			String s2 = (String)o2;

			// get the strings to compare
			String sub1, sub2;
			try {
				sub1 = s1.substring(s1.indexOf('>')+1, s1.lastIndexOf('<'));
				sub2 = s2.substring(s2.indexOf('>')+1, s2.lastIndexOf('<'));
			}
			catch(Exception e){
				throw new IllegalArgumentException("Invalid arguments to RecordLinkSorter.compare()");
			}

			// return string compare
			return sub1.compareToIgnoreCase(sub2);
		}
		
		// made it here, we have invalid args
		throw new IllegalArgumentException("Invalid arguments to RecordLinkSorter.compare()");
	}

}