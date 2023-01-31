package com.masil.domain.address.controller;

import com.masil.domain.address.dto.request.AddressSearchRequest;
import com.masil.domain.address.dto.response.AddressResponse;
import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @GetMapping("/search")
    public List<AddressSearchResponse> search(AddressSearchRequest searchRequest) {
        return addressService.search(searchRequest);
    }

    @GetMapping("/{addressId}")
    public AddressResponse getAddress(@PathVariable String addressId) {
        return addressService.getAddress(addressId);
    }

}
