package com.masil.domain.address.service;

import com.masil.domain.address.dto.request.AddressSearchRequest;
import com.masil.domain.address.dto.response.AddressSearchResponse;

import java.util.List;

public interface AddressSearchService {

    List<AddressSearchResponse> search(AddressSearchRequest addressSearchRequest);
}
