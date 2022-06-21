package blog.util;

import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SortUtil {
    public static Pageable overrideSort(Pageable pageable, Sort sort) {
        if (pageable == null)
            return null;
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
