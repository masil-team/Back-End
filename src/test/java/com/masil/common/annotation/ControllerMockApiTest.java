package com.masil.common.annotation;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.masil.common.security.WithMockCustomUser;
import com.masil.global.auth.jwt.provider.JwtTokenProvider;
import com.masil.global.config.security.SecurityConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;


@AutoConfigureRestDocs
@ExtendWith(MockitoExtension.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityConfig.class)
@WithMockCustomUser // 유저 인증 정보
public class ControllerMockApiTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;
    @MockBean
    protected UserDetailsService userDetailsService;
}
