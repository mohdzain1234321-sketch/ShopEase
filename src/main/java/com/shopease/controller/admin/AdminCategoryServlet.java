package com.shopease.controller.admin;

import java.io.IOException;
import java.util.List;

import com.shopease.model.Category;
import com.shopease.service.CategoryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private CategoryService categoryService;

    @Override
    public void init() {
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
            if ("edit".equals(action)) {

                int id = Integer.parseInt(request.getParameter("id"));

                Category category =
                    categoryService.getCategoryById(id);

                if (category == null) {
                    response.sendRedirect(
                        request.getContextPath()
                        + "/admin/categories?error=notfound"
                    );
                    return;
                }

                request.setAttribute("editCategory", category);
            }

            List<Category> categories =
                categoryService.getAllCategories();

            request.setAttribute("categories", categories);

            request.getRequestDispatcher(
                "/admin/categories.jsp"
            ).forward(request, response);

        } catch (NumberFormatException e) {
            response.sendRedirect(
                request.getContextPath()
                + "/admin/categories?error=invalidid"
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

            boolean success = false;

            if ("add".equals(action)) {

                Category category = new Category();

                category.setCategoryName(
                    request.getParameter("categoryName")
                );

                category.setDescription(
                    request.getParameter("description")
                );

                success = categoryService.addCategory(category);

            } else if ("update".equals(action)) {

                Category category = new Category();

                category.setCategoryId(
                    Integer.parseInt(request.getParameter("categoryId"))
                );

                category.setCategoryName(
                    request.getParameter("categoryName")
                );

                category.setDescription(
                    request.getParameter("description")
                );

                success = categoryService.updateCategory(category);

            } else if ("delete".equals(action)) {

                int id = Integer.parseInt(
                    request.getParameter("categoryId")
                );

                success = categoryService.deleteCategory(id);
            }

            String result = success
                    ? "success=completed"
                    : "error=operationfailed";

            response.sendRedirect(
                request.getContextPath()
                + "/admin/categories?" + result
            );

        } catch (Exception e) {
            e.printStackTrace();

            response.sendRedirect(
                request.getContextPath()
                + "/admin/categories?error=server"
            );
        }
    }
}