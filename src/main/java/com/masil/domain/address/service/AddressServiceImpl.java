package com.masil.domain.address.service;

import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.address.repository.SggAddressRepository;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressServiceImpl implements AddressService {

    public static final int MINUM_SEARCH_KEYWORD_LENGTH = 2;
    private final EmdAddressRepository emdAddressRepository;
    private final SggAddressRepository sggAddressRepository;


    @Override
    public List<AddressSearchResponse> search(String keyword) {
        if (validateKeyword(keyword)) {
            List<AddressSearchResponse> searchResult = searchBySggAddress(keyword);
            searchResult.addAll(searchByEmdAddress(keyword));
            return searchResult;
        } else {
            throw new BusinessException(ErrorCode.INVALID_ADDRESS_SEARCH_KEYWORD);
        }
    }

    private boolean validateKeyword(String keyword) {
        return keyword.length() >= MINUM_SEARCH_KEYWORD_LENGTH;
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
}
