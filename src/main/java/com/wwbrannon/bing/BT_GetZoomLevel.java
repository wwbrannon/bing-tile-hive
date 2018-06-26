package com.wwbrannon.bing;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_GetZoomLevel",
    value = "_FUNC_(BT_GetZoomLevel) - return the zoom level of a given Bing tile"
)

public class BT_GetZoomLevel extends BT_Base {
    public IntWritable evaluate(Text btref) throws BingTileException
    {
        if(btref == null) return null;

        int z = BingTile.fromQuadKey(btref.toString()).getZoomLevel();
        return new IntWritable(z);
    }
}

