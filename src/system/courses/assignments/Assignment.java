package system.courses.assignments;

import system.courses.Course;
import system.courses.groups.Group;
import system.users.doctors.Doctor;
import utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class Assignment {

    private String name;
    private Course course;
    private Doctor doctor;
    private static int maxId = 0;
    private int id;
    private ArrayList<String> questions;
    private LinkedHashMap<Group, Grade> grades;
    private LinkedHashMap<Group, Answer> answers;

    public Assignment(String name, Course course, Doctor doctor, String... questions) {
        this.name = name;
        this.course = course;
        this.doctor = doctor;
        id = generateId();
        this.questions = new ArrayList<>();
        grades = new LinkedHashMap<>();
        answers = new LinkedHashMap<>();
        for (Group group : course.getGroups()) {
            getGrades().put(group, null);
            getAnswers().put(group, null);
        }
        getQuestions().addAll(Arrays.asList(questions));
    }

    private int generateId() {
        maxId++;
        return maxId;
    }

    public boolean addQuestion(String question) {
        return Utils.addGeneralToArrayList(getQuestions(), question);
    }

    public boolean submitAnswer(Answer answer) {
        if (getAnswers().containsKey(answer.getGroup()) && getAnswers().get(answer.getGroup()) != null) {
            return false;
        } else if (getAnswers().containsKey(answer.getGroup())) {
            getAnswers().put(answer.getGroup(), answer);
            return true;
        } else {
            return false;
        }
    }

    public boolean submitGrade(Answer answer, Grade grade, Doctor doctor) {
        if (getAnswers().containsKey(answer.getGroup()) && doctor.equals(this.getDoctor())) {
            getAnswers().put(answer.getGroup(), answer);
            getGrades().put(answer.getGroup(), grade);
            answer.getGroup().getStudents().forEach(student -> {
                if (student != null) {
                    student.addGrade(Assignment.this, grade);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    public Grade getGradeByGroup(Group group) {
        return grades.get(group);
    }

    @Override
    public String toString() {

        return "name : " + getName() + "\n" +
                questionsString() +
                gradesString();
    }

    private String questionsString() {
        StringBuilder stringBuilder = new StringBuilder();
        int[] i = {0};
        getQuestions().forEach(s -> {
            i[0]++;
            stringBuilder.append("Q").append(i[0]).append(" : ").append(s).append("\n");
        });
        return stringBuilder.toString();
    }


    private String gradesString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean[] booleans = {false};
        grades.forEach((group, grade) -> {
            if (grade != null && group != null) {
                stringBuilder.append("group name : ").append(group.getName()).append("\tgrade : ").append(grade.toString()).append("\n");
                booleans[0] = true;
            }
        });
        if (booleans[0]) {
            return stringBuilder.toString();
        } else {
            return "no grades available.";
        }
    }

    public String getName() {
        return name;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public ArrayList<String> getQuestions() {
        return questions;
    }

    public LinkedHashMap<Group, Grade> getGrades() {
        return grades;
    }

    public Course getCourse() {
        return course;
    }

    public LinkedHashMap<Group, Answer> getAnswers() {
        return answers;
    }

    public ArrayList<Group> getGroups() {
        return course.getGroups();
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        Assignment assignment = (Assignment) obj;
        return getName().equals(assignment.getName()) &&
                getCourse().equals(assignment.getCourse());
    }

    public void addGroup(Group group) {
        for (Group group1 : getGroups()) {
            if (group1.getId() == group.getId()) {
                getGrades().put(group, null);
                getAnswers().put(group, new Answer(group, this));
            }
        }
    }
}
