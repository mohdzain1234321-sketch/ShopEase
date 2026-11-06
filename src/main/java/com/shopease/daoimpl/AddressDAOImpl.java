package com.shopease.daoimpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shopease.dao.AddressDAO;
import com.shopease.model.Address;
import com.shopease.utility.DBConnection;

public class AddressDAOImpl implements AddressDAO {

    // 1. Add Address
    @Override
    public boolean addAddress(Address address) {

        String sql = "INSERT INTO addresses "
                + "(user_id, address_type, full_name, phone, address_line, "
                + "city, state, postal_code, country, is_default) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, address.getUserId());
            ps.setString(2, address.getAddressType());
            ps.setString(3, address.getFullName());
            ps.setString(4, address.getPhone());
            ps.setString(5, address.getAddressLine());
            ps.setString(6, address.getCity());
            ps.setString(7, address.getState());
            ps.setString(8, address.getPostalCode());
            ps.setString(9, address.getCountry());
            ps.setBoolean(10, address.isDefault());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 2. Get Address By ID
    @Override
    public Address getAddressById(int addressId) {

        String sql = "SELECT * FROM addresses WHERE address_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, addressId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractAddress(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 3. Get All Addresses
    @Override
    public List<Address> getAllAddresses() {

        List<Address> addresses = new ArrayList<>();

        String sql = "SELECT * FROM addresses";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                addresses.add(extractAddress(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addresses;
    }

    // 4. Get Addresses By User ID
    @Override
    public List<Address> getAddressesByUserId(int userId) {

        List<Address> addresses = new ArrayList<>();

        String sql = "SELECT * FROM addresses WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    addresses.add(extractAddress(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return addresses;
    }

    // 5. Get Default Address
    @Override
    public Address getDefaultAddress(int userId) {

        String sql = "SELECT * FROM addresses "
                + "WHERE user_id = ? AND is_default = TRUE";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return extractAddress(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // 6. Update Address
    @Override
    public boolean updateAddress(Address address) {

        String sql = "UPDATE addresses SET "
                + "address_type = ?, full_name = ?, phone = ?, "
                + "address_line = ?, city = ?, state = ?, "
                + "postal_code = ?, country = ?, is_default = ? "
                + "WHERE address_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, address.getAddressType());
            ps.setString(2, address.getFullName());
            ps.setString(3, address.getPhone());
            ps.setString(4, address.getAddressLine());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getState());
            ps.setString(7, address.getPostalCode());
            ps.setString(8, address.getCountry());
            ps.setBoolean(9, address.isDefault());
            ps.setInt(10, address.getAddressId());
            ps.setInt(11, address.getUserId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 7. Set Default Address
    @Override
    public boolean setDefaultAddress(int addressId, int userId) {

        String resetSql = "UPDATE addresses SET is_default = FALSE "
                + "WHERE user_id = ?";

        String updateSql = "UPDATE addresses SET is_default = TRUE "
                + "WHERE address_id = ? AND user_id = ?";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try (PreparedStatement resetPs = con.prepareStatement(resetSql);
                 PreparedStatement updatePs = con.prepareStatement(updateSql)) {

                resetPs.setInt(1, userId);
                resetPs.executeUpdate();

                updatePs.setInt(1, addressId);
                updatePs.setInt(2, userId);

                int rows = updatePs.executeUpdate();

                if (rows > 0) {
                    con.commit();
                    return true;
                } else {
                    con.rollback();
                    return false;
                }

            } catch (SQLException e) {
                con.rollback();
                throw e;
            } finally {
                con.setAutoCommit(true);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 8. Delete Address
    @Override
    public boolean deleteAddress(int addressId) {

        String sql = "DELETE FROM addresses WHERE address_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, addressId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Extract Address From ResultSet
    private Address extractAddress(ResultSet rs) throws SQLException {

        Address address = new Address();

        address.setAddressId(rs.getInt("address_id"));
        address.setUserId(rs.getInt("user_id"));
        address.setAddressType(rs.getString("address_type"));
        address.setFullName(rs.getString("full_name"));
        address.setPhone(rs.getString("phone"));
        address.setAddressLine(rs.getString("address_line"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setPostalCode(rs.getString("postal_code"));
        address.setCountry(rs.getString("country"));
        address.setDefault(rs.getBoolean("is_default"));

        return address;
    }
}