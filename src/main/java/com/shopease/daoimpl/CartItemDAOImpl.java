package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.CartItemDAO;
import com.shopease.model.CartItem;
import com.shopease.utility.DBConnection;

public class CartItemDAOImpl implements CartItemDAO {

    // 1. Add Cart Item
    @Override
    public boolean addCartItem(CartItem cartItem) {

        String sql = "INSERT INTO cart_items "
                + "(cart_id, product_id, variant_id, quantity) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getProductId());

            if (cartItem.getVariantId() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, cartItem.getVariantId());
            }

            ps.setInt(4, cartItem.getQuantity());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 2. Get Cart Item By ID
    @Override
    public CartItem getCartItemById(int cartItemId) {

        String sql = "SELECT * FROM cart_items WHERE cart_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartItemId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCartItem(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 3. Get All Cart Items
    @Override
    public List<CartItem> getAllCartItems() {

        List<CartItem> items = new ArrayList<>();

        String sql = "SELECT * FROM cart_items";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                items.add(extractCartItem(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // 4. Get Cart Items By Cart ID
    @Override
    public List<CartItem> getCartItemsByCartId(int cartId) {

        List<CartItem> items = new ArrayList<>();

        String sql = "SELECT * FROM cart_items WHERE cart_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(extractCartItem(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    // 5. Get Cart Item By Product and Variant
    @Override
    public CartItem getCartItemByProduct(
            int cartId, int productId, Integer variantId) {

        String sql;

        if (variantId == null) {
            sql = "SELECT * FROM cart_items "
                    + "WHERE cart_id = ? AND product_id = ? "
                    + "AND variant_id IS NULL";
        } else {
            sql = "SELECT * FROM cart_items "
                    + "WHERE cart_id = ? AND product_id = ? "
                    + "AND variant_id = ?";
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);
            ps.setInt(2, productId);

            if (variantId != null) {
                ps.setInt(3, variantId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractCartItem(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 6. Update Cart Item
    @Override
    public boolean updateCartItem(CartItem cartItem) {

        String sql = "UPDATE cart_items SET "
                + "cart_id = ?, product_id = ?, variant_id = ?, quantity = ? "
                + "WHERE cart_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartItem.getCartId());
            ps.setInt(2, cartItem.getProductId());

            if (cartItem.getVariantId() == null) {
                ps.setNull(3, java.sql.Types.INTEGER);
            } else {
                ps.setInt(3, cartItem.getVariantId());
            }

            ps.setInt(4, cartItem.getQuantity());
            ps.setInt(5, cartItem.getCartItemId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 7. Update Quantity
    @Override
    public boolean updateQuantity(int cartItemId, int quantity) {

        String sql = "UPDATE cart_items SET quantity = ? "
                + "WHERE cart_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 8. Delete Cart Item
    @Override
    public boolean deleteCartItem(int cartItemId) {

        String sql = "DELETE FROM cart_items WHERE cart_item_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartItemId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 9. Clear Cart
    @Override
    public boolean clearCart(int cartId) {

        String sql = "DELETE FROM cart_items WHERE cart_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 10. Get Cart Item Count
    @Override
    public int getCartItemCount(int cartId) {

        String sql = "SELECT COALESCE(SUM(quantity), 0) "
                + "AS total_items FROM cart_items WHERE cart_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total_items");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Extract Cart Item From ResultSet
    private CartItem extractCartItem(ResultSet rs) throws SQLException {

        CartItem item = new CartItem();

        item.setCartItemId(rs.getInt("cart_item_id"));
        item.setCartId(rs.getInt("cart_id"));
        item.setProductId(rs.getInt("product_id"));

        int variantId = rs.getInt("variant_id");

        if (rs.wasNull()) {
            item.setVariantId(null);
        } else {
            item.setVariantId(variantId);
        }

        item.setQuantity(rs.getInt("quantity"));

        return item;
    }
}