package com.wwbrannon.bing;

import java.util.ArrayList;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "BT_TilesAround",
    value = "_FUNC_(BT_TilesAround) - return an array of Bing tiles surrounding a given lat/long at a given zoom level"
)

public class BT_TilesAround extends BT_Base {
    public ArrayList<Text> evaluate(DoubleWritable lat, DoubleWritable lon,
                                    IntWritable zoomLevel) throws BingTileException
    {
        if(lat == null || lon == null || zoomLevel == null) return null;
        
        ArrayList<Text> ret = new ArrayList<Text>();
        ArrayList<BingTile> tmp = BingTile.tilesAround(lat.get(), lon.get(), zoomLevel.get());

        for (BingTile b: tmp)
            ret.add(new Text(b.toQuadKey()));

        return ret;
    }
}

