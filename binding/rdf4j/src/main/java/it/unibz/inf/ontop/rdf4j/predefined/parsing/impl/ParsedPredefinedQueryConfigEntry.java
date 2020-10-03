package it.unibz.inf.ontop.rdf4j.predefined.parsing.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableMap;
import it.unibz.inf.ontop.rdf4j.predefined.parsing.PredefinedQueryConfigEntry;
import org.eclipse.rdf4j.query.Query;

import java.util.Map;
import java.util.Optional;

/**
 * TODO: enforce required (shall we use @JsonCreator?)
 */
public class ParsedPredefinedQueryConfigEntry implements PredefinedQueryConfigEntry {
    @JsonProperty(value = "queryType", required = true)
    private String queryTypeString;

    // LAZY
    private Query.QueryType queryType;

    @JsonProperty(value = "name", required = false)
    private String name;
    @JsonProperty(value = "description", required = false)
    private String description;
    @JsonProperty(value = "context", required = false)
    private Object context;
    @JsonProperty(value = "outputContext", required = false)
    private Object outputContext;
    @JsonProperty(value = "frame", required = false)
    private Map<String, Object> frame;
    @JsonProperty(value = "parameters", required = true)
    private Map<String, ParsedQueryParameter> parameters;
    // LAZY
    private ImmutableMap<String, QueryParameter> typedParameters;

    @Override
    public Query.QueryType getQueryType() {
        if (queryType == null)
            queryType = Query.QueryType.valueOf(queryTypeString.toUpperCase());
        return queryType;
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<Object> getContext() {
        return Optional.ofNullable(context);
    }

    public Optional<Object> getOutputContext() {
        return Optional.ofNullable(outputContext);
    }

    public Optional<Map<String, Object>> getFrame() {
        return Optional.ofNullable(frame);
    }

    public ImmutableMap<String, QueryParameter> getParameters() {
        if (typedParameters == null)
            typedParameters = ImmutableMap.copyOf(parameters);
        return typedParameters;
    }

    /**
     * TODO: enforce required (shall we use @JsonCreator?)
     */
    protected static class ParsedQueryParameter implements QueryParameter {
        @JsonProperty(value = "description", required = false)
        private String description;
        @JsonProperty(value = "type", required = true)
        private String type;
        @JsonProperty(value = "safeForRandomGeneration", required = true)
        private Boolean safeForRandomGeneration;
        @JsonProperty(value = "required", required = true)
        private Boolean required;

        @Override
        public Optional<String> getDescription() {
            return Optional.ofNullable(description);
        }

        public String getType() {
            return type;
        }

        @Override
        public Boolean getSafeForRandomGeneration() {
            return safeForRandomGeneration;
        }

        @Override
        public Boolean getRequired() {
            return required;
        }
    }
}
