package system.users.doctors;

import system.courses.Course;
import system.courses.assignments.Answer;
import system.courses.assignments.Assignment;
import system.courses.assignments.Grade;
import system.users.User;
import utils.Pair;

import java.util.ArrayList;


public class Doctor extends User {

    private DoctorCourses courses;

    public Doctor(String name, String username, String password, String email) {
        super(name, username, password, email);
        setUserType(UserType.DOCTOR);
        courses = new DoctorCourses();
    }

    public boolean addAssignmentToCourse(Assignment assignment, Course course) {
        return courses.addAssignmentToCourse(assignment, course);
    }

    public boolean addAssignmentToCourse(String name, Course course, String... questions) {
        return courses.addAssignmentToCourse(name, course, questions);
    }

    public Course getCourse(String codeOrName) {
        return courses.getCourse(codeOrName);
    }

    public ArrayList<Course> getCourses() {
        return courses.getCourses();
    }

    private DoctorCourses getDoctorCourses() {
        return courses;
    }

    public boolean createCourse(String name, String code) {
        return courses.createCourse(name, code, this);
    }

    public ArrayList<Assignment> getAssignments(Course course) {
        return courses.getAssignments(course);
    }

    private class DoctorCourses {

        private ArrayList<Course> courses = new ArrayList<>();

        ArrayList<Course> getCourses() {
            return courses;
        }

        boolean createCourse(String name, String code, Doctor doctor) {
            Pair pair = Course.createCourse(name, code, doctor);
            if ((Boolean) pair.getSecond()) {
                courses.add((Course) pair.getFirst());
                return true;
            }
            return false;
        }

        boolean addAssignmentToCourse(Assignment assignment, Course course) {
            return course.addAssignment(assignment);
        }

        boolean addAssignmentToCourse(String name, Course course, String... questions) {
            return course.addAssignment(name, Doctor.this, questions);
        }

        ArrayList<Assignment> getAssignments(Course course) {
            return course.getAssignments();
        }

        void submitGrade(Answer answer, Grade grade) {
            answer.getAssignment().submitGrade(answer, grade, Doctor.this);
        }

        private Course getCourse(String codeOrName) {
            for (Course course : getCourses()) {
                if (course.getName().equals(codeOrName) || course.getCode().equals(codeOrName)) {
                    return course;
                }
            }
            return null;
        }
    }

}
