package blog.service.impl;

import blog.entity.Category;
import blog.entity.Post;
import blog.repository.CategoryRepository;
import blog.repository.PostRepository;
import blog.service.CategoryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        Category category = createCategory(1, "David");
        Category category1 = createCategory(2, "Uros");

        List<Category> categoryList = new ArrayList<>(List.of(category1, category));
        categoryRepository.saveAll(categoryList);
    }

    @AfterEach
    void tearDown() {
        categoryRepository.deleteAll();
    }

    @Test
    void findAll() {
        List<Category> categories = categoryRepository.findAll();
        assertEquals(2, categories.size());
    }

    @Test
    void save() {
        Category category = createCategory(null, "Blabla");
        Category category1 = categoryRepository.save(category);
        assertEquals(category, category1);
    }

    private static Category createCategory(Integer categoryId, String name) {
        return new Category(categoryId, name);
    }
}