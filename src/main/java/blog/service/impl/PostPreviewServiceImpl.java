package blog.service.impl;

import blog.entity.PostPreview;
import blog.entity.domain.RecordStatus;
import blog.repository.PostPreviewRepository;
import blog.service.PostPreviewService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class PostPreviewServiceImpl extends BaseServiceImpl<PostPreview> implements PostPreviewService {

    private final PostPreviewRepository postPreviewRepository;

    protected PostPreviewServiceImpl(PostPreviewRepository postPreviewRepository) {
        super(postPreviewRepository);
        this.postPreviewRepository = postPreviewRepository;
    }

    @Override
    public PostPreview setRecordStatus(Integer postId, RecordStatus recordStatus) {
        PostPreview postPreview = postPreviewRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("PostPreview.notFound"));
        postPreview.setRecordStatus(recordStatus);
        return save(postPreview);
    }
}
