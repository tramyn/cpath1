// $Id: XmlValidator.java,v 1.6 2008-06-24 20:02:16 cerami Exp $
//------------------------------------------------------------------------------
/** Copyright (c) 2006 Memorial Sloan-Kettering Cancer Center.
 **
 ** Code written by: Ethan Cerami
 ** Authors: Ethan Cerami, Gary Bader, Chris Sander
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
package org.mskcc.pathdb.util.xml;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * XML Validator Utility.
 *
 * @author Ethan Cerami
 */
public class XmlValidator extends DefaultHandler {

    // Default parser name
    protected static final String DEFAULT_PARSER_NAME =
            "org.apache.xerces.parsers.SAXParser";

    // Validation feature id
    protected static final String VALIDATION_FEATURE_ID =
            "http://xml.org/sax/features/validation";

    //  Schema validation feature id.
    protected static final String SCHEMA_VALIDATION_FEATURE_ID =
            "http://apache.org/xml/features/validation/schema";

    // Dynamic validation feature id
    protected static final String DYNAMIC_VALIDATION_FEATURE_ID
            = "http://apache.org/xml/features/validation/dynamic";

    protected static final String EXTERNAL_SCHEMA_LOCATION
            = "http://apache.org/xml/properties/schema/external-schemaLocation";

    private ArrayList errorList;
    private boolean validateAgainstSchema;

    /**
     * Validates Specified Document.
     *
     * @param xml XML Document (String Representation).
     * @return ArrayList of SAXExceptions (if any).
     * @throws SAXException Error Parsing Document.
     * @throws IOException  Error Reading Document.
     */
    public ArrayList validate(String xml, boolean validateAgainstSchema)
            throws SAXException, IOException {
        return execute(xml, validateAgainstSchema, null);
    }

    /**
     * Validates Specified Document, using designated schema location.
     *
     * @param xml            XML Document (String Representation).
     * @param schemaLocation Schema Location.
     * @return ArrayList of SAXExceptions (if any).
     * @throws SAXException Error Parsing Document.
     * @throws IOException  Error Reading Document.
     */
    public ArrayList validate(String xml, String schemaLocation)
            throws SAXException, IOException {
        return execute(xml, true, schemaLocation);
    }

    /**
     * Validates the Specified Document against PSI-MI Level 1.
     * <p/>
     * Unfortunately, there is no strict rule for how PSI-MI Level 1 documents reference
     * the external schema.  Some use absolute URLs to the schema;  others use relative URLs
     * to local files (which do not actually exist).  To get around this very common problem,
     * this method explicitly validates the XML document against the local MIF.xsd in cPath.
     *
     * @param xml XML Document.
     * @return ArrayList of SAXExceptions (if any).
     * @throws SAXException XML SAX Error.
     * @throws IOException File I/O Error.
     */
    public ArrayList validatePsiMiLevel1(String xml) throws SAXException, IOException {
        String cpathHome = System.getProperty("CPATH_HOME");
        String separator = System.getProperty("file.separator");
        String psiMiLevel1 = "net:sf:psidev:mi " + cpathHome + separator + "testData"
                + separator + "psi_mi" + separator + "MIF.xsd";
        return validate(xml, psiMiLevel1);
    }

    private ArrayList execute(String xmlData, boolean validateAgainstSchema, String schemaLocation)
            throws IOException {
        errorList = new ArrayList();
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader
                    (DEFAULT_PARSER_NAME);
            if (validateAgainstSchema) {
                parser.setFeature(VALIDATION_FEATURE_ID, true);
                parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, true);
            } else {
                parser.setFeature(VALIDATION_FEATURE_ID, false);
                parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, false);
            }
            
            if (schemaLocation != null) {
                parser.setProperty(EXTERNAL_SCHEMA_LOCATION, schemaLocation);
            }
            parser.setContentHandler(this);
            parser.setErrorHandler(this);

            StringReader reader = new StringReader(xmlData);
            InputSource source = new InputSource(reader);
            parser.parse(source);
        } catch (SAXParseException e) {
            errorList.add(e);
        } catch (SAXException e) {
            errorList.add(e);
        }
        return errorList;
    }

    /**
     * Error.
     *
     * @param ex SAXParseException Object.
     * @throws SAXException SAXException.
     */
    public void error(SAXParseException ex) throws SAXException {
        errorList.add(ex);
    }

    /**
     * Fatal Error.
     *
     * @param ex SAXParseException Object.
     * @throws SAXException SAXException.
     */
    public void fatalError(SAXParseException ex) throws SAXException {
        errorList.add(ex);
    }
}
