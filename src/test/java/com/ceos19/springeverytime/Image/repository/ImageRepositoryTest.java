package com.ceos19.springeverytime.Image.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.ceos19.springeverytime.Image.domain.Image;
import com.ceos19.springeverytime.post.domain.Post;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ImageRepositoryTest {

    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private EntityManager em;

    @Test
    void FindOne() {
        //given
        Image image = Image.builder()
                .build();
        //when
        imageRepository.save(image);
        //then
        Image result = imageRepository.findById(image.getId()).orElseThrow(IllegalStateException::new);
        assertEquals(image, result);
    }

    @Test
    void findAll() {
        //given
        Image image1 = Image.builder()
                .build();
        Image image2 = Image.builder()
                .build();
        //when
        imageRepository.save(image1);
        imageRepository.save(image2);
        List<Image> allImages = imageRepository.findAll();
        //then
        assertEquals(2, allImages.size());
    }

    @Test
    void findByPost() {
        //given
        Post post = Post.builder().build();
        em.persist(post);
        Image image1 = Image.builder()
                .post(post)
                .build();
        Image image2 = Image.builder()
                .post(post)
                .build();
        //when
        imageRepository.save(image1);
        imageRepository.save(image2);
        //then
        List<Image> allImagesByPost = imageRepository.findByPostId(post.getId());

        assertEquals(2, allImagesByPost.size());
    }
}