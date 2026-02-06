package com.cengo.muzayedebackendv2.util;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import java.util.Comparator;
import java.util.List;

public class PageUtils {

    public static <T> Page<T> sortPage(Page<T> page, Comparator<T> comparator) {
        // Step 1: Extract content from the Page
        List<T> content = page.getContent();

        // Step 2: Sort the content
        content.sort(comparator);

        // Step 3: Create a new Page with the sorted content and the original Pageable
        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
}
