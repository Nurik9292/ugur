package tm.ugur.controllers;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.*;

@Controller
public class TestController {

    private final GeometryFactory factory;

    @Autowired
    public TestController(GeometryFactory factory) {
        this.factory = factory;
    }

    @GetMapping("bus")
    public void busTest(){

    }
}
