package com.wwbrannon.bing;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

public class TestBingTilePolygon
{
    @Test
    public void test()
    {
        assertFunction("ST_AsText(bing_tile_polygon(bing_tile('123030123010121')))", VARCHAR, "POLYGON ((59.996337890625 30.11662158281937, 60.00732421875 30.11662158281937, 60.00732421875 30.12612436422458, 59.996337890625 30.12612436422458, 59.996337890625 30.11662158281937))");
        assertFunction("ST_AsText(ST_Centroid(bing_tile_polygon(bing_tile('123030123010121'))))", VARCHAR, "POINT (60.0018310442288 30.121372968273892)");

        // Check bottom right corner of a stack of tiles at different zoom levels
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(1, 1, 1)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (180 -85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(3, 3, 2)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (180 -85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(7, 7, 3)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (180 -85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(15, 15, 4)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (180 -85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(31, 31, 5)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (180 -85.05112877980659)");

        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(0, 0, 1)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(1, 1, 2)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(3, 3, 3)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(7, 7, 4)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(15, 15, 5)), g -> ST_Point(ST_XMax(g), ST_YMin(g))))", VARCHAR, "POINT (0 0)");

        // Check top left corner of a stack of tiles at different zoom levels
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(1, 1, 1)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(2, 2, 2)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(4, 4, 3)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(8, 8, 4)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (0 0)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(16, 16, 5)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (0 0)");

        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(0, 0, 1)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (-180 85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(0, 0, 2)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (-180 85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(0, 0, 3)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (-180 85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(0, 0, 4)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (-180 85.05112877980659)");
        assertFunction("ST_AsText(apply(bing_tile_polygon(bing_tile(0, 0, 5)), g -> ST_Point(ST_XMin(g), ST_YMax(g))))", VARCHAR, "POINT (-180 85.05112877980659)");
    }
}

