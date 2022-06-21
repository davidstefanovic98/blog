package blog.controller;

import blog.entity.PostPreview;
import blog.entity.User;
import blog.entity.domain.RecordStatus;
import blog.service.PostPreviewService;
import blog.util.SpecificationUtil;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static blog.entity.domain.RecordStatus.ACTIVE;
import static blog.util.SpecificationUtil.combineSpecificationFor;

@RestController
@RequestMapping("/previews")
@RequiredArgsConstructor
public class PostPreviewController {
    private final PostPreviewService postPreviewService;

    @GetMapping("/all")
    @ApiOperation(value = "", nickname = "getAllPostPreviews")
    public ResponseEntity<List<PostPreview>> getAllPostPreviews(@AuthenticationPrincipal User user,
                                                                @RequestParam(required = false, name = "specification") Specification<PostPreview> specification,
                                                                @RequestParam(required = false, name = "sort") Sort sort,
                                                                @RequestParam(required = false, name = "page") Pageable pageable) {
        if (!user.isAdmin()) {
            specification = combineSpecificationFor(specification, "user.username", user.getUsername());
        }
        return ResponseEntity.ok(postPreviewService.findAll(specification, pageable, sort));
    }

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllPostPreviewsNotDeleted")
    public ResponseEntity<List<PostPreview>> getAllPostPreviewsNotDeleted(@RequestParam(name = "q", required = false) Specification<PostPreview> specification,
                                                                          @RequestParam(name = "page", required = false) Pageable pageable,
                                                                          @RequestParam(name = "sort", required = false) Sort sort) {
        Specification<PostPreview> spec = SpecificationUtil.combineSpecificationFor(specification, ACTIVE);
        return ResponseEntity.ok(postPreviewService.findAll(spec, pageable, sort));
    }

    @PutMapping("/{postId}/record-status")
    @ApiOperation(value = "", nickname = "setRecordStatus")
    public ResponseEntity<PostPreview> setRecordStatus(@PathVariable Integer postId,
                                                       @RequestParam RecordStatus recordStatus) {
        return ResponseEntity.ok(postPreviewService.setRecordStatus(postId, recordStatus));
    }
}