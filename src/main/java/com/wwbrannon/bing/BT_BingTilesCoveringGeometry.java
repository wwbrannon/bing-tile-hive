package com.wwbrannon.bing;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import com.esri.core.geometry.ogc.OGCGeometry;
import com.esri.hadoop.hive.GeometryUtils;

import com.wwbrannon.bing.BingTile;

@Description(
    name = "",
    value = "",
    extended = ""
)

public class BT_BingTilesCoveringGeometry extends BT_Base {
    static final Log LOG = LogFactory.getLog(BT_BingTilesCoveringGeometry.class.getName());

    public ArrayList<Text> evaluate(BytesWritable geomref, IntWritable zoomLevel)
    {
        if (geomref == null || geomref.getLength() == 0 || zoomLevel == null)
            return null;

		OGCGeometry geom = GeometryUtils.geometryFromEsriShape(geomref);
		if (geom == null)
			return null;

        ArrayList<Text> ret = new ArrayList<Text>();
        ArrayList<BingTile> tmp = BingTile.tilesCovering(geom, zoomLevel.get());

        for (BingTile b: tmp)
            ret.add(Text(b.toQuadKey()));

        return ret;
    }
}

