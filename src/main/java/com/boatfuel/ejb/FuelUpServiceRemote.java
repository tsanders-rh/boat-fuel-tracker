package com.boatfuel.ejb;

import com.boatfuel.entity.FuelUp;
import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.List;

/**
 * EJB 2.x Remote interface (anti-pattern)
 * Konveyor violations:
 * - EJB 2.x Remote interface (deprecated)
 * - RMI/RemoteException on every method
 * - Should be migrated to @Remote annotation or local interfaces
 */
public interface FuelUpServiceRemote extends EJBObject {

    /**
     * Create a new fuel-up record
     */
    FuelUp createFuelUp(FuelUp fuelUp) throws RemoteException;

    /**
     * Get all fuel-ups for a user
     */
    List<FuelUp> getFuelUpsByUser(String userId) throws RemoteException;

    /**
     * Delete a fuel-up
     */
    void deleteFuelUp(Long fuelUpId) throws RemoteException;

    /**
     * Get fuel-up statistics
     */
    FuelUpStatistics getStatistics(String userId) throws RemoteException;
}
