package com.example.demo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WayController {

	    @Autowired
	    private JdbcTemplate jdbcTemplate;

	    @RequestMapping(value = "/circle")
	    public
	    @ResponseBody
	    ArrayList<Way> getCyrcle(@RequestParam String locationX, @RequestParam String locationY) {
	    	String query = "with cycle_circles as ( \n" + 
	    			"with recursive cycleway(way, origin_id, ids, depth, length) as (\n" + 
	    			"\n" + 
	    			"(select way, id, array[id], 1, ST_length(way)\n" + 
	    			"from planet_osm_line \n" + 
	    			"where bicycle in ('yes', 'official', 'tollerated', 'permissive', 'designated', 'use_sidepath')\n" + 
	    			"or  highway='cycleway'\n" + 
	    			"or route = 'bicycle'\n" + 
	    			"order by ST_DISTANCE(ST_Transform(way, 2100), ST_Transform(ST_GeomFromText('POINT("+locationY+" "+locationX+")',4326), 2100))/1000,\n" + 
	    			"ST_Length(way)\n" + 
	    			"limit 1)\n" + 
	    			"\n" + 
	    			"union all\n" + 
	    			"\n" + 
	    			"(select l.way, cw.origin_id ,cw.ids || l.id, cw.depth + 1, length + ST_Length(l.way) \n" + 
	    			"from cycleway cw join planet_osm_line l on ST_Touches(l.way, cw.way)\n" + 
	    			"where cw.depth < 7\n" + 
	    			"and (depth < 2 or cw.ids[array_length(cw.ids, 1)] != cw.origin_id)\n" + 
	    			"and NOT l.id = ANY(ids[2:array_length(cw.ids, 1)])\n" + 
	    			"and (bicycle in ('yes', 'official', 'tollerated', 'permissive', 'designated', 'use_sidepath')\n" + 
	    			"or  highway='cycleway'\n" + 
	    			"or route = 'bicycle'))\n" + 
	    			"\n" + 
	    			") select * from cycleway \n" + 
	    			"where cycleway.ids[array_length(ids, 1)] = cycleway.origin_id\n" + 
	    			"and depth > 2\n" + 
	    			"order by length desc limit 1)\n" + 
	    			"SELECT unnest((ST_DumpPoints(ST_Transform(line.way,4326))).path) as path,\n" + 
	    			"st_x((ST_DumpPoints(ST_Transform(line.way,4326))).geom) as x,\n" + 
	    			"st_y((ST_DumpPoints(ST_Transform(line.way,4326))).geom) as y,\n" + 
	    			"depth from planet_osm_line line, cycle_circles\n" + 
	    			"where line.id = ANY(cycle_circles.ids)";
	    	
	    	return getWays(query);
	    	
	}
	    
	    @RequestMapping(value = "/way")
	    public
	    @ResponseBody
	    ArrayList<Way> getWay(@RequestParam String locationX, @RequestParam String locationY, @RequestParam String length) {
	    	String query = "with cycle_circle as ( " +
	    			"with recursive cycleway(way, origin_id, ids, depth, length) as (\n" + 
	    			"\n" + 
	    			"(select way, id, array[id], 1, ST_length(way)\n" + 
	    			"from planet_osm_line \n" + 
	    			"where bicycle in ('yes', 'official', 'tollerated', 'permissive', 'designated', 'use_sidepath')\n" + 
	    			"or  highway='cycleway'\n" + 
	    			"or route = 'bicycle'\n" + 
	    			"order by ST_DISTANCE(ST_Transform(way, 2100), ST_Transform(ST_GeomFromText('POINT("+ locationY + " " +locationX + ")',4326), 2100))/1000,\n" + 
	    			"ST_Length(way)\n" + 
	    			"limit 1)\n" + 
	    			"\n" + 
	    			"union all\n" + 
	    			"\n" + 
	    			"(select l.way, cw.origin_id ,cw.ids || l.id, cw.depth + 1, length + ST_Length(l.way) \n" + 
	    			"from cycleway cw join planet_osm_line l on ST_Touches(l.way, cw.way)\n" + 
	    			"where cw.depth < 7\n" + 
	    			"and (depth < 2 or cw.ids[array_length(cw.ids, 1)] != cw.origin_id)\n" + 
	    			"and NOT l.id = ANY(ids[2:array_length(cw.ids, 1)])\n" + 
	    			"and (bicycle in ('yes', 'official', 'tollerated', 'permissive', 'designated', 'use_sidepath')\n" + 
	    			"or  highway='cycleway'\n" + 
	    			"or route = 'bicycle'))\n" + 
	    			"\n" + 
	    			") select * from cycleway \n" + 
	    			"--where cycleway.ids[array_length(ids, 1)] = cycleway.origin_id\n" + 
	    			"where depth > 2\n" + 
	    			"order by ABS(length - " + length + 	") asc\n" +
	    			"limit 1)\n" + 
	    			"SELECT unnest((ST_DumpPoints(ST_Transform(line.way,4326))).path) as path,\n" + 
	    			"st_x((ST_DumpPoints(ST_Transform(line.way,4326))).geom) as x,\n" + 
	    			"st_y((ST_DumpPoints(ST_Transform(line.way,4326))).geom) as y,\n" + 
	    			"depth \n" + 
	    			"from planet_osm_line line , cycle_circle cc where id = ANY (cc.ids)";
	    	
	    	return getWays(query);
	    	
	}
	 
	    ArrayList<Way> getWays(String query){
	    	List<WayPoint> wayPoints;

	    	wayPoints = jdbcTemplate.query(query, new RowMapper<WayPoint>() {
	            public WayPoint mapRow(ResultSet rs, int rowNum) throws SQLException {
	                WayPoint wayPoint = new WayPoint();
	                Point point = new Point();
	                point.setX(rs.getDouble("x"));
	                point.setY(rs.getDouble("y"));
	                wayPoint.setPoint(point);
	                wayPoint.setPath(1);
	                wayPoint.setPath(rs.getInt("path"));
	                return wayPoint;
	            }
	        });
	    	
	    	System.out.println(wayPoints.size());
	    	
	    	ArrayList<Way> ways = new ArrayList<>();
	    	Way way = null;
	    	for(WayPoint wayPoint: wayPoints) {
	    		System.out.println("TED");
	    		if(wayPoint.getPath() == 1) {
	    			System.out.println("tu");
	    			if(way != null)
	    				ways.add(way);
	    			way = new Way();
	    		}
	    		if(way != null) {
	    			way.addPoint(wayPoint.getPoint());
	    			System.out.println("tu2");
	    		}
	    	}
	    	ways.add(way);
	    	
	    	
	    	System.out.println(query);
	    	System.out.println(ways.size());
	    return ways;
	    }
	    

}
