package com.novoda.gradle.command

import groovy.transform.Immutable

@Immutable
class FileInfo {
    static enum Type {
        FILE, DIRECTORY, LINK
    }

    String name
    String path
    Type type
    Date timestamp

    static FileInfo fromListing(final String line, String dir) {
        String[] items = line.split()
        FileInfo.Type type = computeType(items[0])
        String name = computeName(items, type)
        Date date = computeTimestamp(items, type)
        new FileInfo([type: type, name: name, path: dir, timestamp: date])
    }

    private static String computeName(final String[] lineItems, final FileInfo.Type type) {
        switch (type) {
            case Type.LINK:
                return lineItems[-3]
            default:
                return lineItems[-1]
        }
    }

    private static Type computeType(final String accessFlags) {
        if (accessFlags.startsWith("l"))
            return Type.LINK
        if (accessFlags.startsWith("d"))
            return Type.DIRECTORY
        return Type.FILE
    }

    private static Date computeTimestamp(String[] items, Type type) {
        switch (type) {
            case Type.FILE:
                return computeTimestamp(items[4], items[5])
            default:
                return computeTimestamp(items[3], items[4])
        }
    }

    private static Date computeTimestamp(String date, String time) {
        Date.parse('yyyy-MM-dd HH:mm', date + ' ' + time)
    }

}
