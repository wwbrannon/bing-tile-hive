package com.wwbrannon.bing;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.testng.annotations.Test;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import com.esri.hadoop.hive.ST_GeomFromText;

import com.wwbrannon.bing.*;
import com.wwbrannon.bing.exception.BingTileException;

import static org.testng.Assert.*;

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
            
            assertEquals( (new BT_TilesCoveringGeometry()).evaluate( (new ST_GeomFromText()).evaluate(new Text(wkt)), new IntWritable(zoomLevel) ).size(), tileCount);
            // assertFunction("cardinality(geometry_to_bing_tiles(ST_GeometryFromText('" + wkt + "'), " + zoomLevel + "))", BIGINT, tileCount);
        }
    }
}

