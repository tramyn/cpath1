package org.mskcc.pathdb.servlet;

import com.opensymphony.module.oscache.base.Cache;
import com.opensymphony.module.oscache.base.NeedsRefreshException;
import com.opensymphony.module.oscache.web.ServletCacheAdministrator;
import com.opensymphony.module.oscache.web.filter.
        CacheHttpServletResponseWrapper;
import com.opensymphony.module.oscache.web.filter.ResponseContent;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Cache Filter.
 * Copied from the com.opensymphony.module.oscache.web.
 * Modified to use the default Logging system.
 *
 * @author OSCache Team.
 */
public class CacheFilter implements Filter {
    /**
     * Filter Config Object.
     */
    private FilterConfig config;

    /**
     * Scope of Scaching.
     */
    private int cacheScope = PageContext.APPLICATION_SCOPE;

    /**
     * Administrator.
     */
    private ServletCacheAdministrator admin = null;

    /**
     * Time before cache should be refreshed - default one hour (in seconds)
     */
    private int time;

    /**
     * Logger.
     */
    private Logger logger = Logger.getLogger(this.getClass().getName());

    /**
     * Initialize the filter
     *
     * @param filterConfig The filter configuration
     */
    public void init(FilterConfig filterConfig) {
        config = filterConfig;
        admin = ServletCacheAdministrator.getInstance
                (config.getServletContext());
        try {
            time = Integer.parseInt(config.getInitParameter("time"));
        } catch (Exception e) {
            time = 60 * 60;
            logger.error("Error Parsing Time Parameter:  " + e.toString());
        }
    }

    /**
     * Filter clean-up
     */
    public void destroy() {
        logger.info("Shuting down CacheFilter");
    }

    /**
     * Process the doFilter
     *
     * @param request The servlet request
     * @param response The servlet response
     * @param chain The filet chain
     * @throws ServletException Indicates Servlet Error.
     * @throws IOException Indicates I/O Error.
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        log ("Incoming request from  IP:  " + request.getRemoteAddr());
        log ("Incoming request from  Host:  " + request.getRemoteHost());
        log ("URL Requested:  " + hRequest.getQueryString());
        String key = admin.generateEntryKey(null, (HttpServletRequest)
                request, cacheScope);
        log ("Using Cache Key:  " + key);
        Cache cache = admin.getCache((HttpServletRequest) request, cacheScope);
        try {
            ResponseContent respContent = (ResponseContent)
                    cache.getFromCache(key, time);
            log ("Using cached entry");
            if (respContent == null) {
                throw new NeedsRefreshException (respContent);
            }
            respContent.writeTo(response);
        } catch (NeedsRefreshException nre) {
            log ("New cache entry, or cache entry is stale");
            CacheHttpServletResponseWrapper cacheResponse =
                    new CacheHttpServletResponseWrapper
                            ((HttpServletResponse) response);
            chain.doFilter(request, cacheResponse);
            // Store as the cache content the result of the response
            ResponseContent content = cacheResponse.getContent();
            cache.putInCache(key, content);
        }
    }

    private void log (String msg) {
        System.err.println(msg);
        logger.info(msg);
    }
}