package com.cengo.muzayedebackendv2.util.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.OrNode;
import cz.jirutka.rsql.parser.ast.RSQLVisitor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.List;

import static com.cengo.muzayedebackendv2.config.rsql.RSQLConfig.ILIKE;
import static com.cengo.muzayedebackendv2.config.rsql.RSQLConfig.LIKE;
import static com.cengo.muzayedebackendv2.config.rsql.RSQLConfig.SILIKE;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.GREATER_THAN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.GREATER_THAN_OR_EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.LESS_THAN;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.LESS_THAN_OR_EQUAL;
import static cz.jirutka.rsql.parser.ast.RSQLOperators.NOT_EQUAL;


@SuppressWarnings({"unchecked", "rawtypes"})
public class JpaRSQLConverter implements RSQLVisitor<Predicate, Root> {

    private final CriteriaBuilder builder;
    private final ConversionService conversionService = new DefaultConversionService();


    public JpaRSQLConverter(CriteriaBuilder builder) {
        this.builder = builder;
    }


    public Predicate visit(AndNode node, Root root) {

        return builder.and(processNodes(node.getChildren(), root));
    }

    public Predicate visit(OrNode node, Root root) {

        return builder.or(processNodes(node.getChildren(), root));
    }

    public Predicate visit(ComparisonNode node, Root root) {

        ComparisonOperator op = node.getOperator();
        Path attrPath = root.get(node.getSelector());
        var attrType = attrPath.getJavaType();

        // RSQL guarantees that node has at least one argument
        var argument = conversionService.convert(node.getArguments().getFirst(), attrType);
        assert argument != null;

        if (op.equals(EQUAL)) {
            return builder.equal(attrPath, argument);
        }
        if (op.equals(NOT_EQUAL)) {
            return builder.notEqual(attrPath, argument);
        }

        Attribute attribute = root.getModel().getAttribute(node.getSelector());
        Class type = attribute.getJavaType();

        if (! Comparable.class.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(
                    "Operator %s can be used only for Comparables", op));
        }
        Comparable comparable = (Comparable) conversionService.convert(argument, type);

        if (op.equals(GREATER_THAN)) {
            return builder.greaterThan(attrPath, comparable);
        }
        if (op.equals(GREATER_THAN_OR_EQUAL)) {
            return builder.greaterThanOrEqualTo(attrPath, comparable);
        }
        if (op.equals(LESS_THAN)) {
            return builder.lessThan(attrPath, comparable);
        }
        if (op.equals(LESS_THAN_OR_EQUAL)) {
            return builder.lessThanOrEqualTo(attrPath, comparable);
        }

        if(op.equals(LIKE) || op.equals(ILIKE) || op.equals(SILIKE)){
            if(argument instanceof String strArg){
                if(op.equals(LIKE)){
                    return builder.like(attrPath, "%" + strArg + "%");
                }else if (op.equals(ILIKE)){
                    return builder.like(builder.lower(attrPath), "%" + strArg.toLowerCase() + "%");
                }else if (op.equals(SILIKE)){
                    return builder.like(builder.lower(attrPath), strArg.toLowerCase() + "%");
                }
            }else {
                throw new IllegalArgumentException("In order to use LIKE family operators compared attributes both must be String");
            }
        }
        throw new IllegalArgumentException("Unknown operator: " + op);
    }

    private Predicate[] processNodes(List<Node> nodes, Root root) {

        Predicate[] predicates = new Predicate[nodes.size()];

        for (int i = 0; i < nodes.size(); i++) {
            predicates[i] = nodes.get(i).accept(this, root);
        }
        return predicates;
    }
}
