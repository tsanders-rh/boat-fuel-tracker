package com.boatfuel.ejb;

import com.boatfuel.entity.FuelUp;
import com.boatfuel.util.JNDILookupHelper;
import org.apache.log4j.Logger;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * EJB 2.x Session Bean implementation (anti-pattern)
 * Konveyor violations:
 * - EJB 2.x SessionBean interface (deprecated)
 * - Manual JNDI lookups (hardcoded)
 * - Mixed JPA and JDBC code
 * - Log4j 1.x usage
 * - No dependency injection
 */
public class FuelUpServiceBean implements SessionBean {

    private static final Logger logger = Logger.getLogger(FuelUpServiceBean.class);

    private SessionContext sessionContext;
    private EntityManager entityManager;

    /**
     * EJB 2.x lifecycle method
     */
    public void ejbCreate() {
        logger.info("Creating FuelUpServiceBean");
        try {
            // Hardcoded JNDI lookup (anti-pattern)
            InitialContext ctx = new InitialContext();
            entityManager = (EntityManager) ctx.lookup("java:comp/env/persistence/EntityManager");
        } catch (Exception e) {
            logger.error("Failed to lookup EntityManager", e);
            throw new RuntimeException("Cannot initialize EJB", e);
        }
    }

    /**
     * Create fuel-up using JPA
     */
    public FuelUp createFuelUp(FuelUp fuelUp) throws RemoteException {
        try {
            logger.info("Creating new fuel-up for user: " + fuelUp.getUser().getUserId());
            entityManager.persist(fuelUp);
            return fuelUp;
        } catch (Exception e) {
            logger.error("Error creating fuel-up", e);
            throw new RemoteException("Failed to create fuel-up", e);
        }
    }

    /**
     * Get fuel-ups using JPA
     */
    public List<FuelUp> getFuelUpsByUser(String userId) throws RemoteException {
        try {
            logger.debug("Getting fuel-ups for user: " + userId);
            Query query = entityManager.createQuery(
                "SELECT f FROM FuelUp f WHERE f.user.userId = :userId ORDER BY f.date DESC");
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            logger.error("Error retrieving fuel-ups", e);
            throw new RemoteException("Failed to retrieve fuel-ups", e);
        }
    }

    /**
     * Delete fuel-up
     */
    public void deleteFuelUp(Long fuelUpId) throws RemoteException {
        try {
            logger.info("Deleting fuel-up: " + fuelUpId);
            FuelUp fuelUp = entityManager.find(FuelUp.class, fuelUpId);
            if (fuelUp != null) {
                entityManager.remove(fuelUp);
            }
        } catch (Exception e) {
            logger.error("Error deleting fuel-up", e);
            throw new RemoteException("Failed to delete fuel-up", e);
        }
    }

    /**
     * Get statistics using direct JDBC (anti-pattern - mixing JPA and JDBC)
     * Konveyor will flag: Direct JDBC usage, hardcoded SQL, datasource lookup
     */
    public FuelUpStatistics getStatistics(String userId) throws RemoteException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Hardcoded JNDI lookup for datasource (anti-pattern)
            DataSource ds = JNDILookupHelper.lookupDataSource();
            conn = ds.getConnection();

            // Direct SQL query (should use JPA)
            String sql = "SELECT COUNT(*), SUM(GALLONS), SUM(TOTAL_COST), AVG(PRICE_PER_GALLON) " +
                        "FROM FUEL_UPS WHERE USER_ID = ?";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userId);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                BigDecimal totalGallons = rs.getBigDecimal(2);
                BigDecimal totalSpent = rs.getBigDecimal(3);
                BigDecimal avgPrice = rs.getBigDecimal(4);

                return new FuelUpStatistics(count, totalGallons, totalSpent, avgPrice);
            }

            return new FuelUpStatistics(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);

        } catch (Exception e) {
            logger.error("Error calculating statistics", e);
            throw new RemoteException("Failed to calculate statistics", e);
        } finally {
            // Manual resource cleanup (anti-pattern - should use try-with-resources)
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                logger.warn("Error closing JDBC resources", e);
            }
        }
    }

    // EJB 2.x lifecycle methods
    public void ejbActivate() throws RemoteException {
        logger.debug("ejbActivate called");
    }

    public void ejbPassivate() throws RemoteException {
        logger.debug("ejbPassivate called");
    }

    public void ejbRemove() throws RemoteException {
        logger.info("ejbRemove called");
    }

    public void setSessionContext(SessionContext sessionContext) throws RemoteException {
        this.sessionContext = sessionContext;
    }
}
