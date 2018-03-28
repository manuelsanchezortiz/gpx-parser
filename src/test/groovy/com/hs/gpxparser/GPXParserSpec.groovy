package com.hs.gpxparser

import com.hs.gpxparser.modal.GPX
import com.hs.gpxparser.modal.Waypoint
import groovy.transform.CompileStatic
import spock.lang.Specification

class GPXParserSpec extends Specification {

    @CompileStatic
    def "load GPX"(){
        setup:
            GPXParser p = new GPXParser()
        when:
            InputStream is= this.class.getResourceAsStream("Collbato-vallirana.gpx")

            GPX gpx = p.parseGPX(is)

            println gpx
        then:
        gpx
    }

    //@CompiMath.abs(it.latitude-searchLatitude)leStatic
    def "Set start"(){
        setup:
        GPXParser p = new GPXParser()
        InputStream is= this.class.getResourceAsStream("Collbato-vallirana.gpx")

        GPX gpx = p.parseGPX(is)
        when:
        List<Waypoint> waypointList= []

        //only one track, i hope
        double searchLongitude= 1.810378
        double searchLatitude= 41.562642
        double epsilon= 0.0001

        //println epsilon
        //println "Matches: ${gpx.tracks[0].trackSegments[0].waypoints.findAll { Waypoint it -> Math.abs(it.latitude-searchLatitude) < epsilon && Math.abs(it.longitude-searchLongitude) < epsilon }}"

        //gpx.tracks[0].trackSegments[0].waypoints.each {
        //    println "Long: ${it.longitude}/${Math.abs(it.longitude-searchLongitude)}, Lat: ${it.latitude}/${Math.abs(it.latitude-searchLatitude)}"
        //}

        int idx= gpx.tracks[0].trackSegments[0].waypoints.findIndexOf { Waypoint it -> Math.abs(it.latitude-searchLatitude) < epsilon && Math.abs(it.longitude-searchLongitude) < epsilon }
        println idx

        int size= gpx.tracks[0].trackSegments[0].waypoints.size()
        (0..size-1).each {
            waypointList.add( gpx.tracks[0].trackSegments[0].waypoints[ (it + idx) % size ] )
        }
        gpx.tracks[0].trackSegments[0].waypoints= waypointList.reverse()

        GPXWriter w= new GPXWriter()

        w.writeGPX(gpx, new FileOutputStream('c:/temp/Collbato-Vallirana_start_rev.gpx') )

        then:
        idx > 0
        noExceptionThrown()
    }
}
