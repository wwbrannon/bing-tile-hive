package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_FromCoordinates",
    value = "_FUNC_(BT_FromCoordinates) - return the Bing tile at given (x,y) coordinates at a given zoom level"
)

public class BT_FromCoordinates extends BT_Base {
    public Text evaluate(IntWritable x, IntWritable y,
                         IntWritable zoomLevel) throws BingTileException
    {
        if(x == null || y == null || zoomLevel == null) return null;

        BingTile bt = BingTile.fromCoordinates(x.get(), y.get(), zoomLevel.get());
        return new Text(bt.toQuadKey());
    }
}

