package com.novoda.gradle.command

class VariantSuffix {

  static String variantNameFor(variant) {
    String buildTypeName = variant.buildType.name.capitalize()
    String projectFlavorName = variant.productFlavors.collect { it.name.capitalize() }.join()
    return projectFlavorName + buildTypeName
  }
}
