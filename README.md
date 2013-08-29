RuRegistered
============

RuRegistered is an Android application which allows for tracking of the open/close status of courses for Rutgers.

##Overview
RuRegistered uses JSON requests to parse relevant course information. Simple.

##Object Structure

A list of course under a major are first parsed into a CourseList object.

CourseLists consists of Courses which themselves have relevant information such as Section arrays and Meeting Times.


TrackedCourses differ mainly in the fact that they are used for tracking purposes and only hold ebough information to track the open/closed status.

In general the hierarchy is as follows:

CourseList>Course>Sections/Meeting Times

Tracker>TrackedCourse
