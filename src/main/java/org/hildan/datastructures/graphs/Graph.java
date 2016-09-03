package org.hildan.datastructures.graphs;

import java.util.Collection;
import java.util.Map;

public interface Graph<N, C> {

    Collection<N> nodes();

    Map<N, Map<N, C>> edges();

    Map<N, C> edgesFrom(N node);

    Map<N, C> edgesTo(N node);
}
