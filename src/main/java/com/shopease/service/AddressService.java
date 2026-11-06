package com.shopease.service;

import java.util.List;

import com.shopease.dao.AddressDAO;
import com.shopease.daoimpl.AddressDAOImpl;
import com.shopease.model.Address;

public class AddressService {
private AddressDAO addressDAO = new AddressDAOImpl();

// 1. Add Address
public boolean addAddress(Address address) {

    if (address == null || address.getUserId() <= 0) {
        return false;
    }

    if (address.getFullName() == null
            || address.getFullName().trim().isEmpty()
            || address.getPhone() == null
            || address.getPhone().trim().isEmpty()
            || address.getAddressLine() == null
            || address.getAddressLine().trim().isEmpty()
            || address.getCity() == null
            || address.getCity().trim().isEmpty()
            || address.getState() == null
            || address.getState().trim().isEmpty()
            || address.getPostalCode() == null
            || address.getPostalCode().trim().isEmpty()
            || address.getCountry() == null
            || address.getCountry().trim().isEmpty()) {
        return false;
    }

    return addressDAO.addAddress(address);
}

// 2. Get Address By ID
public Address getAddressById(int addressId) {

    if (addressId <= 0) {
        return null;
    }

    return addressDAO.getAddressById(addressId);
}

// 3. Get All Addresses
public List<Address> getAllAddresses() {

    return addressDAO.getAllAddresses();
}

// 4. Get Addresses By User ID
public List<Address> getAddressesByUserId(int userId) {

    if (userId <= 0) {
        return null;
    }

    return addressDAO.getAddressesByUserId(userId);
}

// 5. Get Default Address
public Address getDefaultAddress(int userId) {

    if (userId <= 0) {
        return null;
    }

    return addressDAO.getDefaultAddress(userId);
}

// 6. Update Address
public boolean updateAddress(Address address) {

    if (address == null
            || address.getAddressId() <= 0
            || address.getUserId() <= 0) {
        return false;
    }

    if (address.getFullName() == null
            || address.getFullName().trim().isEmpty()
            || address.getPhone() == null
            || address.getPhone().trim().isEmpty()
            || address.getAddressLine() == null
            || address.getAddressLine().trim().isEmpty()
            || address.getCity() == null
            || address.getCity().trim().isEmpty()
            || address.getState() == null
            || address.getState().trim().isEmpty()
            || address.getPostalCode() == null
            || address.getPostalCode().trim().isEmpty()
            || address.getCountry() == null
            || address.getCountry().trim().isEmpty()) {
        return false;
    }

    Address existingAddress =
            addressDAO.getAddressById(address.getAddressId());

    if (existingAddress == null
            || existingAddress.getUserId() != address.getUserId()) {
        return false;
    }

    return addressDAO.updateAddress(address);
}

// 7. Set Default Address
public boolean setDefaultAddress(int addressId, int userId) {

    if (addressId <= 0 || userId <= 0) {
        return false;
    }

    Address address = addressDAO.getAddressById(addressId);

    if (address == null || address.getUserId() != userId) {
        return false;
    }

    return addressDAO.setDefaultAddress(addressId, userId);
}

// 8. Delete Address
public boolean deleteAddress(int addressId, int userId) {

    if (addressId <= 0 || userId <= 0) {
        return false;
    }

    Address address = addressDAO.getAddressById(addressId);

    if (address == null || address.getUserId() != userId) {
        return false;
    }

    return addressDAO.deleteAddress(addressId);
}


}
