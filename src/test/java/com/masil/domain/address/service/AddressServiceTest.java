package com.masil.domain.address.service;

import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.global.error.exception.BusinessException;
import com.masil.global.error.exception.ErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        String keyword = "마포";
        //when
        List<AddressSearchResponse> searchResult = addressService.search(keyword);

        //then
        assertEquals(2,searchResult.size());
    }

    @Test
    @DisplayName("주소 검색이 시군구만 검색될 경우 성공")
    void searchAddressSuccessWhenSggAddress () throws Exception {
        //given
        String keyword = "강남";
        //when
        List<AddressSearchResponse> result = addressService.search(keyword);

        result.stream().forEach( t -> System.out.println(t.toString()));
        //then
        assertEquals(1, result.size());
        assertEquals("강남구", result.get(0).getSggName());
    }

    @Test
    @DisplayName("주소 검색 키워드가 2자리 이하일 때 실패")
    void searchFailBecauseInvalidKeywordLength () throws Exception {
        //given
        String keyword = "강";

        //when
        BusinessException e =
                assertThrows(BusinessException.class,() -> addressService.search(keyword));
        assertEquals(ErrorCode.INVALID_ADDRESS_SEARCH_KEYWORD,e.getErrorCode());
    }
}