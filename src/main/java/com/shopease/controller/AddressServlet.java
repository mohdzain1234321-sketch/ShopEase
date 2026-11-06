package com.shopease.controller;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.shopease.model.Address;
import com.shopease.service.AddressService;

@WebServlet("/addresses")
public class AddressServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AddressService addressService;
    @Override public void init() { addressService = new AddressService(); }

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login?returnTo=cart"); return;
        }
        int userId = (Integer) session.getAttribute("userId");
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        request.setAttribute("addresses", addresses);
        request.getRequestDispatcher("/views/addresses.jsp").forward(request, response);
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login"); return;
        }
        int userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        boolean ok = false;
        try {
            if ("add".equals(action) || "update".equals(action)) {
                Address a = fromRequest(request); a.setUserId(userId);
                if ("add".equals(action)) ok = addressService.addAddress(a);
                else { a.setAddressId(Integer.parseInt(request.getParameter("addressId"))); ok = addressService.updateAddress(a); }
            } else if ("default".equals(action)) {
                ok = addressService.setDefaultAddress(Integer.parseInt(request.getParameter("addressId")), userId);
            } else if ("delete".equals(action)) {
                ok = addressService.deleteAddress(Integer.parseInt(request.getParameter("addressId")), userId);
            } else { response.sendError(400, "Unknown address action"); return; }
        } catch (NumberFormatException ex) { response.sendError(400, "Invalid address ID"); return; }
        response.sendRedirect(request.getContextPath() + "/addresses?message=" + (ok ? "success" : "error"));
    }
    private Address fromRequest(HttpServletRequest r) {
        Address a = new Address(); a.setAddressType(r.getParameter("addressType"));
        a.setFullName(r.getParameter("fullName")); a.setPhone(r.getParameter("phone"));
        a.setAddressLine(r.getParameter("addressLine")); a.setCity(r.getParameter("city"));
        a.setState(r.getParameter("state")); a.setPostalCode(r.getParameter("postalCode"));
        a.setCountry(r.getParameter("country")); return a;
    }
}
