package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "",
    value = "",
    extended = ""
)

public class BT_FromLatLon extends BT_Base {
    public Text evaluate(DoubleWritable lat, DoubleWritable lon,
                                  IntWritable zoomLevel) throws BingTileException
    {
        if(lat == null || lon == null || zoomLevel == null) return null;

        BingTile bt = BingTile.fromLatLon(lat.get(), lon.get(), zoomLevel.get());
        return new Text(bt.toQuadKey());
    }
}

