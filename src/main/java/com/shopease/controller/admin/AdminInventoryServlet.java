package com.shopease.controller.admin;

import java.io.IOException;
import java.util.List;

import com.shopease.model.Product;
import com.shopease.service.ProductService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/inventory")
public class AdminInventoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService;

    @Override
    public void init() {
        productService = new ProductService();
    }

    private boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        return session != null &&
               session.getAttribute("loggedInAdmin") != null;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(
                request.getContextPath() + "/admin/login"
            );
            return;
        }

        List<Product> products = productService.getAllProducts();

        request.setAttribute("products", products);

        request.getRequestDispatcher(
            "/admin/inventory.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdmin(request)) {
            response.sendRedirect(
                request.getContextPath() + "/admin/login"
            );
            return;
        }

        try {

            int productId = Integer.parseInt(
                request.getParameter("productId")
            );

            int stock = Integer.parseInt(
                request.getParameter("stock")
            );

            if (productId <= 0 || stock < 0) {
                response.sendRedirect(
                    request.getContextPath()
                    + "/admin/inventory?error=invalid"
                );
                return;
            }

            Product product =
                productService.getProductById(productId);

            if (product == null) {
                response.sendRedirect(
                    request.getContextPath()
                    + "/admin/inventory?error=notfound"
                );
                return;
            }

            product.setStock(stock);

            boolean updated =
                productService.updateProduct(product);

            response.sendRedirect(
                request.getContextPath()
                + "/admin/inventory?"
                + (updated ? "success=updated" : "error=failed")
            );

        } catch (NumberFormatException e) {

            response.sendRedirect(
                request.getContextPath()
                + "/admin/inventory?error=invalid"
            );

        } catch (Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                request.getContextPath()
                + "/admin/inventory?error=server"
            );
        }
    }
}