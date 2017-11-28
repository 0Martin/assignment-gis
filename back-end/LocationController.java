package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Controller
public class LocationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/location")
    public
    @ResponseBody
    List<Location> getLocations(@RequestParam String amenity, @RequestParam String locationX, @RequestParam String locationY) {
    	List<Location> locations = new ArrayList<Location>();
    	HashMap<String, List<String>> amenityHashMap = new HashMap<>();
    	amenityHashMap.put("medical", Arrays.asList("pharmacy", "first_aid", "doctors", "hospital"));
    	amenityHashMap.put("bicycle", Arrays.asList("bicycle_repair_station", "bicycle_rental", "bicycle_parking"));
    	amenityHashMap.put("food", Arrays.asList("fast_food", "restaurant", "pub"));
    
    	List<String> searchesForAmenity = amenityHashMap.get(amenity);
    	System.out.println(searchesForAmenity.size());
    	for(int index=0; index<searchesForAmenity.size(); index++) {
        String query = "SELECT ST_X(ST_Transform(way, 4326)) AS x, "
        		+ "ST_Y(ST_Transform(way, 4326)) AS y, name, amenity"
        		+ " from planet_osm_point where amenity = '" + searchesForAmenity.get(index) + "'"
                + " and name is not null"
                + " order by "
                + "ST_DISTANCE(ST_Transform(way, 2100), ST_Transform(ST_GeomFromText('POINT("
                + locationY +" " + locationX
                +")',4326), 2100))/1000 asc"
                + " LIMIT 10;";
        System.out.println(query);

        final int i = index;
        locations.addAll(  jdbcTemplate.query(query, new RowMapper<Location>() {
            public Location mapRow(ResultSet rs, int rowNum) throws SQLException {
                Location location = new Location();
                location.setX(rs.getDouble("x"));
                location.setY(rs.getDouble("y"));
                location.setName(rs.getString("name"));
                location.setAmenity(rs.getString("amenity"));
                location.setIndex(i);
                return location;
            }
        }));
        System.out.println("Size: " +locations.size());
    	}
        
        return locations;
        
    }

}
