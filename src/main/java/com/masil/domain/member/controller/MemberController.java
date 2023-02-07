package com.masil.domain.member.controller;

import com.masil.domain.member.dto.request.MemberAddressRequest;
import com.masil.domain.member.service.MemberService;
import com.masil.global.auth.annotaion.LoginUser;
import com.masil.global.auth.dto.response.CurrentMember;
import com.masil.global.auth.dto.response.LoginMemberInfoResponse;
import com.masil.global.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

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

    @PutMapping("/{memberId}/address")
    public void modifyMemberAddress(@PathVariable Long memberId,
                                    @RequestBody MemberAddressRequest request) {
        memberService.modifyMemberAddress(memberId, request);
    }

    @GetMapping("/login-user")
    public LoginMemberInfoResponse getMemberInfo(@LoginUser CurrentMember member) {
        return authService.getMemberInfo(member);
    }

}
