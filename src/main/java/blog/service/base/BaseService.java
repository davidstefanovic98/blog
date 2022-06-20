package blog.service.base;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * This class represents base for all service class. It contains CRUD service declarations
 * to reduce boilerplate code.
 * @param <T> - Generic entity.
 */
public interface BaseService<T> {

    List<T> findAll();

    List<T> findAll(Specification<T> specification, Sort sort);

    List<T> findAll(Specification<T> specification, Pageable pageable, Sort sort);

    T findById(Integer id);

    T save(T t);

    T update(T t);

    void deleteById(Integer id);
}
