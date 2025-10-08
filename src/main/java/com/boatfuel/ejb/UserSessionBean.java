package com.boatfuel.ejb;

import com.boatfuel.entity.User;
import org.apache.log4j.Logger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import java.rmi.RemoteException;

/**
 * Stateful Session Bean used for HTTP session management (anti-pattern)
 * Konveyor violations:
 * - EJB 2.x Stateful Session Bean (deprecated)
 * - Using EJB for HTTP session state (anti-pattern)
 * - Should use HTTP session or stateless pattern
 * - SessionSynchronization for transaction callbacks
 */
public class UserSessionBean implements SessionBean, SessionSynchronization {

    private static final Logger logger = Logger.getLogger(UserSessionBean.class);

    private SessionContext sessionContext;
    private User currentUser;
    private String sessionId;
    private long lastAccessTime;

    /**
     * EJB 2.x lifecycle method
     */
    public void ejbCreate(String sessionId) {
        logger.info("Creating UserSessionBean for session: " + sessionId);
        this.sessionId = sessionId;
        this.lastAccessTime = System.currentTimeMillis();
    }

    /**
     * Store user in stateful bean (anti-pattern)
     * Should use HTTP session instead
     */
    public void setCurrentUser(User user) throws RemoteException {
        logger.info("Setting current user: " + user.getEmail());
        this.currentUser = user;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public User getCurrentUser() throws RemoteException {
        this.lastAccessTime = System.currentTimeMillis();
        return currentUser;
    }

    public boolean isLoggedIn() throws RemoteException {
        return currentUser != null;
    }

    public void logout() throws RemoteException {
        logger.info("User logging out: " + (currentUser != null ? currentUser.getEmail() : "unknown"));
        this.currentUser = null;
    }

    public long getLastAccessTime() throws RemoteException {
        return lastAccessTime;
    }

    // SessionSynchronization methods (EJB 2.x pattern)
    public void afterBegin() throws RemoteException {
        logger.debug("Transaction started for session: " + sessionId);
    }

    public void beforeCompletion() throws RemoteException {
        logger.debug("Before transaction completion for session: " + sessionId);
    }

    public void afterCompletion(boolean committed) throws RemoteException {
        logger.debug("Transaction " + (committed ? "committed" : "rolled back") + " for session: " + sessionId);
    }

    // EJB 2.x lifecycle methods
    public void ejbActivate() throws RemoteException {
        logger.debug("ejbActivate called for session: " + sessionId);
    }

    public void ejbPassivate() throws RemoteException {
        logger.debug("ejbPassivate called for session: " + sessionId);
    }

    public void ejbRemove() throws RemoteException {
        logger.info("ejbRemove called for session: " + sessionId);
    }

    public void setSessionContext(SessionContext sessionContext) throws RemoteException {
        this.sessionContext = sessionContext;
    }
}
