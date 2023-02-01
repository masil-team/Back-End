package com.masil.domain.address.controller;

import com.masil.domain.address.dto.response.AddressResponse;
import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.service.AddressService;
import com.masil.domain.member.service.MemberService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;
    private final MemberService memberService;

    @GetMapping("/search")
    public List<AddressSearchResponse> search(@NotBlank String keyword) {
        return addressService.search(keyword);
    }

    @GetMapping("/{addressId}")
    public AddressResponse getAddress(@PathVariable String addressId,
                                      @LoginUser CurrentMember currentMember) {
        if (Objects.isNull(currentMember)) {
            return addressService.getAddress(addressId);
        } else {
            return addressService.getAddress(addressId);
        }
    }

}
