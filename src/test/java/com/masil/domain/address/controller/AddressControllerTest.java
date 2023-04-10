package com.masil.domain.address.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import com.masil.domain.address.dto.response.AddressSearchResponse;
import com.masil.domain.address.service.AddressService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({
        AddressController.class,
})
@AutoConfigureMockMvc(addFilters = false)
class AddressControllerTest extends ControllerMockApiTest {

    @MockBean
    private AddressService addressService;

    @Test
    @DisplayName("시군구 데이터만 검색되는 경우")
    @WithAnonymousUser
    void searchAddressSuccessBySggId () throws Exception {

        //given
        String keyword = "강남구";
        List<AddressSearchResponse> result = new ArrayList<>();
        AddressSearchResponse resultData = AddressSearchResponse.builder()
                .rCode(11680)
                .rName("서울특별시 강남구")
                .emdId(null)
                .sggId(11680)
                .sidoId(1)
                .sidoName("서울특별시")
                .sggName("강남구")
                .emdName(null)
                .isEmdAddress(false)
                .build();
        result.add(resultData);
        given(addressService.search(keyword)).willReturn(result);

        //expected
        String json = objectMapper.writeValueAsString(result);
        mockMvc.perform(get("/api/guest-available/addresses/search")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(content().json(json))
                .andDo(print());
    }
}