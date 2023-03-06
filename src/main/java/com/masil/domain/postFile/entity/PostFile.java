package com.masil.domain.postFile.entity;

import com.masil.domain.post.entity.Post;
import com.masil.global.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostFile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String name;         // s3에 저장된 이름

    @Column(nullable = false)
    private String originName;   // 원래 이름

    private int byteSize;

    private int width;

    private int height;

    @Builder
    public PostFile(Post post, String name, String originName, int byteSize, int width, int height) {
        this.post = post;
        this.name = name;
        this.originName = originName;
        this.byteSize = byteSize;
        this.width = width;
        this.height = height;
    }

    public void addPost(Post post) {
        this.post = post;
    }
}
