package com.masil.domain.address.service;

import com.masil.domain.address.dto.response.AddressResponse;
import com.masil.domain.address.dto.response.AddressSearchResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Override
    public List<AddressSearchResponse> search(String search) {

        return null;
    }

    @Override
    public AddressResponse getAddress(String addressId) {
        return null;
    }
}
