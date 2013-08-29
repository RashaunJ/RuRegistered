package parse;
//Main course object
public class Course {
public Course(){//Blank constructor

}
public String major;
public String campusCode;
public String title;
public String courseDescription;
public String courseNumber;
public String expandedTitle;
public String subject;
public String credits;
public String preReqNotes;
public String openSections;
public Section[] sections;


public static void courseInfo(Course in){//Basic output
	System.out.println("Campus Code: " + in.campusCode);
	System.out.println("Title: " + in.title);
	System.out.println("Course Description: "+in.courseDescription );
	System.out.println("Expanded Title: "+ in.expandedTitle);
	System.out.println("Subject: " + in.subject);
	System.out.println("Credits: " + in.credits);
	System.out.println("Prereq Notes: " + in.preReqNotes);
	System.out.println("Number of sections:" + in.sections.length);
	return;
}

}
