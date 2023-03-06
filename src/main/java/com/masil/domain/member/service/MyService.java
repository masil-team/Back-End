package com.masil.domain.member.service;

import com.masil.domain.member.dto.request.MyFindRequest;
import com.masil.domain.post.dto.PostsResponse;
import org.springframework.data.domain.Pageable;

public interface MyService {

    PostsResponse findMyPosts(MyFindRequest request, Pageable pageable);


}
