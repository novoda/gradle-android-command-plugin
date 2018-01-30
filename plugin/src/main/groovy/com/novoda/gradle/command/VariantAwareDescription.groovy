package com.novoda.gradle.command

import groovy.transform.CompileStatic
import groovy.transform.PackageScope

@PackageScope
@CompileStatic
class VariantAwareDescription {

    private VariantAwareDescription() {}

    static String createFor(variant, extension, String defaultDescription = null) {
        def variantName = VariantSuffix.variantNameFor(variant)
        def description = extension instanceof DescriptionAware && extension.description ?: defaultDescription
        return description ? "$description for $variantName" : null
    }
}
