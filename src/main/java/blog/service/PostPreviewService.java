package blog.service;

import blog.entity.PostPreview;
import blog.entity.domain.RecordStatus;
import blog.service.base.BaseService;

public interface PostPreviewService extends BaseService<PostPreview> {

    PostPreview setRecordStatus(Integer postId, RecordStatus recordStatus);
}
