package it.egesuato.trainalarm;

import java.util.List;

public class NotificationMessage {
	private List<String> results;

	public NotificationMessage(List<String> results){
		this.results = results;
	}
	
	
	public String getTitle(){
		if (results == null)
			throw new NullPointerException("results in NotificationMessage is null");
		
		if (results.size() == 1){
			return "One train notification";
		} else{
			return results.size() + " train notifications";
		}
	}
	
	
	public String getDetail(){
		if (results == null)
			throw new NullPointerException("results in NotificationMessage is null");
		
		
		
		if (results.size() == 1){
			return "There is one train notification";
		} else{
			return "There are " + results.size() + " train notifications";
		}
	}
}
