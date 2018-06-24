package com.wwbrannon.bing;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.wwbrannon.bing.BingTile;

@Description(
    name = "",
    value = "",
    extended = ""
)

public class BT_BingTilesAround extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTilesAround.class.getName());

    public ArrayList<Text> evaluate(DoubleWritable lat, DoubleWritable lon,
                                    IntWritable zoomlevel)
    {
        if(lat == null || lon == null || zoomLevel == null) return null;
        
        ArrayList<Text> ret = new ArrayList<Text>();
        ArrayList<BingTile> tmp = BingTile.tilesAround(lat.get(), lon.get(), zoomLevel.get());

        for (BingTile b: tmp)
            ret.add(Text(b.toQuadKey()));

        return ret;
    }
}

