package blog.controller;

import blog.entity.Tag;
import blog.service.TagService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllTags")
    public ResponseEntity<List<Tag>> getAllTags(@RequestParam(name = "q", required = false) Specification<Tag> specification,
                                                @RequestParam(name = "sort", required = false) Sort sort) {
        return ResponseEntity.ok(tagService.findAll(specification, sort));
    }

    @GetMapping("/{tagId}")
    @ApiOperation(value = "", nickname = "getTagById")
    public ResponseEntity<Tag> getTagById(@PathVariable Integer tagId) {
        return ResponseEntity.ok(tagService.findById(tagId));
    }

    @PostMapping
    @ApiOperation(value = "", nickname = "saveTag")
    public ResponseEntity<Tag> saveTag(@RequestBody Tag tag) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tagService.save(tag));
    }

    @PutMapping
    @ApiOperation(value = "", nickname = "updateTag")
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag) {
        return ResponseEntity.ok(tagService.update(tag));
    }

    @DeleteMapping("/{tagId}")
    @ApiOperation(value = "", nickname = "deleteTagById")
    public void deleteTagById(@PathVariable Integer tagId) {
        tagService.deleteById(tagId);
    }
}