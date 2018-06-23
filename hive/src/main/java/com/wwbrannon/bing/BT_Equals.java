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

public class BT_Equals extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTileEquals.class.getName());

    public boolean evaluate(TextWritable left, TextWritable right)
    {
        if(left == null || right == null) return null;

        BingTile btl = BingTile.fromQuadKey(left);
        BingTile btr = BingTile.fromQuadKey(right);

        return btl.equals(btr);
    }
}

