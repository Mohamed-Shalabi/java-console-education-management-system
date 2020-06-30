package system.courses.assignments;


import system.courses.groups.Group;

public class Grade {
    private float degree;
    private int fullDegree = 100;

    public Grade(){

    }

    public Grade(float degree) {
        this.degree = degree;
    }

    public Grade(float degree, int fullDegree) {
        this.degree = degree;
        this.fullDegree = fullDegree;
    }

    @Override
    public String toString() {
        return getDegree() + " / " + getFullDegree();
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public int getFullDegree() {
        return fullDegree;
    }

    public void setFullDegree(int fullDegree) {
        this.fullDegree = fullDegree;
    }
}
