package com.masil.domain.bookmark.controller;

import com.masil.common.annotation.ControllerMockApiTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest({
        BookmarkController.class,
})
class BookmarkControllerTest extends ControllerMockApiTest {

}