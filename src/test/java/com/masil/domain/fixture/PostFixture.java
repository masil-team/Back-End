package com.masil.domain.fixture;

import com.masil.domain.address.entity.EmdAddress;
import com.masil.domain.board.entity.Board;
import com.masil.domain.member.entity.Member;
import com.masil.domain.post.entity.Post;

import java.util.ArrayList;
import java.util.List;

public enum PostFixture {
    일반_게시글_JJ(MemberFixture.일반_회원_JJ.엔티티_생성(), "일반 게시글 내용", BoardFixture.전체_카테고리.엔티티_생성()),
    일반_게시글_KK(MemberFixture.일반_회원_KK.엔티티_생성(), "일반 게시글 내용", BoardFixture.전체_카테고리.엔티티_생성())
    ;

    private final Member member;
    private final String content;
    private final Board board;

    PostFixture(Member member, String content, Board board) {
        this.member = member;
        this.content = content;
        this.board = board;
    }

    // TODO : emd 파라미터로 받는 거 나중에 수정 02-11
    public Post 엔티티_생성(EmdAddress emdAddress) {

        return Post.builder()
                .content(this.content)
                .member(this.member)
                .board(this.board)
                .emdAddress(emdAddress)
                .build();
    }
    public Post 엔티티_생성() {
        // 카테고리 필요없는 테스트에 사용
        return Post.builder()
                .content(this.content)
                .member(this.member)
//                .board(this.board)
                .build();
    }

    public List<Post> 엔티티_여러개_생성(EmdAddress emdAddress) {
        List<Post> posts = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            posts.add(
                    Post.builder()
                            .content(this.content)
                            .member(this.member)
                            .board(this.board)
                            .emdAddress(emdAddress)
                            .build()
            );
        }
        return posts;
    }

    public Member 멤버() {
        return this.member;
    }
    public String 내용() {
        return this.content;
    }
    public Board 카테고리() {
        return this.board;
    }
}
