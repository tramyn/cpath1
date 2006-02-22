// $Id: InteractionSummaryUtils.java,v 1.22 2006-02-22 22:47:50 grossb Exp $
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
package org.mskcc.pathdb.schemas.biopax.summary;

// imports

import org.mskcc.pathdb.model.BioPaxControlTypeMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class generates Summary Strings of BioPAX Interaction Objects.
 *
 * @author Benjamin Gross, Ethan Cerami.
 */
public class InteractionSummaryUtils {

    /**
     * Space Character.
     */
    private static final String SPACE = " ";

    /**
     * Creates the interaction summary string.
     *
     * @param interactionSummary PhysicalInteractiong
     * @return HTML String
     */
    public static String createInteractionSummaryString
            (InteractionSummary interactionSummary) {
        StringBuffer buf = new StringBuffer();

        //  Branch, depending on interaction type.
        if (interactionSummary instanceof ConversionInteractionSummary) {
            createConversionInteractionSummary(interactionSummary, buf);
        } else if (interactionSummary instanceof ControlInteractionSummary) {
            createControlSummary(interactionSummary, buf);
        } else if (interactionSummary instanceof PhysicalInteractionSummary) {
            createPhysicalInteractionSummary(interactionSummary, buf);
        } else {
            buf.append("Interaction Type Not yet supported!");
        }
        return buf.toString();
    }

    /**
     * Creates a Conversion Interaction Summary.
     *
     * @param interactionSummary InteractionSummary Object.
     * @param buf                HTML String Buffer.
     */
    private static void createConversionInteractionSummary(InteractionSummary
            interactionSummary, StringBuffer buf) {
        ConversionInteractionSummary summary =
                (ConversionInteractionSummary) interactionSummary;

        //  Create left side
        ArrayList left = summary.getLeftSideComponents();
        createSide(left, interactionSummary, buf);

        //  Create rigth side
        ArrayList right = summary.getRightSideComponents();
        buf.append(" &rarr; ");
        createSide(right, interactionSummary, buf);
    }

    /**
     * Creates a Physical Interaction Summary.
     *
     * @param interactionSummary InteractionSummary Object.
     * @param buf                HTML String Buffer.
     */
    private static void createPhysicalInteractionSummary(InteractionSummary
            interactionSummary, StringBuffer buf) {
        PhysicalInteractionSummary summary =
                (PhysicalInteractionSummary) interactionSummary;

        //  Iterate through all participants
        ArrayList participantList = summary.getParticipants();
        for (int i = 0; i < participantList.size(); i++) {
            ParticipantSummaryComponent component =
                    (ParticipantSummaryComponent) participantList.get(i);
            buf.append(BioPaxRecordSummaryUtils.createEntityLink
                    (component, interactionSummary));
            if (i < participantList.size() - 1) {
                buf.append(", ");
            }
        }
    }

    /**
     * Creates a Control Interaction Summary.
     *
     * @param interactionSummary InteractionSummary.
     * @param buf                HTML String Buffer.
     */
    private static void createControlSummary(InteractionSummary
            interactionSummary, StringBuffer buf) {
        BioPaxControlTypeMap map = new BioPaxControlTypeMap();
        ControlInteractionSummary summary =
                (ControlInteractionSummary) interactionSummary;
        List controllerList = summary.getControllers();

        //  Iterate through all controllers.
        for (int i = 0; i < controllerList.size(); i++) {
            ParticipantSummaryComponent component =
                    (ParticipantSummaryComponent) controllerList.get(i);
            buf.append(BioPaxRecordSummaryUtils.createEntityLink
                    (component, interactionSummary));
            if (i < controllerList.size() - 1) {
                buf.append(", ");
            }
        }

        //  Output control type in Plain English
        String controlType = summary.getControlType();
        String controlTypeInEnglish = (String) map.get(controlType);
        if (controlTypeInEnglish != null) {
            buf.append(SPACE + controlTypeInEnglish + SPACE);
        }

        //  Iterate through all controlled elements.
        List controlledList = summary.getControlled();

        if (controlledList != null) {
            if (controlledList.size() == 1) {
                EntitySummary entitySummary =
                        (EntitySummary) controlledList.get(0);
                if (entitySummary instanceof InteractionSummary) {
                    InteractionSummary intxnSummary =
                            (InteractionSummary) entitySummary;
                    buf.append("[");
                    buf.append(InteractionSummaryUtils.
                            createInteractionSummaryString(intxnSummary));
                    buf.append("]");
                } else {
                    buf.append(entitySummary.getName());
                }
            }
        }
    }

    /**
     * Create Left/Right Side of a reaction.
     *
     * @param list    List of ParticipantSummaryComponent Objects.
     * @param summary InteractionSummary Object.
     * @param buf     HTML String Buffer.
     */
    private static void createSide(ArrayList list, InteractionSummary summary,
            StringBuffer buf) {
        for (int i = 0; i < list.size(); i++) {
            ParticipantSummaryComponent component =
                    (ParticipantSummaryComponent) list.get(i);
            buf.append(BioPaxRecordSummaryUtils.createEntityLink
                    (component, summary));
            if (i < list.size() - 1) {
                buf.append(" + ");
            }
        }
    }
}
