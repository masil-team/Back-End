package com.masil.domain.address.service;

import com.masil.common.annotation.ServiceTest;
import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.repository.EmdAddressRepository;
import com.masil.domain.address.repository.SggAddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Test
    @DisplayName("주소 검색 검색어에 시군구와 읍면동이 같이 검색될 경우 성공")
    void searchAddressSuccessWhenBothSggAddressAndEmdAddress() throws Exception {
        //given
        String search = "마포";
        //when
        List<AddressSearchResponse> searchResult = addressService.search(search);

        //then
        assertEquals(2,searchResult.size());
    }

    @Test
    @DisplayName("주소 검색이 시군구만 검색될 경우 성공")
    void searchAddressSuccessWhenSggAddress () throws Exception {
        //given
        String search = "강남";
        //when
        List<AddressSearchResponse> result = addressService.search(search);

        //then
        assertEquals(1, result.size());
        assertEquals("강남구", result.get(0).getSggName());
    }
}