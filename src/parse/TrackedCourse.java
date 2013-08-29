package parse;

/**
 * 
 * @author RaShaun
 *Courses to be tracked should be written to Tracker Objects for easy parsing.
 *TrackedCourses are NOT to be used in the initial JSON parse.
 *Only to be used for the tracking file.
 */



public class TrackedCourse {
public String major;
public String campus;
public String term;
public String level;
public String course;
public String section;

public TrackedCourse(String major, String campus, String term, String course, String section){
this.major = major;
this.campus = campus;
this.term = term;
//this.level=level;
this.course=course;
this.section=section;
}


}
