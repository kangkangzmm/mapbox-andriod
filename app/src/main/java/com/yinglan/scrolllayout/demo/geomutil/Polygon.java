package com.yinglan.scrolllayout.demo.geomutil;

import android.graphics.Color;

import com.mapbox.geojson.Point;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    static final int BLUE_COLOR = Color.parseColor("#3bb2d0");
    static final int RED_COLOR = Color.parseColor("#AF0000");

    //多边形的点的集合
    static final List<Point> POLYGON_COORDINATES = new ArrayList<Point>() {
        {
            add(Point.fromLngLat(55.30122473231012, 25.26476622289597));
            add(Point.fromLngLat(55.29743486255916, 25.25827212207261));
            add(Point.fromLngLat(55.28978863411328, 25.251356725509737));
            add(Point.fromLngLat(55.300027931336984, 25.246425506635504));
            add(Point.fromLngLat(55.307474692951274, 25.244200378933655));
            add(Point.fromLngLat(55.31212891895635, 25.256408010450187));
            add(Point.fromLngLat(55.30774064871093, 25.26266169122738));
            add(Point.fromLngLat(55.301357710197806, 25.264946609615492));
            add(Point.fromLngLat(55.30122473231012, 25.26476622289597));
        }
    };


    //孔洞点的集合
    static final List<List<Point>> HOLE_COORDINATES = new ArrayList<List<Point>>() {
        {
            add(new ArrayList<>(new ArrayList<Point>() {
                {
                    add(Point.fromLngLat(55.30084858315658, 25.256531695820797));
                    add(Point.fromLngLat(55.298280197635705, 25.252243254705405));
                    add(Point.fromLngLat(55.30163885563897, 25.250501032248863));
                    add(Point.fromLngLat(55.304059065092645, 25.254700192612702));
                    add(Point.fromLngLat(55.30084858315658, 25.256531695820797));
                }
            }));
            add(new ArrayList<>(new ArrayList<Point>() {
                {
                    add(Point.fromLngLat(55.30173763969924, 25.262517391695198));
                    add(Point.fromLngLat(55.301095543307355, 25.26122200491396));
                    add(Point.fromLngLat(55.30396028103232, 25.259479911263526));
                    add(Point.fromLngLat(55.30489872958182, 25.261132667394975));
                    add(Point.fromLngLat(55.30173763969924, 25.262517391695198));
                }
            }));
        }
    };

}
