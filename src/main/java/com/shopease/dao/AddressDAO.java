package com.shopease.dao;

import java.util.List;
import com.shopease.model.Address;

public interface AddressDAO {

    boolean addAddress(Address address);

    Address getAddressById(int addressId);

    List<Address> getAllAddresses();

    List<Address> getAddressesByUserId(int userId);

    Address getDefaultAddress(int userId);

    boolean updateAddress(Address address);

    boolean setDefaultAddress(int addressId, int userId);

    boolean deleteAddress(int addressId);
}