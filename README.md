# bing-tile-hive

This repository provides support for [Bing tiles](https://msdn.microsoft.com/en-us/library/bb259689.aspx) for Hive. Most of the functionality is a port of [Presto's implementation](https://github.com/prestodb/presto/tree/master/presto-geospatial/src/main/java/com/facebook/presto/plugin/geospatial), but there are differences described below.

Among other uses, Bing tiles can be used for efficient spatial joins in environments, like Hive, without built-in support for spatial joins or spatial indexing.

## Use for spatial joins
In environments like Hive without built-in support for spatial joins, we can use Bing tiles to make for more efficient joins.

As an example, let's say we have two tables: one table of Census geographies, with their WKT representations, and another of lat-long locations reported by IoT devices. We want to find the Census block group each report from a device occurred in. The natural, naive algorithm to do so computes the Cartesian product of the two tables, and then discards rows corresponding to the wrong block groups. Because Hive doesn't support spatial indexing, that's how it'll evaluate this query:
```
create table default.overlap as
select
	io.*,
	cg.geoid
from default.iot_observations io
	cross join default.census_geo cg
where
	cg.level = 'blockgroup' and
	ST_Contains(ST_GeomFromText(cg.wkt),
				ST_Point(io.longitude, io.latitude));
```

Of course, this is impractically slow for even moderately large datasets. In place of the [quadtree](https://en.wikipedia.org/wiki/Quadtree) and [R-tree](https://en.wikipedia.org/wiki/R-tree) indexes we'd typically use to speed it up, which Hive lacks, we can use a [grid-based index](https://en.wikipedia.org/wiki/Grid_(spatial_index)) via Bing tiles:

```
create table default.overlap as
with
    cg as (
        select
            cgi.geoid,
            cgi.level,

            ST_GeomFromText(cgi.wkt) as geom,

            t.tile
        from default.census_geo cgi
            lateral view explode
                (BT_TilesCoveringGeometry(ST_Envelope(ST_GeomFromText(cgi.wkt)), 10)) t as tile
    ),

    iot as (
        select
            ioti.obs_id,

            ST_Point(cast(ioti.latitude as double), cast(ioti.longitude as double)) as geom,
            BT_FromLatLon(cast(ioti.latitude as double), cast(ioti.longitude as double), 10) as tile
        from default.iot_observations ioti
        where
            -- bing tiles don't work very close to the poles, but that's usually fine
            abs(cast(ioti.latitude as double)) <= 85
    )
select
  iot.obs_id,
  cg.geoid,
  cg.level
from iot
    inner join cg on cg.tile = iot.tile
where
    ST_Contains(cg.geom, iot.geom);
```

Joining on the tile reduces the number of rows to compare enough to let the query finish in a reasonable amount of time. (We could follow a similar grid-based strategy with the standard `ST_Bin` and `ST_BinEnvelope` functions, but the Bing tiles make it much easier.)

See also [this post](https://github.com/prestodb/presto/pull/5435#issuecomment-353400638) from the Presto team.

## Differences from Presto
Differences from the Presto implementation include:
* Hive doesn't have user-defined types, so there's no Bing tile type. Tiles are instead stored as their quadkeys, in a text field.
* Different names for the functions. They're prefixed with `BT_` to mimic the `ST_` prefix of the OGC geospatial functions. In particular, the `bing_tile_coordinates` function from Presto has been split into `BT_GetX` and `BT_GetY`, and the `bing_tile` constructor from coordinates has been renamed to `BT_FromCoordinates`.

## Installation
Download the most recent released jar file from the [releases page](https://github.com/wwbrannon/bing-tile-hive/releases), or for the development version, build the jar from this repo in the usual maven way.

You can either put the jar on the Java classpath by hand, or add it to Hive:
```
add jar /path/to/jar/dir/bing-tile-hive-X.Y.jar;
```

Finally, run the appropriate `CREATE FUNCTION` statements to add the classes in the jar as UDFs. A script to create temporary (i.e., session-specific) functions is in this repo under `/sql/install.sql`.

