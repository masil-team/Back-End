package com.masil.domain.member.controller;

import com.masil.domain.member.dto.request.MemberCreateRequest;
import com.masil.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public void signUp(@Valid @RequestBody MemberCreateRequest createRequest) {
        memberService.signUp(createRequest);
    }

    @GetMapping("/{memberId}")
    public void findMember() {
        memberService.getMyUser();
    }

    @PatchMapping("/{memberId}")
    public void modifyUser() {
        memberService.modifyUser();
    }

    @DeleteMapping("/{memberId}")
    public void modifyUserToDeleteState() {
        memberService.modifyUserToDeleteState();
    }

}
