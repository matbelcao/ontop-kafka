package it.unibz.inf.ontop.datalog.impl;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import it.unibz.inf.ontop.injection.IntermediateQueryFactory;
import it.unibz.inf.ontop.iq.IQTree;
import it.unibz.inf.ontop.iq.UnaryIQTree;
import it.unibz.inf.ontop.iq.node.*;
import it.unibz.inf.ontop.model.term.Variable;

/**
 * Lifts the ORDER BY above the highest construction node, AS REQUIRED by our Datalog data structure
 *
 * TEMPORARY CODE (quickly implemented)
 *
 */
public class OrderByLifter {

    private final IntermediateQueryFactory iqFactory;

    @Inject
    private OrderByLifter(IntermediateQueryFactory iqFactory) {
        this.iqFactory = iqFactory;
    }

    public IQTree liftOrderBy(IQTree iqTree) {

        QueryNode root = iqTree.getRootNode();

        if(root instanceof DistinctNode || root instanceof SliceNode){
            return liftOrderBy(((UnaryIQTree) iqTree).getChild());
        }
        if (root instanceof ConstructionNode) {
            IQTree child = liftOrderBy(((UnaryIQTree) iqTree).getChild());
            QueryNode childRoot = child.getRootNode();
            if (childRoot instanceof OrderByNode) {
                ImmutableSet<Variable> projectedVars = ((ConstructionNode) root).getVariables();
                if(((OrderByNode) childRoot).getComparators().stream()
                        .flatMap(c -> c.getTerm().getVariableStream())
                        .allMatch(v -> projectedVars.contains(v))){
                    IQTree orderByChild = ((UnaryIQTree) child).getChild();
                    return iqFactory.createUnaryIQTree(
                            (UnaryOperatorNode) childRoot,
                            iqFactory.createUnaryIQTree(
                                    (UnaryOperatorNode) root,
                                    orderByChild
                            ));
                }
            }
        }
        return iqTree;
    }
//    public IQTree liftOrderBy(IQTree iqTree) {
//
//        Stack<QueryModifierNode> ancestors = new Stack<>();
//
//        // Non-final
//        IQTree parentTree = iqTree;
//        while ((parentTree.getRootNode() instanceof QueryModifierNode)) {
//            ancestors.push((QueryModifierNode) parentTree.getRootNode());
//            parentTree = ((UnaryIQTree)parentTree).getChild();
//        }
//
//        if (parentTree.getRootNode() instanceof ConstructionNode) {
//            ConstructionNode constructionNode = (ConstructionNode) parentTree.getRootNode();
//
//            IQTree childTree = ((UnaryIQTree)parentTree).getChild();
//            if (childTree.getRootNode() instanceof OrderByNode) {
//                OrderByNode formerOrderByNode = (OrderByNode) childTree.getRootNode();
//                IQTree grandChildTree = ((UnaryIQTree) childTree).getChild();
//
//                OrderByNode newOrderByNode = replaceTermsByProjectedVariables(formerOrderByNode,
//                        constructionNode.getSubstitution());
//
//                UnaryIQTree newConstructionTree = iqFactory.createUnaryIQTree(constructionNode, grandChildTree);
//                UnaryIQTree orderByTree = iqFactory.createUnaryIQTree(newOrderByNode, newConstructionTree);
//
//                // Non-final
//                IQTree newTree = orderByTree;
//                while (!ancestors.isEmpty()) {
//                    newTree = iqFactory.createUnaryIQTree(ancestors.pop(), newTree);
//                }
//                return newTree;
//            }
//            else
//                throw new IllegalArgumentException(
//                        "An ORDER BY was expected to follow the highest construction node");
//        }
//        else
//            throw new IllegalArgumentException(
//                    "The first non query modifier was expecting to be a construction node");
//    }
//
//    private OrderByNode replaceTermsByProjectedVariables(OrderByNode formerOrderByNode,
//                                                         ImmutableSubstitution<ImmutableTerm> substitution)
//            throws OntopInternalBugException {
//        ImmutableList<OrderByNode.OrderComparator> newComparators = formerOrderByNode.getComparators().stream()
//                .map(c -> convertComparator(c, substitution))
//                .collect(ImmutableCollectors.toList());
//
//        return iqFactory.createOrderByNode(newComparators);
//    }
//
//    private OrderByNode.OrderComparator convertComparator(OrderByNode.OrderComparator comparator,
//                                                          ImmutableSubstitution<ImmutableTerm> substitution) {
//        NonGroundTerm term = comparator.getTerm();
//        if (term instanceof Variable)
//            return comparator;
//
//        Optional<Variable> correspondingVariable = substitution.getImmutableMap().entrySet().stream()
//                .filter(e -> e.getValue().equals(term))
//                .map(Map.Entry::getKey)
//                .findFirst();
//
//        return correspondingVariable
//                .map(v -> iqFactory.createOrderComparator(v, comparator.isAscending()))
//                .orElseThrow(() -> new MinorOntopInternalBugException("The order condition " + term
//                        + " is not projected by the construction node with " + substitution + " and therefore" +
//                        "cannot be translated into the internal Datalog-like structure"));
//    }
}
