package system.users.students;

import system.courses.Course;
import system.courses.assignments.Answer;
import system.courses.assignments.Assignment;
import system.courses.assignments.Grade;
import system.courses.groups.Group;
import system.users.User;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;


public class Student extends User {

    private StudentCourses courses;

    public boolean registerInCourse(Course course) {
        return courses.registerInCourse(course);
    }

    public Student(String name, String username, String password, String email) {
        super(name, username, password, email);
        setUserType(UserType.STUDENT);
        courses = new StudentCourses();
    }

    public boolean submitAnswer(Assignment assignment, Answer answer) {
        return courses.submitAnswer(assignment, answer);
    }

    public LinkedHashMap<Course, Boolean> getCourses() {
        return courses.getCourses();
    }

    public LinkedHashMap<Group, Assignment> getAssignments() {
        return courses.getAssignments();
    }

    public Course getCourse(String codeOrName) {
        return courses.getCourse(codeOrName);
    }

    public void addGrade(Assignment assignment, Grade grade) {
        courses.addGrade(assignment, grade);
    }

    public LinkedHashMap<Group, Grade> getGrades() {
        return courses.getGrades();
    }

    private class StudentCourses {

        private LinkedHashMap<Course, Boolean> courses;
        private LinkedHashMap<Group, Assignment> assignments;
        private LinkedHashMap<Group, Grade> grades;


        boolean registerInCourse(Course course) {
            Utils.addGeneralToMap(courses, course, false);
            System.out.println("Please select a group to access the assignments " + " in the course " + course.getName());

            ArrayList<Group> baseGroups = course.getGroups();

            final int[] i = {0};

            if (baseGroups.size() == 0) {
                System.out.println("No groups in this course yet");
                System.out.println("course added successfully");
                course.addStudent(Student.this);
                System.out.println("course added successfully");

            } else {

                baseGroups.forEach(group -> {
                    i[0]++;
                    System.out.print("" + i[0] + ". " + group.toString());
                });

                try {
                    int groupNumber = new Scanner(System.in).nextInt();
                    while (groupNumber <= 0 || groupNumber > i[0]) {
                        System.out.println("Wrong number, please try again.");
                        groupNumber = new Scanner(System.in).nextInt();
                    }
                    final int finalGroupNumber = groupNumber - 1;
                    course.getAssignments().forEach(assignment -> {
                        assignments.put(baseGroups.get(finalGroupNumber), assignment);
                    });
                    baseGroups.get(finalGroupNumber).addStudent(Student.this);
                    course.addStudent(Student.this);
                    System.out.println("course added successfully");
                } catch (Exception ignored) {
                }

            }

            return true;
        }

        private LinkedHashMap<Course, Boolean> getCourses() {
            return courses;
        }

        private LinkedHashMap<Group, Assignment> getAssignments() {
            return assignments;
        }

        private boolean submitAnswer(Assignment assignment, Answer answer) {
            return assignment.submitAnswer(answer);
        }

        private Course getCourse(String codeOrName) {
            for (Course course : getCourses().keySet()) {
                if (course.getName().equals(codeOrName) || course.getCode().equals(codeOrName)) {
                    return course;
                }
            }
            return null;
        }

        StudentCourses() {
            courses = new LinkedHashMap<>();
            assignments = new LinkedHashMap<>();
            grades = new LinkedHashMap<>();
        }

        private void addGrade(Assignment assignment, Grade grade) {
            getAssignments().forEach((group, assignment1) -> {
                if (assignment1.getCourse().getCode().equals(assignment.getCourse().getCode())) {
                    getGrades().put(group, grade);
                }
            });
        }

        private LinkedHashMap<Group, Grade> getGrades() {
            return grades;
        }
    }
}
