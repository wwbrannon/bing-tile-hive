[![Build Status](https://img.shields.io/travis/wwbrannon/bing-tile-hive.svg?style=flat)](https://travis-ci.org/wwbrannon/bing-tile-hive)

# bing-tile-hive
----------------

This repository provides support for [Bing tiles](https://msdn.microsoft.com/en-us/library/bb259689.aspx) for Hive. Much of the functionality is a port of [Presto's implementation](https://github.com/prestodb/presto/tree/master/presto-geospatial/src/main/java/com/facebook/presto/plugin/geospatial), but there are differences described below.

Among other uses, Bing tiles can be used for efficient spatial joins in environments, like Hive, without built-in support for spatial joins or spatial indexing.

## Features
TODO

## Differences from Presto
TODO

## Use for spatial joins
TODO

## Installation
Download the most recent released jar file from the [releases page](https://github.com/wwbrannon/bing-tile-hive/releases), or for the development version, build the jar from this repo in the usual maven way.

You can either put the jar on the Java classpath, or add it to Hive by hand:
```
add jar /path/to/jar/dir/bing-tile-hive-X.Y.jar;
```

Finally, run the appropriate `CREATE FUNCTION` statements to add the classes in the jar as UDFs. A script to create temporary (i.e., session-specific) functions is in this repo under `/sql/install.sql`.
