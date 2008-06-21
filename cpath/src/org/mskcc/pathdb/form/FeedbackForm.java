package org.mskcc.pathdb.form;

import org.apache.struts.action.*;
import org.mskcc.pathdb.servlet.CPathUIConfig;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.methods.PostMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Feedback Form.
 *
 * @author Ethan Cerami
 */
public class FeedbackForm extends ActionForm {

	private static final String CBIO_MSKCC_ORG_RECAPTCHA_KEY = "6Ld3PgIAAAAAAB8ZdAwyf4Rp2jvF24pDyUmMbZ6f";
	private static final String PATHWAY_COMMONS_ORG_RECAPTCHA_KEY = "6Ld2PgIAAAAAAINTdo1lTZQt0eZQAzPbHfAAzYmt";
	

    private String email;
    private String subject;
    private String message;

    /**
     * Gets email address.
     * @return email email address.
     */
    public String getEmail () {
        return email;
    }

    /**
     * Sets email address.
     * @param email email address
     */
    public void setEmail (String email) {
        this.email = email;
    }

    /**
     * Get the subject.
     * @return subject.
     */
    public String getSubject () {
        return subject;
    }

    /**
     * Sets the subject.
     * @param subject subject.
     */
    public void setSubject (String subject) {
        this.subject = subject;
    }

    /**
     * Gets the message.
     * @return message.
     */
    public String getMessage () {
        return message;
    }

    /**
     * Sets the message.
     * @param message message.
     */
    public void setMessage (String message) {
        this.message = message;
    }

    /**
     * Validate form data.
     * @param actionMapping         ActionMapping Object.
     * @param httpServletRequest    Servlet Request.
     * @return ActionErrors List.
     */
    public ActionErrors validate (ActionMapping actionMapping,
            HttpServletRequest httpServletRequest) {
        WebUIBean uiBean = CPathUIConfig.getWebUIBean();
        ActionErrors errors = new ActionErrors();

        //  Check for required fields, and valid email address.
        if (email == null || email.length() < 1) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError ("error.feedback_form.required",
                    "Email address"));
        }
        if (subject == null || subject.length() < 1) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError ("error.feedback_form.required",
                    "Subject"));
        }
        if (message == null || message.length() < 1) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError ("error.feedback_form.required",
                    "Message"));
        }
        if (email != null && email.length() > 0
            && !email.matches(".+@.+\\.[a-z]+")) {
            errors.add(ActionErrors.GLOBAL_ERROR,
                    new ActionError("error.feedback_form.invalid_email"));
        }
        if (message != null) {
            //  Find out how many URLs the message has.
            //  If there is more than one URL that does not point to baseURL,
            //  flag it as invalid.
            int numNonBaseUrls = 0;
            String words[] = message.split("\\s");
            for (int i=0; i<words.length; i++) {
                String word = words[i];
                if (word.startsWith("http://")) {
                    if (uiBean != null && uiBean.getBaseURL() != null) {
                        if (word.indexOf(uiBean.getBaseURL()) == -1) {
                            numNonBaseUrls++;
                        }
                    }
                }
            }
            if (numNonBaseUrls > 1) {
                errors.add(ActionErrors.GLOBAL_ERROR,
                        new ActionError("error.feedback_form.invalid_message"));
            }
        }

		// validate challenge
		if (!validChallengeResponse(httpServletRequest)) {
			errors.add(ActionErrors.GLOBAL_ERROR,
					   new ActionError("error.feedback_form.invalid_challenge_response", "Challenge"));
		}

        return errors;
    }

	/**
	 * Method to validate challenge response.
	 *
	 * @param httpServletRequest HttpServletRequest
	 */
	private boolean validChallengeResponse(HttpServletRequest httpServletRequest) {

		// get the challenge field
		String challenge = httpServletRequest.getParameter("recaptcha_challenge_field");
		if (challenge == null || challenge.length() == 0) return false;

		// get challenge response field
		String challengeResponse = httpServletRequest.getParameter("recaptcha_response_field");
		if (challengeResponse == null || challengeResponse.length() == 0) return false;

		// private key
		String privateKey = (httpServletRequest.getServerName().equals("pathway.commons.org")) ?
			PATHWAY_COMMONS_ORG_RECAPTCHA_KEY : CBIO_MSKCC_ORG_RECAPTCHA_KEY;

		// validate with reCAPTCHA servers
		HttpClient client = new HttpClient();
		NameValuePair nvps[] = new NameValuePair[4];
		nvps[0] = new NameValuePair("privatekey", privateKey);
		nvps[1] = new NameValuePair("remoteip", httpServletRequest.getRemoteAddr());
		nvps[2] = new NameValuePair("challenge", challenge);
		nvps[3] = new NameValuePair("response", challengeResponse);

		// create method
		HttpMethodBase method = new PostMethod("http://api-verify.recaptcha.net/verify");
		((PostMethod)(method)).addParameters(nvps);

		// outta here
		return executeMethod(client, method);
	}

	/**
	 * Executes http request.
	 *
	 * @param client HttpClient
	 * @param method HttpMethodBase
	 * @return boolean
	 */
	private boolean executeMethod(HttpClient client, HttpMethodBase method) {

		try {
			// execute method
			int statusCode = client.executeMethod(method);
			if (statusCode != 200) return false;

			// read in content
			String[] content = readContent(method).split("\n");

			// outta here
			return (content[0].contains("true"));
        }
		catch (Exception e) {
            return false;
        }
	}

    /**
     * Reads content of http request.
	 * 
	 * @param method HttpMethodBase
	 * @throws java.io.IOException
     */
    private String readContent(HttpMethodBase method) throws java.io.IOException {

		// create input stream to read response
		java.io.InputStream instream = method.getResponseBodyAsStream();

		// create outputstream to write response into
		java.io.ByteArrayOutputStream outstream =  new java.io.ByteArrayOutputStream(1024);
		byte[] buffer = new byte[1024];
		int len;
		int totalBytes = 0;
		while ((len = instream.read(buffer)) > 0) {
			outstream.write(buffer, 0, len);
		}
		instream.close();

		// outta here
		return new String(outstream.toByteArray());
	}
}