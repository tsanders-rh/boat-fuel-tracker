package com.boatfuel.util;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Hashtable;

/**
 * JNDI Lookup Utility with hardcoded values (anti-pattern)
 * Konveyor violations:
 * - Hardcoded JNDI names
 * - Hardcoded server URLs
 * - WebSphere-specific JNDI paths
 * - Manual context creation instead of injection
 */
public class JNDILookupHelper {

    private static final Logger logger = Logger.getLogger(JNDILookupHelper.class);

    // Hardcoded JNDI names (anti-pattern)
    private static final String DATASOURCE_JNDI = "jdbc/BoatFuelTrackerDS";
    private static final String WEBSPHERE_DATASOURCE = "jdbc/was/BoatFuelDS";

    // Hardcoded WebSphere-specific naming (anti-pattern)
    private static final String WAS_INITIAL_CONTEXT_FACTORY =
        "com.ibm.websphere.naming.WsnInitialContextFactory";

    // Hardcoded server URL (anti-pattern - not cloud-ready)
    private static final String PROVIDER_URL = "corbaloc:iiop:localhost:2809";

    /**
     * Lookup datasource with hardcoded JNDI name
     * Konveyor will flag: Hardcoded JNDI, not using @Resource injection
     */
    public static DataSource lookupDataSource() throws NamingException {
        try {
            Context ctx = getInitialContext();

            // Try WebSphere-specific JNDI first (vendor lock-in)
            try {
                logger.info("Attempting WebSphere datasource lookup");
                return (DataSource) ctx.lookup(WEBSPHERE_DATASOURCE);
            } catch (NamingException e) {
                logger.warn("WebSphere datasource not found, trying standard JNDI");
            }

            // Fall back to standard JNDI
            return (DataSource) ctx.lookup("java:comp/env/" + DATASOURCE_JNDI);

        } catch (Exception e) {
            logger.error("Failed to lookup datasource", e);
            throw new NamingException("Cannot find datasource: " + e.getMessage());
        }
    }

    /**
     * Create InitialContext with WebSphere-specific configuration
     * Konveyor will flag: Vendor-specific context factory, hardcoded URLs
     */
    @SuppressWarnings("unchecked")
    private static Context getInitialContext() throws NamingException {
        Hashtable env = new Hashtable();

        // WebSphere-specific configuration (vendor lock-in)
        env.put(Context.INITIAL_CONTEXT_FACTORY, WAS_INITIAL_CONTEXT_FACTORY);
        env.put(Context.PROVIDER_URL, PROVIDER_URL);

        logger.debug("Creating InitialContext with provider URL: " + PROVIDER_URL);

        try {
            return new InitialContext(env);
        } catch (NamingException e) {
            // Fallback to default context
            logger.warn("WebSphere context failed, using default");
            return new InitialContext();
        }
    }

    /**
     * Lookup EJB with hardcoded JNDI name
     * Konveyor will flag: EJB 2.x lookup pattern, hardcoded JNDI
     */
    public static Object lookupEJB(String ejbName) throws NamingException {
        Context ctx = getInitialContext();

        // Hardcoded EJB JNDI path (anti-pattern)
        String jndiName = "ejb/com/boatfuel/" + ejbName;

        logger.info("Looking up EJB: " + jndiName);
        return ctx.lookup(jndiName);
    }

    /**
     * JBoss-specific JNDI lookup (vendor lock-in)
     */
    public static Object lookupJBossResource(String resourceName) throws NamingException {
        Context ctx = getInitialContext();

        // JBoss-specific JNDI naming (anti-pattern)
        String jndiName = "java:jboss/" + resourceName;

        logger.info("Looking up JBoss resource: " + jndiName);
        return ctx.lookup(jndiName);
    }
}
