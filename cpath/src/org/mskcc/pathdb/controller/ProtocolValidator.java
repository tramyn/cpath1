package org.mskcc.pathdb.controller;

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
    private static final String HELP_MESSAGE = "  Try cmd=help for Help";

    /**
     * Protocol Constants.
     */
    private ProtocolConstants constants = new ProtocolConstants();

    /**
     * Constructor.
     * @param request Protocol Request.
     */
    public ProtocolValidator(ProtocolRequest request) {
        this.request = request;
    }

    /**
     * Validates the Request object.
     * @throws ProtocolException Indicates Violation of Protocol.
     * @throws NeedsHelpException Indicates user requests/needs help.
     */
    public void validate() throws ProtocolException, NeedsHelpException {
        validateEmptySet();
        validateCommand();
        validateVersion();
        validateDatabase();
        validateFormat();
        validateUid();
    }

    /**
     * Validates the Command Parameter.
     * @throws ProtocolException Indicates Violation of Protocol.
     * @throws NeedsHelpException Indicates user requests/needs help.
     */
    private void validateCommand() throws ProtocolException,
            NeedsHelpException {
        if (request.getCommand() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument:  'cmd' is not specified."
                    + HELP_MESSAGE);
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
     * Validates the Database Parameter.
     * @throws ProtocolException Indicates Violation of Protocol.
     */
    private void validateDatabase() throws ProtocolException {
        if (request.getDatabase() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument:  'db' is not specified."
                    + HELP_MESSAGE);
        }
        HashMap map = constants.getValidDatabases();
        if (!map.containsKey(request.getDatabase())) {
            throw new ProtocolException(ProtocolStatusCode.BAD_DATA_SOURCE,
                    "Datasource:  '" + request.getDatabase()
                    + "' is not recognized.  " + HELP_MESSAGE);
        }
    }

    /**
     * Validates the Format Parameter.
     * @throws ProtocolException Indicates Violation of Protocol.
     */
    private void validateFormat() throws ProtocolException {
        if (request.getFormat() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument:  'format' is not specified."
                    + HELP_MESSAGE);
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
     * @throws ProtocolException Indicates Violation of Protocol.
     */
    private void validateUid() throws ProtocolException {
        if (request.getUid() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument:  'uid' is not specified."
                    + HELP_MESSAGE);
        }
    }

    /**
     * Validates the Version Parameter.
     * @throws ProtocolException Indicates Violation of Protocol.
     */
    private void validateVersion() throws ProtocolException {
        if (request.getVersion() == null) {
            throw new ProtocolException(ProtocolStatusCode.MISSING_ARGUMENTS,
                    "Argument: 'version' is not specified."
                    + HELP_MESSAGE);
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
     * @throws NeedsHelpException Indicates user requests/needs help.
     */
    private void validateEmptySet() throws NeedsHelpException {
        if (request.isEmpty()) {
            throw new NeedsHelpException();
        }
    }
}