package com.shopease.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.shopease.model.Product;
import com.shopease.service.ProductService;
import com.shopease.service.RelatedProductsService;

@WebServlet("/related-products")
public class RelatedProductsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductService productService;
    private RelatedProductsService relatedProductsService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        relatedProductsService = new RelatedProductsService();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        int productId;

        try {
            productId = Integer.parseInt(
                request.getParameter("productId")
            );
        } catch (NumberFormatException e) {
            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Invalid product ID."
            );
            return;
        }

        Product currentProduct =
                productService.getProductById(productId);

        if (currentProduct == null) {
            response.sendError(
                HttpServletResponse.SC_NOT_FOUND,
                "Product not found."
            );
            return;
        }

        List<Product> relatedProducts =
                relatedProductsService.getRelatedProducts(
                    currentProduct.getProductId(),
                    currentProduct.getCategoryId()
                );

        request.setAttribute(
            "relatedProducts", relatedProducts
        );

        request.getRequestDispatcher(
            "/views/related-products.jsp"
        ).forward(request, response);
    }
}