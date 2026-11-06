package com.shopease.controller.admin;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import com.shopease.model.Product;
import com.shopease.service.ProductService;
import com.shopease.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService;
    private CategoryService categoryService;

    @Override
    public void init() {
        productService = new ProductService();
        categoryService = new CategoryService();
    }

    private boolean isAdminLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null
                && session.getAttribute("loggedInAdmin") != null;
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String action = request.getParameter("action");

        try {
            List<?> categories = categoryService.getAllCategories();
            request.setAttribute("categories", categories);

            if ("add".equals(action)) {
                request.getRequestDispatcher(
                    "/admin/add-product.jsp"
                ).forward(request, response);
                return;
            }

            if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));

                Product product = productService.getProductById(id);

                if (product == null) {
                    response.sendRedirect(
                        request.getContextPath()
                        + "/admin/products?error=notfound"
                    );
                    return;
                }

                request.setAttribute("product", product);

                request.getRequestDispatcher(
                    "/admin/edit-product.jsp"
                ).forward(request, response);
                return;
            }

            List<Product> products = productService.getAllProducts();

            request.setAttribute("products", products);

            request.getRequestDispatcher(
                "/admin/products.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(
                request.getContextPath()
                + "/admin/products?error=invalidid"
            );
        }
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        if (!isAdminLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {

                Product product = createProduct(request);

                boolean success = productService.addProduct(product);

                response.sendRedirect(
                    request.getContextPath()
                    + (success
                        ? "/admin/products?success=added"
                        : "/admin/products?error=addfailed")
                );

            } else if ("update".equals(action)) {

                Product product = createProduct(request);

                int productId = Integer.parseInt(
                    request.getParameter("productId")
                );

                product.setProductId(productId);

                boolean success = productService.updateProduct(product);

                response.sendRedirect(
                    request.getContextPath()
                    + (success
                        ? "/admin/products?success=updated"
                        : "/admin/products?error=updatefailed")
                );

            } else if ("delete".equals(action)) {

                int productId = Integer.parseInt(
                    request.getParameter("productId")
                );

                boolean success =
                    productService.deleteProduct(productId);

                response.sendRedirect(
                    request.getContextPath()
                    + (success
                        ? "/admin/products?success=deleted"
                        : "/admin/products?error=deletefailed")
                );

            } else {

                response.sendRedirect(
                    request.getContextPath()
                    + "/admin/products?error=invalidaction"
                );
            }

        } catch (Exception e) {
            e.printStackTrace();

            response.sendRedirect(
                request.getContextPath()
                + "/admin/products?error=server"
            );
        }
    }

    private Product createProduct(HttpServletRequest request) {

        Product product = new Product();

        product.setCategoryId(
            Integer.parseInt(request.getParameter("categoryId"))
        );

        product.setProductName(
            request.getParameter("productName").trim()
        );

        product.setDescription(
            request.getParameter("description")
        );

        product.setPrice(
            new BigDecimal(request.getParameter("price"))
        );

        String discount = request.getParameter("discount");

        product.setDiscount(
            discount == null || discount.trim().isEmpty()
                ? BigDecimal.ZERO
                : new BigDecimal(discount)
        );

        product.setStock(
            Integer.parseInt(request.getParameter("stock"))
        );

        product.setImage(request.getParameter("image"));

        return product;
    }
}