package it.unibz.inf.ontop.datalog.impl;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import it.unibz.inf.ontop.datalog.UnionFlattener;
import it.unibz.inf.ontop.injection.IntermediateQueryFactory;
import it.unibz.inf.ontop.iq.IQ;
import it.unibz.inf.ontop.iq.IQTree;
import it.unibz.inf.ontop.iq.node.ConstructionNode;
import it.unibz.inf.ontop.iq.node.QueryNode;
import it.unibz.inf.ontop.iq.node.UnionNode;
import it.unibz.inf.ontop.iq.transform.impl.DefaultRecursiveIQTransformer;
import it.unibz.inf.ontop.utils.ImmutableCollectors;
import it.unibz.inf.ontop.utils.VariableGenerator;

import java.util.stream.Stream;

/**
 * Lifts unions above projections, until a fixed point is reached.
 * Also merges consecutive unions and projections.
 * <p>
 * This normalization may be needed for datalog-based mapping optimizers.
 */
public class UnionFlattenerImpl implements UnionFlattener {

    private final IntermediateQueryFactory iqFactory;

    private class TreeTransformer extends DefaultRecursiveIQTransformer {

        private final VariableGenerator variableGenerator;

        private TreeTransformer(VariableGenerator variableGenerator) {
            super(UnionFlattenerImpl.this.iqFactory);
            this.variableGenerator = variableGenerator;
        }

        @Override
        public IQTree transformConstruction(IQTree tree, ConstructionNode rootCn, IQTree child) {

            IQTree transformedChild = child.acceptTransformer(this);
            QueryNode transformedChildRoot = transformedChild.getRootNode();

            // if the child is a union, lift it
            if (transformedChildRoot instanceof UnionNode) {
                return iqFactory.createNaryIQTree(
                        iqFactory.createUnionNode(rootCn.getVariables()),
                        transformedChild.getChildren().stream()
                                .map(t -> iqFactory.createUnaryIQTree(rootCn, t))
                                .collect(ImmutableCollectors.toList())
                );
            }
            // if the child is a construction node, merge it
            if (transformedChildRoot instanceof ConstructionNode) {
                return rootCn.liftBinding(
                        transformedChild,
                        variableGenerator,
                        iqFactory.createIQProperties()
                );
            }
            return iqFactory.createUnaryIQTree(rootCn, transformedChild);
        }

        @Override
        // merge consecutive unions
        public IQTree transformUnion(IQTree tree, UnionNode rootNode, ImmutableList<IQTree> children) {

            ImmutableList<IQTree> transformedChildren = children.stream()
                    .map(t -> t.acceptTransformer(this))
                    .collect(ImmutableCollectors.toList());

            ImmutableList<IQTree> unionGrandChildren = transformedChildren.stream()
                    .filter(t -> t.getRootNode() instanceof UnionNode)
                    .flatMap(t -> t.getChildren().stream())
                    .collect(ImmutableCollectors.toList());

            return iqFactory.createNaryIQTree(
                    rootNode,
                    Stream.concat(
                            transformedChildren.stream()
                                    .filter(t -> !(t.getRootNode() instanceof UnionNode)),
                            unionGrandChildren.stream()
                    ).collect(ImmutableCollectors.toList())
            );
        }
    }

    @Inject
    private UnionFlattenerImpl(IntermediateQueryFactory iqFactory) {
        this.iqFactory = iqFactory;
    }

    /**
     * TODO: why a fix point?
     */
    @Override
    public IQ optimize(IQ query) {
        TreeTransformer treeTransformer = new TreeTransformer(query.getVariableGenerator());
        IQ prev;
        do {
            prev = query;
            query = iqFactory.createIQ(
                    query.getProjectionAtom(),
                    query.getTree().acceptTransformer(treeTransformer)
            );
        } while (!prev.equals(query));
        return query;
    }
}