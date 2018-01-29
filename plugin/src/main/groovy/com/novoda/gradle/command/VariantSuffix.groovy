package com.novoda.gradle.command

class VariantSuffix {

  static String variantNameFor(variant) {
    def buildTypeName = variant.buildType.name.capitalize()
    def projectFlavorName = variant.productFlavors.collect { it.name.capitalize() }.join()
    return projectFlavorName + buildTypeName
  }
}
