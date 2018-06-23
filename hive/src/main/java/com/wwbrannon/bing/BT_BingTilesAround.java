package com.wwbrannon.bing;

import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<BytesWritable> evaluate(DoubleWritable lat, DoubleWritable lon,
                                             IntWritable zoomlevel)
    {
        if(lat == null || lon == null || zoomLevel == null) return null;
        
        ArrayList<BingTile> tmp = BingTile.tilesAround(lat, lon, zoomLevel);
        ArrayList<BytesWritable> ret = new ArrayList<BytesWritable>();

        for (BytesWritable b: tmp)
            ret.add(b);

        return ret;
    }
}

