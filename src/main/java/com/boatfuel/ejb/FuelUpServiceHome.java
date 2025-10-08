package com.boatfuel.ejb;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import java.rmi.RemoteException;

/**
 * EJB 2.x Home interface (anti-pattern)
 * Konveyor violations:
 * - EJB 2.x Home interface (deprecated)
 * - RMI/RemoteException (should use local interfaces or CDI)
 * - Should be migrated to EJB 3.x @Stateless beans or CDI
 */
public interface FuelUpServiceHome extends EJBHome {

    /**
     * Create method for EJB 2.x
     * @return Remote interface
     * @throws RemoteException RMI exception
     * @throws CreateException EJB creation exception
     */
    FuelUpServiceRemote create() throws RemoteException, CreateException;
}
