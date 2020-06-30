package system.users;

import system.users.doctors.Doctor;
import system.users.students.Student;

import java.util.ArrayList;

public class User {

    private String name;
    private String username;
    private String password;
    private String email;
    private static int maxId = 0;
    private int id;
    private UserType userType;
    private static ArrayList<User> users = new ArrayList<>();
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public User(String name, String username, String password, String email) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        generateId();
    }

    public static boolean contains(User user) {
        for (User user1 : users) {
            if (user1.equals(user)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public static boolean logIn(String username, String password, int id) {
        for (User user : users) {
            if (
                    user.getUsername().equals(username) &&
                            user.getPassword().equals(password) &&
                            user.getId() == id
            ) {
                setCurrentUser(user);
                return true;
            }
        }
        return false;
    }

    public static boolean createUser(String name, String username, String password, String email, UserType userType) {
        User user;
        if (userType == UserType.STUDENT) {
            user = new Student(name, username, password, email);
        } else if (userType == UserType.DOCTOR) {
            user = new Doctor(name, username, password, email);
        } else {
            return false;
        }
        if (!contains(user)) {
            users.add(user);
            setCurrentUser(user);
            return true;
        } else {
            return false;
        }
    }

    public static void logOut() {
        setCurrentUser(null);
    }

    public static ArrayList<User> getUsers() {
        return users;
    }

    private void generateId() {
        maxId++;
        id = maxId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    protected void setUserType(UserType userType) {
        this.userType = userType;
    }

    public enum UserType {
        DOCTOR,
        STUDENT
    }

    @Override
    public boolean equals(Object obj) {
        User user = (User) obj;
        if (user != null) {
            return (
                    getId() == user.getId() &&
                            getUsername().equals(user.getUsername()) &&
                            getPassword().equals(user.getPassword()) &&
                            getEmail().equals(user.getEmail()) &&
                            getUserType().equals(user.getUserType())
            );
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "username : " + getUsername() + "\tpassword : " + getPassword() + "\tid : " + getId();
    }
}
