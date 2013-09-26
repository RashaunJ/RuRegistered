package parse;

/**
 * 
 * @author RaShaun
 *@param Courses to be tracked should be written to TrackedCourse Objects for easy parsing.
 *@param TrackedCourses are NOT to be used in the initial JSON parse.
 *@param Only to be used for the tracking file.
 */



public class TrackedCourse {
public String major;
public String campus;
public String term;
public String course;
public String section;

public TrackedCourse(String major, String campus, String term, String course, String section){
this.major = major;
this.campus = campus;
this.term = term;
this.course=course;
this.section=section;
}


}
