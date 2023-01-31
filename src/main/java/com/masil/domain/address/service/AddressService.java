package com.masil.domain.address.service;

import com.masil.domain.address.dto.request.AddressRequest;
import com.masil.domain.address.dto.request.AddressSearchRequest;
import com.masil.domain.address.dto.response.AddressResponse;
import com.masil.domain.address.dto.response.AddressSearchResponse;

import java.util.List;

public interface AddressService {

    List<AddressSearchResponse> search(AddressSearchRequest addressSearchRequest);

    AddressResponse getAddress(String addressId);
}
