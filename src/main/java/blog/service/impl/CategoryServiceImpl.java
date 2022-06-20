package blog.service.impl;

import blog.entity.Category;
import blog.repository.CategoryRepository;
import blog.service.CategoryService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends BaseServiceImpl<Category> implements CategoryService {

    private final CategoryRepository categoryRepository;

    protected CategoryServiceImpl(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }
}
