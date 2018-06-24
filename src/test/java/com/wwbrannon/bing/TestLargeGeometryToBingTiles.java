package com.wwbrannon.bing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Collections;

import org.testng.annotations.Test;
import org.testng.Assert;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

public class TestLargeGeometryToBingTiles
{
    @Test
    public void test() throws Exception
    {
        Path filePath = Paths.get(this.getClass().getClassLoader().getResource("large_polygon.txt").getPath());
        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            String[] parts = line.split("\\|");
            String wkt = parts[0];
            int zoomLevel = Integer.parseInt(parts[1]);
            long tileCount = Long.parseLong(parts[2]);
            assertFunction("cardinality(geometry_to_bing_tiles(ST_GeometryFromText('" + wkt + "'), " + zoomLevel + "))", BIGINT, tileCount);
        }
    }
}

