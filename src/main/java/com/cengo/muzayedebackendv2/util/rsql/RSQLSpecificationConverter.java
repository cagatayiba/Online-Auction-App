package com.cengo.muzayedebackendv2.util.rsql;

import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import static com.cengo.muzayedebackendv2.repository.spec.NoOpSpec.noOpSpec;

@Component
public class RSQLSpecificationConverter {

    private final Set<ComparisonOperator> rsqlOperators;

    public RSQLSpecificationConverter(Set<ComparisonOperator> customOperators) {
        Set<ComparisonOperator> rsqlOperators = new HashSet<>();
        rsqlOperators.addAll(RSQLOperators.defaultOperators());
        rsqlOperators.addAll(customOperators);
        this.rsqlOperators = rsqlOperators;
    }

    public <T> Specification<T> convertToSpec(final String rsqlFilter) {
        if(rsqlFilter == null || rsqlFilter.isEmpty()) return noOpSpec();
        return (root, query, cb) -> {
            Node rsql = new RSQLParser(rsqlOperators).parse(rsqlFilter);
            return rsql.accept(new JpaRSQLConverter(cb), root);
        };
    }
}