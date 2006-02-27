// $Id: WebUIBean.java,v 1.6 2006-02-27 15:33:16 grossb Exp $
//------------------------------------------------------------------------------
/** Copyright (c) 2006 Memorial Sloan-Kettering Cancer Center.
 **
 ** Code written by: Ethan Cerami, Benjamin Gross
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
package org.mskcc.pathdb.form;

// imports

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;

import javax.servlet.http.HttpServletRequest;

/**
 * Struts ActionForm for updating/retrieving web ui elements.
 *
 * @author Benjamin Gross
 */
public class WebUIBean extends ActionForm {

    /**
     * Application Name.
     */
    private String applicationName;

    /**
     * Home Page Header.
     */
    private String homePageHeader;

    /**
     * Home Page Tag Line.
     */
    private String homePageTagLine;

    /**
     * Home Page Right Column Content.
     */
    private String homePageRightColumnContent;

    /**
     * Display Browse by Pathway Tab.
     */
    private boolean displayBrowseByPathwayTab;

    /**
     * Display Browse by Organism Tab.
     */
    private boolean displayBrowseByOrganismTab;

    /**
     * FAQ Page Content.
     */
    private String faqPageContent;

    /**
     * About Page Content.
     */
    private String aboutPageContent;

    /**
     * Maintenance Tag Line.
     */
    private String maintenanceTagLine;

    /**
     * Sets the Application Name.
     *
     * @param applicationName String.
     */
    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * Gets the Application Name.
     *
     * @return applicationName.
     */
    public String getApplicationName() {
        return applicationName;
    }

    /**
     * Sets the Home Page Header.
     *
     * @param homePageHeader String.
     */
    public void setHomePageHeader(String homePageHeader) {
        this.homePageHeader = homePageHeader;
    }

    /**
     * Gets the Home Page Header.
     *
     * @return homePageHeader.
     */
    public String getHomePageHeader() {
        return homePageHeader;
    }

    /**
     * Sets the Home Page Tag Line.
     *
     * @param homePageTagLine String.
     */
    public void setHomePageTagLine(String homePageTagLine) {
        this.homePageTagLine = homePageTagLine;
    }

    /**
     * Gets the Home Page Tag Line.
     *
     * @return homePageTagLine.
     */
    public String getHomePageTagLine() {
        return homePageTagLine;
    }

    /**
     * Sets the Home Page Right Column Content.
     *
     * @param homePageRightColumnContent String.
     */
    public void setHomePageRightColumnContent(String homePageRightColumnContent) {
        this.homePageRightColumnContent = homePageRightColumnContent;
    }

    /**
     * Gets the Home Page Right Column Content.
     *
     * @return homePageRightColumnContent.
     */
    public String getHomePageRightColumnContent() {
        return homePageRightColumnContent;
    }

    /**
     * Sets the Display Browse by Pathway Tab.
     *
     * @param displayBrowseByPathwayTab boolean.
     */
    public void setDisplayBrowseByPathwayTab(boolean displayBrowseByPathwayTab) {
        this.displayBrowseByPathwayTab = displayBrowseByPathwayTab;
    }

    /**
     * Gets the Display Browse by Pathway Tab.
     *
     * @return displayBrowseByPathwayTab.
     */
    public boolean getDisplayBrowseByPathwayTab() {
        return displayBrowseByPathwayTab;
    }

    /**
     * Sets the Display Browse by Organism Tab.
     *
     * @param displayBrowseByOrganismTab boolean.
     */
    public void setDisplayBrowseByOrganismTab(boolean displayBrowseByOrganismTab) {
        this.displayBrowseByOrganismTab = displayBrowseByOrganismTab;
    }

    /**
     * Gets the Display Browse by Organism Tab.
     *
     * @return displayBrowseByOrganismTab.
     */
    public boolean getDisplayBrowseByOrganismTab() {
        return displayBrowseByOrganismTab;
    }

    /**
     * Sets the FAQ Page Content.
     *
     * @param faqPageContent String.
     */
    public void setFAQPageContent(String faqPageContent) {
        this.faqPageContent = faqPageContent;
    }

    /**
     * Gets the FAQ Page Content.
     *
     * @return faqPageContent.
     */
    public String getFAQPageContent() {
        return faqPageContent;
    }

    /**
     * Sets the About Page Content.
     *
     * @param aboutPageContent String.
     */
    public void setAboutPageContent(String aboutPageContent) {
        this.aboutPageContent = aboutPageContent;
    }

    /**
     * Gets the About Page Content.
     *
     * @return aboutPageContent.
     */
    public String getAboutPageContent() {
        return aboutPageContent;
    }

    /**
     * Sets the Maintenance Tag Line.
     *
     * @param maintenanceTagLine String.
     */
    public void setMaintenanceTagLine(String maintenanceTagLine) {
        this.maintenanceTagLine = maintenanceTagLine;
    }

    /**
     * Gets the Maintenance Tag Line.
     *
     * @return maintenanceTagLine.
     */
    public String getMaintenanceTagLine() {
        return maintenanceTagLine;
    }

    /**
     * Our implementation of validate.
     *
     * @param mapping ActionMapping reference.
     * @param request HttpServletRequest.
     * @return ActionErrors reference.
     *
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        // create errors object to return
        ActionErrors errors = new ActionErrors();

        // homePageHeader
        if (homePageHeader == null || homePageHeader.equals("")) {
            errors.add("homePageHeader", new ActionError("error.missing.homePageHeader"));
        }

        // homePageTagLine
        if (homePageTagLine == null || homePageTagLine.equals("")) {
            errors.add("homePageTagLine", new ActionError("error.missing.homePageTagLine"));
        }

        // FAQ Page Content
        if (faqPageContent == null || faqPageContent.equals("")) {
            errors.add("faqPageContent", new ActionError("error.missing.faqPageContent"));
        }

        // About Page Content
        if (aboutPageContent == null || aboutPageContent.equals("")) {
            errors.add("aboutPageContent", new ActionError("error.missing.aboutPageContent"));
        }

        // MaintenanceTagLine
        if (maintenanceTagLine == null || maintenanceTagLine.equals("")) {
            errors.add("maintenanceTagLine", new ActionError("error.missing.maintenanceTagLine"));
        }

        // outta here
		return errors;
	}
}
