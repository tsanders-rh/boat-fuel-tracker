package com.boatfuel.servlet;

import com.boatfuel.ejb.FuelUpServiceHome;
import com.boatfuel.ejb.FuelUpServiceRemote;
import com.boatfuel.entity.FuelUp;
import com.boatfuel.util.FileSystemHelper;
import com.boatfuel.util.JNDILookupHelper;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * Legacy Servlet using old patterns (anti-pattern)
 * Konveyor violations:
 * - Extends HttpServlet (old style, should use @WebServlet)
 * - Manual EJB lookup with JNDI
 * - No dependency injection
 * - Session management in servlet
 * - Using PrintWriter instead of JSP/templating
 */
public class FuelUpServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(FuelUpServlet.class);

    private FuelUpServiceRemote fuelUpService;

    /**
     * Initialize servlet with EJB lookup (anti-pattern)
     * Konveyor will flag: Manual JNDI EJB lookup, should use @EJB injection
     */
    @Override
    public void init() throws ServletException {
        try {
            logger.info("Initializing FuelUpServlet");

            // Manual EJB 2.x lookup (anti-pattern)
            Context ctx = new InitialContext();
            Object obj = ctx.lookup("java:comp/env/ejb/FuelUpService");

            // PortableRemoteObject narrow (EJB 2.x pattern)
            FuelUpServiceHome home = (FuelUpServiceHome)
                PortableRemoteObject.narrow(obj, FuelUpServiceHome.class);

            fuelUpService = home.create();

            logger.info("FuelUpService EJB lookup successful");

        } catch (Exception e) {
            logger.error("Failed to initialize EJB", e);
            throw new ServletException("Cannot initialize EJB", e);
        }
    }

    /**
     * Handle GET requests with HTML generation in servlet (anti-pattern)
     * Konveyor will flag: HTML in servlet, should use JSP/JSF/templates
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(true);
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendRedirect("login");
            return;
        }

        try {
            // Audit log to file system (anti-pattern)
            FileSystemHelper.writeAuditLog("User " + userId + " accessed fuel-ups");

            List<FuelUp> fuelUps = fuelUpService.getFuelUpsByUser(userId);

            // Generating HTML in servlet (anti-pattern)
            out.println("<html>");
            out.println("<head><title>Boat Fuel Tracker</title></head>");
            out.println("<body>");
            out.println("<h1>Your Fuel-Ups</h1>");
            out.println("<table border='1'>");
            out.println("<tr><th>Date</th><th>Gallons</th><th>Price/Gal</th><th>Total</th></tr>");

            for (FuelUp fuelUp : fuelUps) {
                out.println("<tr>");
                out.println("<td>" + fuelUp.getDate() + "</td>");
                out.println("<td>" + fuelUp.getGallons() + "</td>");
                out.println("<td>$" + fuelUp.getPricePerGallon() + "</td>");
                out.println("<td>$" + fuelUp.getTotalCost() + "</td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            logger.error("Error retrieving fuel-ups", e);
            out.println("<h1>Error: " + e.getMessage() + "</h1>");
        }
    }

    /**
     * Handle POST with manual session management (anti-pattern)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // Process form data...
        logger.info("Processing fuel-up submission from user: " + userId);

        // Redirect back to GET
        response.sendRedirect(request.getContextPath() + "/fuelups");
    }

    @Override
    public void destroy() {
        logger.info("Destroying FuelUpServlet");
        super.destroy();
    }
}
