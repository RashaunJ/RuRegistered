package parse;

/**
 * 
 * @author RaShaun
 *
 * Should be at least 1 section per course.
 *
 */

public class Section {
	public Section(){//Blank Constructor
	}

	public String index;
	public boolean openStatus;
	public MeetingTimes[] meetingTimes;
	
	/**
	 * Retrieves all the campuses that the particular section is held on.
	 * Will not show more than one instance of a campus.
	 */
public static String getCampuses(Section in){

	String Campuses = "Campuses: ";

	for(int i =0;i<in.meetingTimes.length;i++){
		if(!Campuses.contains(in.meetingTimes[i].campusName)){//Remove redundant entries due to recitation etc.
		Campuses = Campuses.concat(in.meetingTimes[i].campusName+ ",");
		}
	}
	
	
	return Campuses;
}
/**
 * Parses and returns meeting times.
 * @param in
 * @return
 */
public static String getMeetingTimes(Section in){
	String Times = "Meeting Times: ";

	for(int i =0;i<in.meetingTimes.length;i++){
		String start = in.meetingTimes[i].startTime;
		String end = in.meetingTimes[i].endTime;
		String day = in.meetingTimes[i].meetingDay;
		if(in.meetingTimes[i].pmCode.equals("A")){// Morning/Night class
		start=start.substring(0,2)+":"+start.substring(2,start.length());
		end=end.substring(0,2)+":"+end.substring(2,end.length());
		Times = Times.concat("\n"+day+ " "+start+"AM"+ "-"+end+" ");
		}
		else{
			start=start.substring(0,2)+":"+start.substring(2,start.length());
			end=end.substring(0,2)+":"+end.substring(2,end.length());
			Times = Times.concat("\n"+day+" " +start+"PM"+ "-"+end+" ");
		}
	}
	return Times;
}
}
