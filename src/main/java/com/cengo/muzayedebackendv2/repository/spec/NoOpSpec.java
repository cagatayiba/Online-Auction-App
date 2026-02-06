package com.cengo.muzayedebackendv2.repository.spec;

import org.springframework.data.jpa.domain.Specification;

public class NoOpSpec {

    public static <T> Specification<T> noOpSpec() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(criteriaBuilder.literal(true));
    }
}
