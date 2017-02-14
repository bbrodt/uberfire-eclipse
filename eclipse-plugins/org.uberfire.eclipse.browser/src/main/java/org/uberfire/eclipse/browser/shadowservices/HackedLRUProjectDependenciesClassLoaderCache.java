package org.uberfire.eclipse.browser.shadowservices;

import org.kie.workbench.common.services.backend.builder.LRUBuilderCache;
import org.kie.workbench.common.services.backend.builder.LRUProjectDependenciesClassLoaderCache;

class HackedLRUProjectDependenciesClassLoaderCache extends LRUProjectDependenciesClassLoaderCache {
    @Override
    public void setBuilderCache( LRUBuilderCache builderCache ) {
        super.setBuilderCache( builderCache );
    }
}