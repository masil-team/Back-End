package com.masil.domain.address.service;

import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.address.repository.EmdAddressRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @Autowired
    private EmdAddressRepository emdAddressRepository;

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

    @Test
    @DisplayName("시군구Id 로 여러 읍면동 id 가져오기 테스트")
    void getEmdIdsBySggId () throws Exception {
        //given
        String search = "강남구";

        //when
        List<AddressSearchResponse> result = addressService.search(search);
        Long sggId = result.get(0).getSggId();
        List<EmdAddress> emdAddresses = emdAddressRepository.findAllBySggAddress_Id(sggId);

        System.out.println(emdAddresses);
        //then
    }
}