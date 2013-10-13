package it.egesuato.trainalarm;


public class NotificationMessage {
	private String result;

	public NotificationMessage(String result){
		this.result = result;
	}
	
	
	public String getTitle(){
		return "Train alarm notification";
	}
	
	
	public String getDetail(){
		return result;
		/*
		if (results.size() == 1){
			return buff.toString();//"There is one train notification";
		} else{
			return "There are " + results.size() + " train notifications";
		}
		*/
	}
}
