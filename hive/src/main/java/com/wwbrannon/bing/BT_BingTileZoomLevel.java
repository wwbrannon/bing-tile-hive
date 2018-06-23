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

public class BT_BingTileZoomLevel extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTileZoomLevel.class.getName());

    public int evaluate(TextWritable btref)
    {
        if(btref == null) return null;

        return BingTile.fromQuadKey(btref).getZoomLevel();
    }
}

