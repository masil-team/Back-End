package com.masil.domain.member.service;

import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.post.dto.PostsResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MyServiceImpl implements MyService{

    @Override
    public PostsResponse findMyPosts(MyFindRequest request, Pageable pageable) {
        return null;
    }
}
