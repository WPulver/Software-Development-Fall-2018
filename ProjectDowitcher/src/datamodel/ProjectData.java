
package datamodel;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import datamodel.AnimalTrack;
import datamodel.Video;

public class ProjectData {
	private Video video;
	private List<AnimalTrack> tracks;
	private List<AnimalTrack> unassignedSegments;
	private int chickNum;
	//private List<AnimalTrack> manualTrackSegments;
	
	public ProjectData(String videoFilePath) throws FileNotFoundException {
		video = new Video(videoFilePath);
		tracks = new ArrayList<>();
		unassignedSegments = new ArrayList<>();
	}

	public Video getVideo() {
		return video;
	}
	
	public List<AnimalTrack> getTracks() {
		return tracks;
	}

	public List<AnimalTrack> getUnassignedSegments() {
		return unassignedSegments;
	}

	public void exportCSV(File outFile) {
		
	}
	
	
	public void saveProject(File projectFile) {
		
	}
	
	public void setChickNum(int numberOfChick ) {
		this.chickNum = numberOfChick;
	}
	
	public int getChickNum() {
		return this.chickNum;
	}
	
	
	public void saveToFile(File saveFile) throws FileNotFoundException {
		String json = toJSON();
		PrintWriter out = new PrintWriter(saveFile);
		out.print(json);
		out.close();
	}
	
	public String toJSON() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();		
		return gson.toJson(this);
	}
	
	public static ProjectData loadFromFile(File loadFile) throws FileNotFoundException {
		String json = new Scanner(loadFile).useDelimiter("\\Z").next();
		return fromJSON(json);
	}
	
	public static ProjectData fromJSON(String jsonText) throws FileNotFoundException {
		Gson gson = new Gson();
		ProjectData data = gson.fromJson(jsonText, ProjectData.class);
		data.getVideo().connectVideoCapture();
		return data;
	}
	

}

