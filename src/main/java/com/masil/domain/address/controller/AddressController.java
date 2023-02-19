package com.masil.domain.address.controller;

import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/search")
    public List<AddressSearchResponse> search(String keyword) {
        return addressService.search(keyword);
    }
}
