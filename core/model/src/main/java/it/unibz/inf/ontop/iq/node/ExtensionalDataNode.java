package it.unibz.inf.ontop.iq.node;

import com.google.common.collect.ImmutableMap;
import it.unibz.inf.ontop.dbschema.RelationDefinition;
import it.unibz.inf.ontop.iq.LeafIQTree;
import it.unibz.inf.ontop.iq.exception.QueryNodeTransformationException;
import it.unibz.inf.ontop.model.atom.RelationPredicate;
import it.unibz.inf.ontop.model.atom.DataAtom;
import it.unibz.inf.ontop.iq.transform.node.HomogeneousQueryNodeTransformer;
import it.unibz.inf.ontop.model.term.VariableOrGroundTerm;

/**
 * TODO: explain
 */
public interface ExtensionalDataNode extends LeafIQTree {

    @Deprecated
    DataAtom<RelationPredicate> getProjectionAtom();

    RelationDefinition getRelationDefinition();

    ImmutableMap<Integer, ? extends VariableOrGroundTerm> getArgumentMap();

    @Override
    ExtensionalDataNode clone();

    @Override
    ExtensionalDataNode acceptNodeTransformer(HomogeneousQueryNodeTransformer transformer)
            throws QueryNodeTransformationException;
}
