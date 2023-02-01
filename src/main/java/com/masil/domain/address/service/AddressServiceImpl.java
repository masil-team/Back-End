package com.masil.domain.address.service;

import com.masil.domain.address.dto.response.AddressResponse;
import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.address.repository.SggAddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final EmdAddressRepository emdAddressRepository;
    private final SggAddressRepository sggAddressRepository;


    @Override
    public List<AddressSearchResponse> search(String search) {

        List<AddressSearchResponse> searchResult = searchBySggAddress(search);
        searchResult.addAll(searchByEmdAddress(search));

        return searchResult;
    }

    private List<AddressSearchResponse> searchBySggAddress(String search) {
        return sggAddressRepository.findBySggNameContains(search)
                .stream()
                .map(AddressSearchResponse::of)
                .collect(Collectors.toList());
    }

    private List<AddressSearchResponse> searchByEmdAddress(String search) {
        return emdAddressRepository.findByEmdNameContains(search)
                .stream()
                .map(AddressSearchResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public AddressResponse getAddress(String addressId) {
        return null;
    }
}