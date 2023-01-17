package com.masil.domain.post.service;

import com.masil.domain.post.dto.PostCreateRequest;
import com.masil.domain.post.dto.PostModifyRequest;

import com.masil.domain.post.dto.PostResponse;
import com.masil.domain.post.dto.PostsResponse;
import com.masil.domain.post.entity.Post;
import com.masil.domain.post.exception.PostNotFoundException;
import com.masil.domain.post.repository.PostRepository;
import com.masil.domain.user.entity.User;
import com.masil.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostResponse findPost(Long postId) {
        Post post = findPostById(postId);

        return PostResponse.from(post);
    }

    public PostsResponse findAllPost() {
        List<Post> posts = postRepository.findAll();
        return PostsResponse.ofPosts(posts);
    }

    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest, Long userId){
        User user = findUserById(userId);
        Post post = postCreateRequest.toEntity(user);
        return postRepository.save(post).getId();
    }

    @Transactional
    public void modifyPost(Long postId, PostModifyRequest postModifyRequest, Long userId){
        Post post = findPostById(postId);
        findUserById(userId);

        post.updateContent(postModifyRequest.getContent());
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = findPostById(postId);
        findUserById(userId);

        post.tempDelete();
    }

    // 예외 처리
    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(RuntimeException::new);
    }


}
