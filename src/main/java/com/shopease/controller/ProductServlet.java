
package com.shopease.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.shopease.model.Product;
import com.shopease.service.ProductService;

@WebServlet(urlPatterns = {
        "/products",
        "/product-details",
        "/search-suggestions"
})
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService;

    private static final int PAGE_SIZE = 8;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String servletPath = request.getServletPath();

        // SEARCH AUTOCOMPLETE
        if ("/search-suggestions".equals(servletPath)) {

            String keyword = request.getParameter("q");

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            if (keyword == null || keyword.trim().isEmpty()) {
                response.getWriter().write("[]");
                return;
            }

            List<Product> suggestions =
                    productService.getProductSuggestions(keyword.trim());

            StringBuilder json = new StringBuilder("[");
            boolean first = true;

            for (Product product : suggestions) {

                if (!first) {
                    json.append(",");
                }

                json.append("{")
                    .append("\"productId\":")
                    .append(product.getProductId())
                    .append(",")
                    .append("\"productName\":\"")
                    .append(escapeJson(product.getProductName()))
                    .append("\"")
                    .append("}");

                first = false;
            }

            json.append("]");

            PrintWriter out = response.getWriter();
            out.write(json.toString());
            out.flush();

            return;
        }

        // PRODUCT DETAILS
        if ("/product-details".equals(servletPath)) {

            try {
                int productId = Integer.parseInt(
                        request.getParameter("id")
                );

                Product product = productService.getProductById(productId);

                if (product == null) {
                    response.sendError(404, "Product not found.");
                    return;
                }

                request.setAttribute("product", product);

                request.getRequestDispatcher(
                        "/views/product-details.jsp"
                ).forward(request, response);

            } catch (NumberFormatException e) {
                response.sendError(400, "Invalid product ID.");
            }

            return;
        }

        // SEARCH
        String keyword = request.getParameter("keyword");

        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = request.getParameter("q");
        }

        // CATEGORY
        String categoryParameter = request.getParameter("categoryId");

        // PRICE FILTERS
        String minPriceParameter = request.getParameter("minPrice");
        String maxPriceParameter = request.getParameter("maxPrice");

        // SORTING
        String sort = request.getParameter("sort");

        if (sort == null || sort.trim().isEmpty()) {
            sort = "newest";
        }

        // TODAY'S DEALS
        boolean discountOnly =
                "on".equalsIgnoreCase(request.getParameter("discountOnly"))
                || "true".equalsIgnoreCase(request.getParameter("discountOnly"))
                || "deals".equalsIgnoreCase(sort);

        int currentPage = 1;

        try {

            // PAGE
            String pageParameter = request.getParameter("page");

            if (pageParameter != null && !pageParameter.trim().isEmpty()) {
                currentPage = Integer.parseInt(pageParameter);
            }

            if (currentPage < 1) {
                currentPage = 1;
            }

            Integer categoryId = null;
            BigDecimal minPrice = null;
            BigDecimal maxPrice = null;

            // CATEGORY CONVERSION
            if (categoryParameter != null
                    && !categoryParameter.trim().isEmpty()) {

                categoryId = Integer.parseInt(categoryParameter);
            }

            // MINIMUM PRICE
            if (minPriceParameter != null
                    && !minPriceParameter.trim().isEmpty()) {

                minPrice = new BigDecimal(minPriceParameter);
            }

            // MAXIMUM PRICE
            if (maxPriceParameter != null
                    && !maxPriceParameter.trim().isEmpty()) {

                maxPrice = new BigDecimal(maxPriceParameter);
            }

            // VALIDATE SORTING
            if (!"priceLow".equals(sort)
                    && !"priceHigh".equals(sort)
                    && !"newest".equals(sort)
                    && !"deals".equals(sort)) {

                sort = "newest";
            }

            // COUNT PRODUCTS
            int totalProducts = productService.countFilteredProducts(
                    keyword,
                    categoryId,
                    minPrice,
                    maxPrice,
                    discountOnly
            );

            // TOTAL PAGES
            int totalPages = (int) Math.ceil(
                    (double) totalProducts / PAGE_SIZE
            );

            if (totalPages == 0) {
                currentPage = 1;
            } else if (currentPage > totalPages) {
                currentPage = totalPages;
            }

            // DATABASE OFFSET
            int offset = (currentPage - 1) * PAGE_SIZE;

            // FETCH PRODUCTS
            List<Product> products = productService.getPaginatedProducts(
                    keyword,
                    categoryId,
                    minPrice,
                    maxPrice,
                    discountOnly,
                    sort,
                    offset,
                    PAGE_SIZE
            );

            // SEND DATA TO JSP
            request.setAttribute("products", products);
            request.setAttribute("keyword", keyword);
            request.setAttribute("categoryId", categoryParameter);
            request.setAttribute("minPrice", minPriceParameter);
            request.setAttribute("maxPrice", maxPriceParameter);
            request.setAttribute("discountOnly", discountOnly);
            request.setAttribute("sort", sort);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalProducts", totalProducts);

            request.getRequestDispatcher(
                    "/views/products.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {

            response.sendError(
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Please enter valid page, category and price values."
            );
        }
    }

    // ESCAPE SPECIAL CHARACTERS FOR JSON
    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
