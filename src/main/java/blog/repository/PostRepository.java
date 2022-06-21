package blog.repository;

import blog.entity.Post;
import blog.repository.base.BaseRepository;

import java.util.Optional;

public interface PostRepository extends BaseRepository<Post> {

    Optional<Post> findBySlug(String slug);
}
