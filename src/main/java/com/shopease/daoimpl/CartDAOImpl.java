package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.CartDAO;
import com.shopease.model.Cart;
import com.shopease.utility.DBConnection;

public class CartDAOImpl implements CartDAO {

    // 1. Add Cart
    @Override
    public boolean addCart(Cart cart) {

        String sql = "INSERT INTO cart (user_id) VALUES (?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cart.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 2. Get Cart By ID
    @Override
    public Cart getCartById(int cartId) {

        String sql = "SELECT * FROM cart WHERE cart_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractCart(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 3. Get Cart By User ID
    @Override
    public Cart getCartByUserId(int userId) {

        String sql = "SELECT * FROM cart WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractCart(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 4. Get All Carts
    @Override
    public List<Cart> getAllCarts() {

        List<Cart> carts = new ArrayList<>();

        String sql = "SELECT * FROM cart";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                carts.add(extractCart(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return carts;
    }

    // 5. Update Cart
    @Override
    public boolean updateCart(Cart cart) {

        String sql = "UPDATE cart SET user_id = ? WHERE cart_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cart.getUserId());
            ps.setInt(2, cart.getCartId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 6. Delete Cart By ID
    @Override
    public boolean deleteCart(int cartId) {

        String sql = "DELETE FROM cart WHERE cart_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cartId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 7. Delete Cart By User ID
    @Override
    public boolean deleteCartByUserId(int userId) {

        String sql = "DELETE FROM cart WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 8. Check Cart Exists
    @Override
    public boolean cartExists(int userId) {

        String sql = "SELECT 1 FROM cart WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Extract Cart From ResultSet
    private Cart extractCart(ResultSet rs) throws SQLException {

        Cart cart = new Cart();

        cart.setCartId(rs.getInt("cart_id"));
        cart.setUserId(rs.getInt("user_id"));
        cart.setCreatedAt(rs.getTimestamp("created_at"));

        return cart;
    }
}