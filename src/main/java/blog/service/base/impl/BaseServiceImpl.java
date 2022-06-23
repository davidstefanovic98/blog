package blog.service.base.impl;

import blog.repository.base.BaseRepository;
import blog.service.base.BaseService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class is implementation of BaseService interface.
 * It is used to implement CRUD methods for generic entity to reduce boilerplate code.
 * @param <T>
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Getter(AccessLevel.PROTECTED)
public class BaseServiceImpl<T> implements BaseService<T> {

    protected final BaseRepository<T> baseRepository;

    @Override
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public List<T> findAll(Specification<T> specification, Sort sort) {
        return baseRepository.findAll(specification, sort == null ? Sort.unsorted() : sort);
    }

    @Override
    public List<T> findAll(Specification<T> specification, Pageable pageable, Sort sort) {
        if (pageable == null)
            return baseRepository.findAll(specification, sort == null ? Sort.unsorted() : sort);
        return baseRepository.findAll(specification, pageable).toList();
    }

    @Override
    public T findById(Integer id) {
        return baseRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entity with this id " + id + " doesn't exist."));
    }

    @Override
    public T save(T t) {
        return baseRepository.save(t);
    }

    @Override
    public T update(T t) {
        return baseRepository.save(t);
    }

    @Override
    public void deleteById(Integer id) {
        baseRepository.deleteById(id);
    }
}
