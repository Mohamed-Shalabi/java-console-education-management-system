package navigation;

import system.courses.Course;
import system.courses.assignments.Answer;
import system.courses.assignments.Assignment;
import system.courses.assignments.Grade;
import system.courses.groups.Group;
import system.users.User;
import system.users.doctors.Doctor;
import system.users.students.Student;

import java.util.ArrayList;
import java.util.LinkedHashMap;

final class Repository {

    private Repository() {
    }

    static boolean logIn(String username, String password, int id) {
        return User.logIn(username, password, id);
    }

    static User getCurrentUser() {
        return User.getCurrentUser();
    }

    static boolean createUser(String name, String username, String password, String email, User.UserType userType) {
        return User.createUser(name, username, password, email, userType);
    }

    static ArrayList<Course> getDoctorCourses() {
        return ((Doctor) getCurrentUser()).getCourses();
    }

    static boolean createCourse(String name, String code) {
        return ((Doctor) getCurrentUser()).createCourse(name, code);
    }

    static Course getCourse(String codeOrName) {
        return Course.getCourse(codeOrName, getCurrentUser());
    }

    static void logOut() {
        User.logOut();
    }

    static boolean doctorAddAssignment(String name, Course course, String... questions) {
        Doctor doctor = (Doctor) getCurrentUser();
        return doctor.addAssignmentToCourse(name, course, questions);
    }

    static boolean doctorAddGroup(String name, Course course, Student... students) {
        return course.createGroup(name, students);
    }

    static ArrayList<Student> getCourseStudents(Course course) {
        return course.getStudents();
    }

    static ArrayList<Group> getCourseGroups(Course course) {
        return course.getGroups();
    }

    static Student getCourseStudent(Course course, int id) {
        return course.getStudent(id);
    }

    static ArrayList<Assignment> getCourseAssignments(Course course) {
        return course.getAssignments();
    }

    static boolean submitGrade(Float grade, Answer answer) {
        return answer.getAssignment().submitGrade(answer, new Grade(grade), (Doctor) getCurrentUser());
    }

    static ArrayList<Course> studentGetAllCourses() {
        return Course.getCourses();
    }

    static LinkedHashMap<Course, Boolean> studentGetAllCoursesWithRegistrationState() {
        Student student = (Student) getCurrentUser();
        LinkedHashMap<Course, Boolean> map = new LinkedHashMap<>();
        Course.getCourses().forEach(course -> {
            if (course.getStudents().contains(student)) {
                map.put(course, true);
            } else {
                map.put(course, false);
            }
        });
        return map;
    }

    static Group getCourseUserGroup(Course course) {
        for (Group group : course.getGroups()) {
            Student student = (Student) getCurrentUser();
            if (group.getStudents().contains(student)) {
                return group;
            }
        }
        return null;
    }

    static void studentJoinGroup(Group group) {
        group.addStudent((Student) getCurrentUser());
    }

    static boolean studentSubmitAnswer(Assignment assignment, Answer answer) {
        return ((Student) getCurrentUser()).submitAnswer(assignment, answer);
    }

    static boolean studentRegisterInCourse(Course course) {
        return ((Student) getCurrentUser()).registerInCourse(course);
    }

    static ArrayList<Course> studentGetRegisteredCourses() {
        Student student = (Student) getCurrentUser();
        ArrayList<Course> courses = new ArrayList<>();
        Course.getCourses().forEach(course -> {
            if (course.getStudents().contains(student)) {
                courses.add(course);
            }
        });
        return courses;
    }

    static LinkedHashMap<Assignment, Grade> getGradesReport() {
        LinkedHashMap<Assignment, Grade> report = new LinkedHashMap<>();
        Student student = (Student) getCurrentUser();
        LinkedHashMap<Group, Assignment> assignments = student.getAssignments();
        for (Group group : assignments.keySet()) {
            report.put(assignments.get(group), student.getGrades().get(group));
        }
        return report;
    }

    static User.UserType getCurrentUserType() {
        switch (getCurrentUser().getUserType()) {
            case DOCTOR:
                return User.UserType.DOCTOR;
            case STUDENT:
                return User.UserType.STUDENT;
            default:
                return null;
        }
    }
}
