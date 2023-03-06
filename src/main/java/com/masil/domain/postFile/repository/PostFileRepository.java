package com.masil.domain.postFile.repository;

import com.masil.domain.postFile.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Long> {

    List<PostFile> findByIdIn(List<Long> ids);
}
