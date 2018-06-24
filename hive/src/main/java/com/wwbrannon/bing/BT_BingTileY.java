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

public class BT_BingTileY extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTileY.class.getName());

    public IntWritable evaluate(Text btref)
    {
        if(btref == null) return null;

        int y = BingTile.fromQuadKey(btref.toString()).getY();
        return IntWritable(y);
    }
}

