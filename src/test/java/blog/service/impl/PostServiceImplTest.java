package blog.service.impl;

import blog.entity.Category;
import blog.entity.Post;
import blog.repository.CategoryRepository;
import blog.repository.PostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceImplTest {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category category = createCategory(null, "David");
        categoryRepository.save(category);
        Post post = createPost(1, "David", category, "David", "David", "david");
        System.out.println(post);
        Post post1 = createPost(2, "Uros", category, "Uros", "Uros", "uros");

        postRepository.saveAll(List.of(post, post1));
    }

    @AfterEach
    void tearDown() {
        postRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void findAll() {
        List<Post> posts = postRepository.findAll();
        assertEquals(2, posts.size());
    }

    @Test
    void save() {
        Category category = createCategory(null, "Blabla");
        categoryRepository.save(category);

        Post post = createPost(null, "David", category, "David", "David", "david");
        Post post1 = postRepository.save(post);
        assertEquals(post, post1);
    }

    private static Post createPost(Integer postId, String title, Category category, String excerpt, String body, String slug) {
        Post post = new Post();
        return post.createPostForTesting(postId, title, category, excerpt, body, slug);
    }

    private static Category createCategory(Integer categoryId, String name) {
        return new Category(categoryId, name);
    }
}