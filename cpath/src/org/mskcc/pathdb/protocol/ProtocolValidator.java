/** Copyright (c) 2004 Memorial Sloan-Kettering Cancer Center.
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
package org.mskcc.pathdb.protocol;

import java.util.HashMap;

/**
 * Validates Client/Browser Request.
 *
 * @author Ethan Cerami
 */
public class ProtocolValidator {
    /**
     * Protocol Request.
     */
    private ProtocolRequest request;

    /**
     * Current Version Implemented.
     */
    private static final String CURRENT_VERSION = "1.0";

    /**
     * Help Message
     */
    private static final String HELP_MESSAGE = "  Please try again.";

    /**
     * Protocol Constants.
     */
    private ProtocolConstants constants = new ProtocolConstants();

    /**
     * Constructor.
     *
     * @param request Protocol Request.
     */
    public ProtocolValidator(ProtocolRequest request) {
        this.request = request;
    }

    /**
     * Validates the Request object.
     *
     * @throws ProtocolException  Indicates Violation of Protocol.
     * @throws NeedsHelpException Indicates user requests/needs help.
     */
    public void validate() throws ProtocolException, NeedsHelpException {
        validateEmptySet();
        validateCommand();
        validateVersion();
        validateFormat();
        validateQuery();
    }

    /**
     * Validates the Command Parameter.
     *
     * @throws ProtocolException  Indicates Violation of Protocol.
     * @throws NeedsHelpException Indicates user requests/needs help.
     */
    private void validateCommand() throws ProtocolException,
            NeedsHelpException {
        if (request.getCommand() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument:  '" + ProtocolRequest.ARG_COMMAND
                    + "' is not specified." + HELP_MESSAGE);
        }
        HashMap map = constants.getValidCommands();
        if (!map.containsKey(request.getCommand())) {
            throw new ProtocolException(ProtocolStatusCode.BAD_COMMAND,
                    "Command:  '" + request.getCommand()
                    + "' is not recognized." + HELP_MESSAGE);
        }
        if (request.getCommand().equals(ProtocolConstants.COMMAND_HELP)) {
            throw new NeedsHelpException();
        }
    }

    /**
     * Validates the Format Parameter.
     *
     * @throws ProtocolException Indicates Violation of Protocol.
     */
    private void validateFormat() throws ProtocolException {
        if (request.getFormat() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument:  '" + ProtocolRequest.ARG_FORMAT
                    + "' is not specified." + HELP_MESSAGE);
        }
        HashMap map = constants.getValidFormats();
        if (!map.containsKey(request.getFormat())) {
            throw new ProtocolException(ProtocolStatusCode.BAD_FORMAT,
                    "Format:  '" + request.getFormat() + "' is not recognized."
                    + HELP_MESSAGE);
        }
    }

    /**
     * Validates the UID Parameter.
     *
     * @throws ProtocolException Indicates Violation of Protocol.
     */
    private void validateQuery() throws ProtocolException {
        String q = request.getQuery();
        String command = request.getCommand();
        if ((q == null || q.length() == 0)
                && (request.getOrganism() == null
                || request.getOrganism().length() == 0)) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument:  '" + ProtocolRequest.ARG_QUERY
                    + "' is not specified." + HELP_MESSAGE,
                    "You did not specify a search term.  Please try again.");
        }
    }

    /**
     * Validates the Version Parameter.
     *
     * @throws ProtocolException Indicates Violation of Protocol.
     */
    private void validateVersion() throws ProtocolException {
        if (request.getVersion() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument: '" + ProtocolRequest.ARG_VERSION
                    + "' is not specified." + HELP_MESSAGE);
        }
        if (!request.getVersion().equals(CURRENT_VERSION)) {
            throw new ProtocolException
                    (ProtocolStatusCode.VERSION_NOT_SUPPORTED,
                            "This data service currently only supports "
                    + "version 1.0." + HELP_MESSAGE);
        }
    }

    /**
     * Checks if no arguments are specified.
     * If none are specified, throws NeedsHelpException.
     *
     * @throws NeedsHelpException Indicates user requests/needs help.
     */
    private void validateEmptySet() throws NeedsHelpException {
        if (request.isEmpty()) {
            throw new NeedsHelpException();
        }
    }
}