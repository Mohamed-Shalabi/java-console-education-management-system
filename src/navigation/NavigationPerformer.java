package navigation;

import system.courses.Course;
import system.courses.assignments.Answer;
import system.courses.assignments.Assignment;
import system.courses.assignments.Grade;
import system.courses.groups.Group;
import system.users.User;
import system.users.doctors.Doctor;
import system.users.students.Student;
import utils.Pair;
import utils.Utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.util.Stack;


public class NavigationPerformer {

    private static Navigation welcome, logIn, signUp;
    private static Navigation doctorPage, studentPage;
    private static Navigation doctorListCourses, doctorSelectCourse, doctorCreateCourse, doctorViewCourse;
    private static Navigation doctorCreateAssignment, doctorCreateGroup;
    private static Navigation doctorViewAssignments, doctorViewAssignment;
    private static Navigation doctorSubmitGrade, doctorViewGrades, doctorViewAnswers;
    private static Navigation studentViewAllCourses, studentViewRegisteredCourses, studentGradesReport;
    private static Navigation studentViewCourse, studentViewCourseAssignmentsAndSubmitAnswer;
    private static Navigation studentRegisterInCourse;

    private static Object argument = null;

    public static void start() {

        makeDummyData();

        buildNavigation();

        Navigation currentDestination;
        Stack<Navigation> stack = new Stack<>();
        stack.push(welcome);

        while (!stack.isEmpty()) {

            currentDestination = stack.peek();
            Navigation nextDestination = currentDestination.nextDestination(argument);
            if (nextDestination == null) {
                stack.pop();
                while (
                        !stack.isEmpty() &&
                                (
                                        stack.peek() == logIn || stack.peek() == signUp ||
                                                stack.peek() == doctorSelectCourse || stack.peek() == doctorViewAnswers ||
                                                stack.peek() == doctorViewAssignment || stack.peek() == doctorSelectCourse ||
                                                stack.peek() == doctorListCourses ||
                                                stack.peek() == studentViewAllCourses || stack.peek() == studentViewCourse
                                )
                ) {
                    stack.pop();
                }
            } else {
                stack.push(nextDestination);
            }
        }
        System.out.println("Closing the system...");
    }

