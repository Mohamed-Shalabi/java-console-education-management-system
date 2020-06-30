package system.courses;

import system.courses.assignments.Assignment;
import system.courses.groups.Group;
import system.users.User;
import system.users.doctors.Doctor;
import system.users.students.Student;
import utils.Pair;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;

public class Course implements Comparable {

    private String name;
    private String code;
    private Doctor doctor;
    private ArrayList<Course> preRequests;
    private ArrayList<Student> students;
    private ArrayList<Group> groups;
    private ArrayList<Assignment> assignments;
    private static ArrayList<Course> courses = new ArrayList<>();

    private Course(String name, String code, Doctor doctor) {
        this.name = name;
        this.code = code;
        this.doctor = doctor;
        preRequests = new ArrayList<>();
        students = new ArrayList<>();
        assignments = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public static Pair<Course, Boolean> createCourse(String name, String code, Doctor doctor) {
        Course course = new Course(name, code, doctor);
        boolean stop = Utils.arrayListContains(courses, course);
        if (stop) {
            course = null;
            return new Pair<>(null, false);
        } else {
            courses.add(course);
            Collections.sort(courses);
            return new Pair<>(course, true);
        }
    }

    public void addStudent(Student student) {
        getStudents().add(student);
    }

    public void addPreRequests(Course preRequestedCourse) {
        getPreRequests().add(preRequestedCourse);
    }

    public boolean addAssignment(Assignment assignment) {
        return getAssignments().add(assignment);
    }

    public boolean addAssignment(String name, Doctor doctor, String... questions) {
        Assignment assignment = new Assignment(name, this, doctor, questions);
        return addAssignment(assignment);
    }

    public boolean createGroup(String name, Student... students) {
        for (Group group : getGroups()) {
            if (group.getName().equals(name)) {
                return false;
            }
        }
        Group group = new Group(name, this, students);
        getGroups().add(group);
        for (Assignment assignment : getAssignments()) {
            assignment.addGroup(group);
        }
        return true;
    }

    public static Course getCourse(String codeOrName, User user) {
        if (user.getUserType() == User.UserType.DOCTOR) {
            return ((Doctor) user).getCourse(codeOrName);
        } else {
            return ((Student) user).getCourse(codeOrName);
        }
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public ArrayList<Course> getPreRequests() {
        return preRequests;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public ArrayList<Group> getGroups() {
        return groups;
    }

    public static ArrayList<Course> getCourses() {
        return courses;
    }

    @Override
    public int compareTo(Object o) {
        return getCode().compareTo(((Course) o).getCode());
    }

    @Override
    public boolean equals(Object obj) {
        return (
                getCode().equals(((Course) obj).getCode()) ||
                        getName().equals(((Course) obj).getCode())
        );
    }

    @Override
    public String toString() {
        return "\ncourse name : " + getName() +
                "\ncourse code : " + getCode() +
                "\ngroups : \n" + groupsString(true);
    }

    private String groupsString(boolean showStudents) {
        StringBuilder s = new StringBuilder();
        getGroups().forEach(group -> {
            s.append(group.toString(showStudents));
            s.append("\n");
        });
        return s.toString();
    }

    public String toStringNameAndCodeOnly() {
        return " course name : " + getName() + "    course code : " + getCode();
    }

    public Student getStudent(int id) {
        for (Student student : getStudents()) {
            if (student.getId() == id) {
                return student;
            }
        }
        return null;
    }
}
