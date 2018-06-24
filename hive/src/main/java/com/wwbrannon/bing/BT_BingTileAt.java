package com.wwbrannon.bing;

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

public class BT_BingTileAt extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTileAt.class.getName());

    public Text evaluate(DoubleWritable lat, DoubleWritable lon,
                                  IntWritable zoomlevel)
    {
        if(lat == null || lon == null || zoomLevel == null) return null;

        BingTile bt = BingTile.fromLatLon(lat.get(), lon.get(), zoomLevel.get());
        return Text(bt.toQuadKey());
    }
}

