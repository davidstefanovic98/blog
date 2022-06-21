package blog.controller;

import blog.entity.Comment;
import blog.entity.Post;
import blog.entity.User;
import blog.entity.domain.PostSummary;
import blog.service.CommentService;
import blog.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static blog.entity.domain.RecordStatus.ACTIVE;
import static blog.util.SpecificationUtil.combineSpecificationFor;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping("/all")
    @ApiOperation(value = "", nickname = "getAllPosts")
    public ResponseEntity<List<Post>> getAllPosts(@AuthenticationPrincipal User user,
                                                  @RequestParam(name = "q", required = false) Specification<Post> specification,
                                                  @RequestParam(name = "page", required = false) Pageable pageable,
                                                  @RequestParam(name = "sort", required = false) Sort sort) {
        if (!user.isAdmin()) {
            specification = combineSpecificationFor(specification, "user.username", user.getUsername());
        }
        return ResponseEntity.ok(postService.findAll(specification, pageable, sort));
    }

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllPostsNotDeleted")
    public ResponseEntity<List<Post>> getAllPostsNotDeleted(@RequestParam(name = "q", required = false) Specification<Post> specification,
                                                            @RequestParam(name = "page", required = false) Pageable pageable,
                                                            @RequestParam(name = "sort", required = false) Sort sort) {
        return ResponseEntity.ok(postService.findAll(specification, pageable, sort));
    }

    @GetMapping("/{identifier}")
    @ApiOperation(value = "", nickname = "getPostById")
    public ResponseEntity<Post> getPostById(@PathVariable String identifier) {
        try {
            return ResponseEntity.ok(postService.findById(Integer.parseInt(identifier)));
        } catch (NumberFormatException ignored) {
            return ResponseEntity.ok(postService.findBySlug(identifier));
        }
    }

    @GetMapping("/summary")
    @ApiOperation(value = "", nickname = "getPostSummary")
    public ResponseEntity<PostSummary> getPostSummary(@RequestParam PostSummary.SummaryType type) {
        return ResponseEntity.ok(postService.getSummary(type));
    }

    @PostMapping
    @ApiOperation(value = "", nickname = "savePost")
    public ResponseEntity<Post> savePost(@RequestBody Post post) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(post));
    }

    @PutMapping
    @ApiOperation(value = "", nickname = "updatePost")
    public ResponseEntity<Post> updatePost(@RequestBody Post post) {
        return ResponseEntity.ok(postService.update(post));
    }

    @DeleteMapping("/{postId}")
    @ApiOperation(value = "", nickname = "deletePostById")
    public void deletePostById(@PathVariable Integer postId) {
        postService.deleteById(postId);
    }

    @GetMapping("/{postId}/comments")
    @ApiOperation(value = "", nickname = "getAllPostComments")
    public ResponseEntity<List<Comment>> getAllPostComments(@PathVariable Integer postId,
                                                            @RequestParam(name = "q", required = false) Specification<Comment> specification,
                                                            @RequestParam(name = "page", required = false) Pageable pageable) {
        return ResponseEntity.ok(commentService.findAll(postId, specification, pageable));
    }

    @PostMapping("/{postId}/comments")
    @ApiOperation(value = "", nickname = "savePostComment")
    public ResponseEntity<Comment> savePostComment(@PathVariable Integer postId,
                                                   @RequestBody Comment comment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(postId, comment));
    }

    @PutMapping("/{postId}/comments")
    @ApiOperation(value = "", nickname = "updatePostComment")
    public ResponseEntity<Comment> updatePostComment(@PathVariable Integer postId,
                                                     @RequestBody Comment comment) {
        return ResponseEntity.ok(commentService.update(comment));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    @ApiOperation(value = "", nickname = "deletePostCommentById")
    public void deletePostCommentById(@PathVariable Integer postId,
                                      @PathVariable Integer commentId) {
        commentService.deleteById(commentId);
    }
}
