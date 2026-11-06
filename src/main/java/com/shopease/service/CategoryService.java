package com.shopease.service;

import java.util.List;

import com.shopease.dao.CategoryDAO;
import com.shopease.daoimpl.CategoryDAOImpl;
import com.shopease.model.Category;

public class CategoryService {


private CategoryDAO categoryDAO = new CategoryDAOImpl();

// 1. Add Category
public boolean addCategory(Category category) {

    if (category == null
            || category.getCategoryName() == null
            || category.getCategoryName().trim().isEmpty()) {
        return false;
    }

    String categoryName = category.getCategoryName().trim();

    if (categoryDAO.categoryExists(categoryName)) {
        return false;
    }

    category.setCategoryName(categoryName);

    return categoryDAO.addCategory(category);
}

// 2. Get Category By ID
public Category getCategoryById(int categoryId) {

    if (categoryId <= 0) {
        return null;
    }

    return categoryDAO.getCategoryById(categoryId);
}

// 3. Get All Categories
public List<Category> getAllCategories() {

    return categoryDAO.getAllCategories();
}

// 4. Update Category
public boolean updateCategory(Category category) {

    if (category == null || category.getCategoryId() <= 0
            || category.getCategoryName() == null
            || category.getCategoryName().trim().isEmpty()) {
        return false;
    }

    String categoryName = category.getCategoryName().trim();

    Category existingCategory =
            categoryDAO.getCategoryById(category.getCategoryId());

    if (existingCategory == null) {
        return false;
    }

    if (!existingCategory.getCategoryName().equalsIgnoreCase(categoryName)
            && categoryDAO.categoryExists(categoryName)) {
        return false;
    }

    category.setCategoryName(categoryName);

    return categoryDAO.updateCategory(category);
}

// 5. Delete Category
public boolean deleteCategory(int categoryId) {

    if (categoryId <= 0) {
        return false;
    }

    return categoryDAO.deleteCategory(categoryId);
}

// 6. Check Category Exists
public boolean categoryExists(String categoryName) {

    if (categoryName == null || categoryName.trim().isEmpty()) {
        return false;
    }

    return categoryDAO.categoryExists(categoryName.trim());
}


}
