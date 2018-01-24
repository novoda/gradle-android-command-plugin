package com.novoda.gradle.command

class VariantSuffix {

  static String variantNameFor(variant) {
    def buildTypeName = variant.buildType.name.capitalize()
    def projectFlavorNames = variant.productFlavors.collect { it.name.capitalize() }
    def projectFlavorName = projectFlavorNames.join()
    return projectFlavorName + buildTypeName
  }
}
