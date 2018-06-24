package com.wwbrannon.bing;

import java.util.ArrayList;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;

import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.GeometryUtils;

import com.wwbrannon.bing.BingTile;
import com.wwbrannon.bing.exception.BingTileException;

@Description(
    name = "",
    value = "",
    extended = ""
)

public class BT_TilesCoveringGeometry extends BT_Base {
    public ArrayList<Text> evaluate(BytesWritable geomref, IntWritable zoomLevel) throws BingTileException
    {
        if (geomref == null || geomref.getLength() == 0 || zoomLevel == null)
            return null;

		OGCGeometry geom = GeometryUtils.geometryFromEsriShape(geomref);
		if (geom == null)
			return null;

        ArrayList<Text> ret = new ArrayList<Text>();
        ArrayList<BingTile> tmp = BingTile.tilesCovering(geom, zoomLevel.get());

        for (BingTile b: tmp)
            ret.add(new Text(b.toQuadKey()));

        return ret;
    }
}

