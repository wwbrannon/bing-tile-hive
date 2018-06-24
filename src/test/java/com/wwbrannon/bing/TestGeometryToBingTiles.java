package com.wwbrannon.bing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Collections;

import com.google.common.collect.ImmutableList;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

public class TestGeometryToBingTiles
{
    @Test
    public void test() throws Exception
    {
        assertGeometryToBingTiles("POINT (60 30.12)", 10, ImmutableList.of("1230301230"));
        assertGeometryToBingTiles("POINT (60 30.12)", 15, ImmutableList.of("123030123010121"));
        assertGeometryToBingTiles("POINT (60 30.12)", 16, ImmutableList.of("1230301230101212"));

        assertGeometryToBingTiles("POLYGON ((0 0, 0 10, 10 10, 10 0))", 6, ImmutableList.of("122220", "122222", "122221", "122223"));
        assertGeometryToBingTiles("POLYGON ((0 0, 0 10, 10 10))", 6, ImmutableList.of("122220", "122222", "122221"));

        assertGeometryToBingTiles("POLYGON ((10 10, -10 10, -20 -15, 10 10))", 3, ImmutableList.of("033", "211", "122"));
        assertGeometryToBingTiles("POLYGON ((10 10, -10 10, -20 -15, 10 10))", 6, ImmutableList.of("211102", "211120", "033321", "033323", "211101", "211103", "211121", "033330", "033332", "211110", "211112", "033331", "033333", "211111", "122220", "122222", "122221"));

        assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12))", 10, ImmutableList.of("1230301230"));
        assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12))", 15, ImmutableList.of("123030123010121"));
        assertGeometryToBingTiles("GEOMETRYCOLLECTION (POLYGON ((10 10, -10 10, -20 -15, 10 10)))", 3, ImmutableList.of("033", "211", "122"));
        assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12), POLYGON ((10 10, -10 10, -20 -15, 10 10)))", 3, ImmutableList.of("033", "211", "122", "123"));
        assertGeometryToBingTiles("GEOMETRYCOLLECTION (POINT (60 30.12), LINESTRING (61 31, 61.01 31.01), POLYGON EMPTY)", 15, ImmutableList.of("123030123010121", "123030112310200", "123030112310202", "123030112310201"));

        assertFunction("transform(geometry_to_bing_tiles(bing_tile_polygon(bing_tile('1230301230')), 10), x -> bing_tile_quadkey(x))", new ArrayType(VARCHAR), ImmutableList.of("1230301230"));
        assertFunction("transform(geometry_to_bing_tiles(bing_tile_polygon(bing_tile('1230301230')), 11), x -> bing_tile_quadkey(x))", new ArrayType(VARCHAR), ImmutableList.of("12303012300", "12303012302", "12303012301", "12303012303"));

        assertFunction("transform(geometry_to_bing_tiles(ST_Envelope(ST_GeometryFromText('LINESTRING (59.765625 29.84064389983442, 60.2 30.14512718337612)')), 10), x -> bing_tile_quadkey(x))", new ArrayType(VARCHAR), ImmutableList.of("1230301230", "1230301231"));

        // Empty geometries
        assertGeometryToBingTiles("POINT EMPTY", 10, Collections.emptyList());
        assertGeometryToBingTiles("POLYGON EMPTY", 10, Collections.emptyList());
        assertGeometryToBingTiles("GEOMETRYCOLLECTION EMPTY", 10, Collections.emptyList());

        // Invalid input
        // Longitude out of range
        assertInvalidFunction("geometry_to_bing_tiles(ST_Point(600, 30.12), 10)", "Longitude span for the geometry must be in [-180.00, 180.00] range");
        assertInvalidFunction("geometry_to_bing_tiles(ST_GeometryFromText('POLYGON ((1000 10, -10 10, -20 -15))'), 10)", "Longitude span for the geometry must be in [-180.00, 180.00] range");
        // Latitude out of range
        assertInvalidFunction("geometry_to_bing_tiles(ST_Point(60, 300.12), 10)", "Latitude span for the geometry must be in [-85.05, 85.05] range");
        assertInvalidFunction("geometry_to_bing_tiles(ST_GeometryFromText('POLYGON ((10 1000, -10 10, -20 -15))'), 10)", "Latitude span for the geometry must be in [-85.05, 85.05] range");
        // Invalid zoom levels
        assertInvalidFunction("geometry_to_bing_tiles(ST_Point(60, 30.12), 0)", "Zoom level must be > 0");
        assertInvalidFunction("geometry_to_bing_tiles(ST_Point(60, 30.12), 40)", "Zoom level must be <= 23");

        // Input rectangle too large
        assertInvalidFunction("geometry_to_bing_tiles(ST_Envelope(ST_GeometryFromText('LINESTRING (0 0, 80 80)')), 16)", "The number of input tiles is too large (more than 1M) to compute a set of covering Bing tiles.");
        assertFunction("cardinality(geometry_to_bing_tiles(ST_Envelope(ST_GeometryFromText('LINESTRING (0 0, 80 80)')), 5))", BIGINT, 104L);

        // Input polygon too complex
        String filePath = this.getClass().getClassLoader().getResource("too_large_polygon.txt").getPath();
        String largeWkt = Files.lines(Paths.get(filePath)).findFirst().get();
        assertInvalidFunction("geometry_to_bing_tiles(ST_GeometryFromText('" + largeWkt + "'), 16)", "The zoom level is too high or the geometry is too complex to compute a set of covering Bing tiles. Please use a lower zoom level or convert the geometry to its bounding box using the ST_Envelope function.");
        assertFunction("cardinality(geometry_to_bing_tiles(ST_Envelope(ST_GeometryFromText('" + largeWkt + "')), 16))", BIGINT, 19939L);

        // Zoom level is too high
        assertInvalidFunction("geometry_to_bing_tiles(ST_GeometryFromText('POLYGON ((0 0, 0 20, 20 20, 0 0))'), 20)", "The zoom level is too high to compute a set of covering Bing tiles.");
        assertFunction("cardinality(geometry_to_bing_tiles(ST_GeometryFromText('POLYGON ((0 0, 0 20, 20 20, 0 0))'), 14))", BIGINT, 428787L);
    }

    private void assertGeometryToBingTiles(String wkt, int zoomLevel, List<String> expectedQuadKeys)
    {
        assertFunction(String.format("transform(geometry_to_bing_tiles(ST_GeometryFromText('%s'), %s), x -> bing_tile_quadkey(x))", wkt, zoomLevel), new ArrayType(VARCHAR), expectedQuadKeys);
    }
}

