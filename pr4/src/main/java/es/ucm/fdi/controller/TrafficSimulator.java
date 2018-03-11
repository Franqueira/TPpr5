package es.ucm.fdi.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

import es.ucm.fdi.events.Event;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.simobject.Junction;
import es.ucm.fdi.simobject.Road;
import es.ucm.fdi.util.MultiTreeMap;

public class TrafficSimulator {
	private RoadMap objects;
	private MultiTreeMap<Integer,Event> events;
	private int timeCounter;
	private OutputStream out;
	
	public TrafficSimulator(OutputStream out) {
		this.objects = new RoadMap();
		this.events = new MultiTreeMap<>();
		this.timeCounter = 0;
		this.out = out;
	}
	
	public void addEvent(Event e){
		if(e.getTime() < timeCounter) throw new IllegalArgumentException();
		events.putValue(e.getTime(), e);
	}
	
	public void run(int numSteps) throws IOException{
		int timeLimit = timeCounter + numSteps - 1;
		while(timeCounter <= timeLimit){
			List<Event> nowEvents = events.get(timeCounter);
			if(nowEvents!=null)
				for(Event e : nowEvents) e.execute(objects);
			List<Road> roads = objects.getRoads();
			for(Road r : roads) r.avanza();
			List<Junction> junctions = objects.getJunctions();
			for(Junction j : junctions) j.avanza();
			timeCounter++;
			generateReport();
		}
	}
	
	public void generateReport(){
		Ini report = new Ini();
		
	}
}
