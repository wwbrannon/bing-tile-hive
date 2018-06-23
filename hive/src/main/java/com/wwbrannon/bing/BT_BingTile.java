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

public class BT_BingTile extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTile.class.getName());

    public BytesWritable evaluate(TextWritable btref)
    {
        if(btref == null) return null;

        // create this object as a way of checking validity
        return BingTile.fromQuadKey(btref).toQuadKey();
    }
    
    public BytesWritable evaluate(DoubleWritable x, DoubleWritable y,
                                  IntWritable zoomlevel)
    {
        if(x == null || y == null || zoomLevel == null) return null;

        return BingTile.fromCoordinates(x, y, zoomLevel).toQuadKey();
    }
}