    private static void buildNavigation() {

        welcome = (object) -> {

            System.out.println("Welcome to the system.");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please, select a choice.\n" +
                        "\t1. Log in.\n" +
                        "\t2. Sign up.\n" +
                        "\t3. Exit.\n" +
                        "\t4. Show users.");

                int decision = scanner.nextInt();
                if (decision > 4 || decision < 1) {
                    System.out.println("please enter a correct choice.");
                    continue;
                }
                switch (decision) {
                    case 1:
                        return logIn;
                    case 2:
                        return signUp;
                    case 3:
                        return null;
                    case 4:
                        showUsers();
                        return welcome;
                }
            }
        };

        logIn = (object) -> {
            argument = null;
            System.out.println("Log in page.");

            while (true) {
                System.out.println("Please, fill the fields.");
                try {
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("\t> username : ");
                    String username = scanner.next().trim();
                    System.out.print("\t> password : ");
                    String password = scanner.next().trim();
                    System.out.print("\t> id : ");
                    int id = scanner.nextInt();


                    boolean logged = Repository.logIn(username, password, id);
                    if (logged) {
                        User.UserType userType = Repository.getCurrentUserType();
                        switch (userType) {
                            case STUDENT:
                                return studentPage;
                            case DOCTOR:
                                return doctorPage;
                            default:
                                return welcome;
                        }
                    } else {
                        System.out.println("User is not found.");
                        int choice = makeChoice(0, Integer.MAX_VALUE, "Enter 0 to return or any positive number to continue");
                        if (choice == 0) {
                            return null;
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error, Please insert valid data");
                    int choice = makeChoice(0, Integer.MAX_VALUE, "Enter 0 to return or any positive number to continue");
                    if (choice == 0) {
                        return null;
                    }
                }
            }
        };

        signUp = (object) -> {
            argument = null;
            System.out.println("Sign up page.");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please fill the fields.");
                System.out.print("\t> first name : ");
                String name = scanner.next().trim();
                System.out.print("\t> second name : ");
                name += " " + scanner.next().trim();
                System.out.print("\t> email address :");
                String email = scanner.next().trim();
                //TODO... create a separate utils function for checking email
                System.out.print("\t> username : ");
                String username = scanner.next().trim();
                //TODO... create a separate function for checking username
                System.out.print("\t> password : ");
                String password = scanner.next().trim();
                //TODO... create a separate function for checking password strength
                User.UserType userType;
                loop:
                while (true) {
                    System.out.print("\tType \"S\" for student and \"D\" for doctor");
                    String temp = scanner.next().trim();
                    if (temp.length() > 1) {
                        System.out.println("Enter S or D");
                        continue loop;
                    }
                    char type = temp.toUpperCase().charAt(0);
                    if (type == 'S') {
                        userType = User.UserType.STUDENT;
                        break loop;
                    } else if (type == 'D') {
                        userType = User.UserType.DOCTOR;
                        break loop;
                    }
                    System.out.println("Enter S or D");
                }
                boolean isCreated = Repository.createUser(name, username, password, email, userType);
                if (isCreated) {
                    System.out.println("User created successfully.Hip Hip Horaaaaay :\"");
                    System.out.println("your id is : " + Repository.getCurrentUser().getId());
                    break;
                } else {
                    System.out.println("Error, please try again");
                }
            }
            if (Repository.getCurrentUser().getUserType().equals(User.UserType.DOCTOR)) {
                return doctorPage;
            } else if (Repository.getCurrentUser().getUserType().equals(User.UserType.STUDENT)) {
                return studentPage;
            } else {
                return null;
            }
        };

        doctorPage = (object) -> {
            argument = null;
            String choices = "Please select a choice." +
                    "\n\t1. list my courses." +
                    "\n\t2. create a new course." +
                    "\n\t3. log out.";
            while (true) {
                int choice = makeChoice(1, 4, choices);

                switch (choice) {
                    case 1:
                        return doctorListCourses;
                    case 2:
                        return doctorCreateCourse;
                    case 3:
                        Repository.logOut();
                        return null;
                    default:
                        System.out.println("enter a valid number.");
                        break;
                }
            }
        };

        doctorCreateCourse = (object) -> {
            System.out.println("Course creation page.");
            while (true) {
                try {
                    Scanner scanner = new Scanner(System.in);
                    int choice = makeChoice(1, 2, "choose." +
                            "\n\t1. continue." +
                            "\n\t2. back");
                    if (choice == 2) {
                        return null;
                    }
                    System.out.print("Enter course name : ");
                    String name = scanner.nextLine();
                    System.out.print("Enter course code : ");
                    String code = scanner.nextLine();

                    boolean created = Repository.createCourse(name, code);
                    if (created) {
                        System.out.println("Course created successfully.");
                        argument = Repository.getCourse(name);
                        return doctorViewCourse;
                    } else {
                        System.out.println("Course already exists.");
                        argument = Repository.getCourse(name);
                        return null;
                    }
                } catch (Exception e) {
                    System.out.println("An error occurred.");
                }
            }

        };

        doctorListCourses = (object) -> {
            ArrayList<Course> courses = Repository.getDoctorCourses();
            if (courses.size() == 0) {
                System.out.println("You do not have any courses.");
                return null;
            }
            return doctorSelectCourse;

        };

        doctorSelectCourse = (object) -> {

            ArrayList<Course> courses = Repository.getDoctorCourses();
            String coursesString = Utils.arrayListAsString(courses, "Courses by you");
            int choice = makeChoice(0, courses.size(), coursesString + "\n\tselect a course or enter 0 to return.");
            if (choice == 0) {
                return null;
            }
            argument = courses.get(choice - 1);
            return doctorViewCourse;
        };

        doctorViewCourse = (object) -> {
            Course course;
            try {
                course = (Course) object;
            } catch (ClassCastException e) {
                return null;
            }

            if (course == null) {
                return null;
            }
            System.out.println(course.toString());
            System.out.println("Please, select a choice.");
            String choices = "\t1. create assignment." +
                    "\t2. create group." +
                    "\t3. view assignments." +
                    "\t4. back.";
            while (true) {
                Scanner scanner = new Scanner(System.in);
                int choice = makeChoice(1, 4, choices);
                switch (choice) {
                    case 1:
                        return doctorCreateAssignment;
                    case 2:
                        return doctorCreateGroup;
                    case 3:
                        return doctorViewAssignments;
                    case 4:
                        return null;
                    default:
                        System.out.println("please enter a valid number.");
                }
            }

        };

        doctorCreateAssignment = (object) -> {
            Course course = (Course) object;
            System.out.println("Assignment creation page.");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("enter assignment title : ");
                String title = scanner.nextLine();
                int numberOfQuestions = makeChoice(0, Integer.MAX_VALUE, "Enter number of questions : ");
                String[] questions = new String[numberOfQuestions];
                for (int i = 0; i < numberOfQuestions; i++) {
                    System.out.println("Enter Question no. " + (i + 1));
                    questions[i] = scanner.nextLine();
                }
                if (Repository.doctorAddAssignment(title, course, questions)) {
                    System.out.println("assignment added successfully.");
                    return null;
                } else {
                    System.out.println("An assignment with the same title in this course already exists.");
                }
            }
        };

        doctorCreateGroup = (object) -> {
            Course course = (Course) object;
            System.out.println("Group creation page");
            while (true) {
                Scanner scanner = new Scanner(System.in);
                System.out.print("Enter group name : ");
                String name = scanner.next();
                int size = makeChoice(0, course.getStudents().size(), "Enter group size : ");
                System.out.print(Utils.arrayListAsString(Repository.getCourseStudents(course), "Students of the course"));
                System.out.print(Utils.arrayListAsString(Repository.getCourseGroups(course), "Current Groups"));
                Student[] students = new Student[size];
                for (int i = 0; i < size; i++) {
                    while (course.getStudents().size() > 0) {
                        System.out.print("Enter student id or enter 0 to return : ");
                        try {
                            int id = scanner.nextInt();
                            if (id == 0) {
                                return null;
                            }
                            Student student = Repository.getCourseStudent(course, id);
                            if (student != null) {
                                students[i] = student;
                                break;
                            } else {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Enter Correct id");
                        }
                    }
                }
                if (Repository.doctorAddGroup(name, course, students)) {
                    System.out.println("Group added successfully.");
                    return null;
                } else {
                    System.out.println("Group with the same name in this course exists. Please try again.");
                }
            }
        };

        doctorViewAssignments = (object) -> {
            Course course;
            try {
                course = (Course) object;
            } catch (ClassCastException e) {
                return null;
            }
            if (course == null) {
                return null;
            }
            System.out.println("View Assignment page.");
            ArrayList<Assignment> assignments = Repository.getCourseAssignments(course);
            String message = Utils.arrayListAsString(assignments, "assignments");
            while (true) {
                System.out.println(message);
                System.out.println("Enter 0 to return, or select assignment number.");
                try {
                    Scanner scanner = new Scanner(System.in);
                    int i = scanner.nextInt();
                    if (i > assignments.size() || i < 0) {
                        throw new Exception();
                    }
                    if (i == 0) {
                        return null;
                    } else {
                        argument = assignments.get(i - 1);
                        return doctorViewAssignment;
                    }
                } catch (Exception e) {
                    System.out.println("Enter a valid number.");
                }
            }
        };

        doctorViewAssignment = (object) -> {
            System.out.println("View assignment page");
            Assignment assignment = (Assignment) object;
            String message = assignment.toString();
            while (true) {
                System.out.println(message);
                String choiceMessage = "Select an action." +
                        "\t1. view answers." +
                        "\t2. view grades." +
                        "\t3. back.";
                int choice = makeChoice(1, 3, choiceMessage);
                switch (choice) {
                    case 1:
                        argument = assignment;
                        return doctorViewAnswers;
                    case 2:
                        return doctorViewGrades;
                    case 3:
                        return null;
                }
            }
        };

        doctorViewAnswers = (object) -> {
            Assignment assignment = (Assignment) object;
            LinkedHashMap<Group, Answer> answers = assignment.getAnswers();
            String answersString = Utils.mapAsString(answers, "Group", "Answer");
            int groupNumber = makeChoice(1, answers.size(), answersString);
            ArrayList<Answer> answersList = new ArrayList<>(answers.values());
            argument = answersList.get(groupNumber - 1);
            return doctorSubmitGrade;
        };

        doctorSubmitGrade = (object) -> {
            Answer answer = (Answer) object;
            if (answer == null) {
                System.out.println("Not answered");
                return null;
            }
            Group group = answer.getGroup();
            String groupName = group.getName();
            while (true) {
                int grade = makeChoice(0, 100, "Enter grade of group " + groupName);
                if (Repository.submitGrade((float) grade, answer)) {
                    System.out.println("Grade added successfully.");
                    return null;
                } else {
                    System.out.println("an error occurred");
                    int choice = makeChoice(1, 2,
                            "\t1. try again." +
                                    "\t2. back.");
                    switch (choice) {
                        case 1:
                            continue;
                        case 2:
                            return null;
                    }
                }
            }
        };

        doctorViewGrades = (object) -> {
            Assignment assignment = (Assignment) object;
            LinkedHashMap<Group, Grade> grades = assignment.getGrades();
            grades.forEach((group, grade) -> System.out.println(
                    "group name : " + group.getName() + "\tgroup id : " + group.getId() + "\tgrade : " + grade.toString()
            ));
            return null;
        };

        studentPage = (object) -> {
            argument = null;
            System.out.println("Welcome " + Repository.getCurrentUser().getName());
            String choices = "please select a choice." +
                    "\n\t1. view all courses." +
                    "\n\t2. view registered courses." +
                    "\n\t3. grades report." +
                    "\n\t4. log out.";
            int choice = makeChoice(1, 4, choices);

            switch (choice) {
                case 1:
                    return studentViewAllCourses;
                case 2:
                    return studentViewRegisteredCourses;
                case 3:
                    return studentGradesReport;
                case 4:
                    Repository.logOut();
                    return null;
            }
            return null;
        };

        studentViewAllCourses = (object) -> {
            argument = null;
            LinkedHashMap<Course, Boolean> courses = Repository.studentGetAllCoursesWithRegistrationState();
            if (courses.size() < 1) {
                System.out.println("no courses yet.");
                return null;
            }
            final int[] i = {0};
            courses.forEach((course, aBoolean) -> {
                i[0]++;
                System.out.println(i[0] + ". " + course.toStringNameAndCodeOnly() + "    state : " + (aBoolean ? "registered" : "not registered"));
            });
            int choice = makeChoice(0, courses.size(), "select a course or enter 0 to return.");
            if (choice == 0) {
                return null;
            }
            Course course = Utils.getGeneralFromLinkedSetOrNull(courses.keySet(), choice - 1);
            argument = new Pair<>(course, courses.get(course));
            return studentViewCourse;
        };

        studentViewCourse = (object) -> {
            Pair<Course, Boolean> course = (Pair<Course, Boolean>) object;
            Course courseReal = course.getFirst();
            String courseString = courseReal.toString() + "\n";

            boolean isRegistered = course.getSecond();
            if (isRegistered) {
                courseString += "\nplease select a choice." +
                        "\t1. select a group." +
                        "\t2. view assignments." +
                        "\t3. back.";
                int choice = makeChoice(1, 3, courseString);
                switch (choice) {
                    case 1:
                        Group group = Repository.getCourseUserGroup(course.getFirst());
                        if (group != null) {
                            System.out.println("you ar in group " + group.getName());
                            return studentViewCourse;
                        } else {
                            ArrayList<Group> groups = course.getFirst().getGroups();
                            if (groups.size() == 0) {
                                System.out.println("no groups yet in the course, ask the doctor to create one.");
                                return null;
                            } else {
                                int groupNumber = makeChoice(1, groups.size(), "select group number");
                                Group group1 = groups.get(groupNumber - 1);
                                Repository.studentJoinGroup(group1);
                                System.out.println("Group joined successfully");
                                return null;
                            }
                        }
                    case 2:
                        return studentViewCourseAssignmentsAndSubmitAnswer;
                    default:
                        return null;
                }
            } else {
                courseString += "\nplease select a choice." +
                        "\t1. register in the course." +
                        "\t2. back";
                int choice = makeChoice(1, 2, courseString);
                switch (choice) {
                    case 1:
                        return studentRegisterInCourse;
                    default:
                        return null;
                }
            }
        };

        studentViewCourseAssignmentsAndSubmitAnswer = (object) -> {
            Course course = ((Pair<Course, Boolean>) object).getFirst();

            ArrayList<Assignment> assignments = Repository.getCourseAssignments(course);
            StringBuilder assignmentsString = new StringBuilder();
            int i = 0;
            for (Assignment assignment : assignments) {
                i++;
                assignmentsString.append(i).append(". ").append(assignment.getName()).append("\n");
            }
            String message = "select an assignment to submit answer or enter 0 to return\n" + assignmentsString.toString();
            int choice = makeChoice(0, assignments.size(), message);
            if (choice == 0) {
                return null;
            } else {
                Assignment assignment = assignments.get(choice - 1);
                Group group = Repository.getCourseUserGroup(course);
                if (group == null) {
                    System.out.println("Error : you are not in any group.");
                    return null;
                }
                System.out.println("enter answers.");
                Answer answer = new Answer(group, assignment);
                assignment.getQuestions().forEach(question -> {
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Enter the answer of the question : " + question);
                    String answerString = scanner.nextLine();
                    answer.addAnswer(question, answerString);
                });
                if (Repository.studentSubmitAnswer(assignment, answer)) {
                    System.out.println("Answer submitted successfully");
                    return null;
                } else {
                    System.out.println("an error occurred.");
                }
            }
            return null;
        };

        studentRegisterInCourse = (object) -> {
            Course course = ((Pair<Course, Boolean>) object).getFirst();
            Repository.studentRegisterInCourse(course);
            return null;
        };

        studentViewRegisteredCourses = (object) -> {
            ArrayList<Course> courses = Repository.studentGetRegisteredCourses();
            if (courses.size() < 1) {
                System.out.println("No registered courses yet.");
            }
            final int[] i = {0};
            courses.forEach((course) -> {
                i[0]++;
                System.out.println(i[0] + ". " + course.toStringNameAndCodeOnly() + "\tstate : registered");
            });
            int choice = makeChoice(0, courses.size(), "select a course or enter 0 to return.");
            if (choice == 0) {
                return null;
            }
            Course course = courses.get(choice - 1);
            argument = new Pair<>(course, true);
            return studentViewCourse;
        };

        studentGradesReport = (object) -> {
            LinkedHashMap<Assignment, Grade> report = Repository.getGradesReport();
            if (report.size() < 1) {
                System.out.println("No grades available");
                return null;
            }
            boolean[] booleans = {false};
            report.forEach((Assignment assignment, Grade grade) -> {
                if (grade != null) {
                    System.out.println(assignment.getCourse().getName() + " : " + assignment.getName() + " : " + grade.toString());
                    booleans[0] = true;
                }
            });
            if (!booleans[0]) {
                System.out.println("no submitted grades yet");
            }
            return null;
        };

    }

    private static void showUsers() {
        System.out.println(Utils.arrayListAsString(User.getUsers(), "users"));

    }

    private static int makeChoice(int min, int max, String choiceMessage) {
        int choice;
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.println(choiceMessage);
                choice = scanner.nextInt();
                if (choice < min || choice > max) {
                    throw new Exception();
                }
                break;
            } catch (Exception e) {
                System.out.println("Enter a valid number.");
            }
        }
        return choice;
    }

    private static void makeDummyData() {
        for (int i = 1; i < 11; i++) {
            Repository.createUser("doctor " + i, "doctor" + i, "doctor" + i, "doctor" + i + "@dmail.com", User.UserType.DOCTOR);
            Doctor doctor = (Doctor) Repository.getCurrentUser();
            for (int j = 0; j < 3; j++) {
                Repository.createCourse("Math" + i + j, "MTH" + i + j);
                for (int k = 0; k < 5; k++) {
                    Repository.doctorAddGroup("Math" + i + j + k, Repository.getCourse("Math" + i + j), null);
                    Repository.doctorAddAssignment("Math" + i + j + k, Repository.getCourse("Math" + i + j), "Q1", "Q2", "Q3");
                }
            }
            Repository.createUser("student " + i, "student" + i, "student" + i, "student" + i + "@smail.com", User.UserType.STUDENT);
        }
        Repository.logOut();
    }
}
