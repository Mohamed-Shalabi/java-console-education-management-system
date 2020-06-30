package system.courses.groups;

import system.courses.Course;
import system.courses.assignments.Assignment;
import system.users.students.Student;

import java.util.ArrayList;
import java.util.Collections;

public class Group {

    private String name;
    private static int maxId = 0;
    private int id;
    private ArrayList<Student> students;
    private Course course;
    private ArrayList<Assignment> assignments;

    public Group(String name, Course course) {
        this.name = name;
        this.course = course;
        assignments = course.getAssignments();
        students = new ArrayList<>();
        generateId();
    }

    public Group(String name, Course course, Student... students) {
        this(name, course);
        try {
            addStudents(students);
        } catch (Exception ignored) {
        }
    }

    public void addStudents(Student... students) {
        Collections.addAll(this.students, students);
    }

    private void generateId() {
        maxId++;
        id = maxId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public Course getCourse() {
        return course;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("group name : ").append(getName()).append("\n");
        students.forEach(student -> {
            s.append("student : ");
            if (student != null) {
                s.append(student.getName()).append("\n");
            }
        });
        return s.toString();
    }

    public void addStudent(Student student) {
        getStudents().add(student);
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    @Override
    public boolean equals(Object obj) {
        return getId() == ((Group) obj).getId();
    }

    public String toString(boolean showStudents) {
        StringBuilder s = new StringBuilder();
        s.append("name : ").append(getName()).append("\ncourse : ").append(getName());
        if (showStudents) {
            s.append("\nStudents : ");
            if (getStudents() != null) {
                getStudents().forEach(student -> s.append("\n").append(student.getName()));
            } else {
                s.append("No students");
            }
        }
        return s.toString();
    }
}
