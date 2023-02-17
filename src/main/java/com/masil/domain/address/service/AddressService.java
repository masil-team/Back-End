package com.masil.domain.address.service;

import com.masil.domain.address.dto.response.AddressSearchResponse;

import java.util.List;

public interface AddressService {

    List<AddressSearchResponse> search(String keyword);
}
