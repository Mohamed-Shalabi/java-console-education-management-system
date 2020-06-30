package system.courses.assignments;

import system.courses.groups.Group;
import utils.Utils;

import java.util.LinkedHashMap;

public class Answer {

    private Group group;
    private Assignment assignment;
    private LinkedHashMap<String, String> answers;

    public Answer(Group group, Assignment assignment) {
        this.group = group;
        this.assignment = assignment;
        answers = new LinkedHashMap<>();
    }

    public Group getGroup() {
        return group;
    }

    public void addAnswer(String question, String answer) {
        Utils.addGeneralToMap(answers, question, answer);
    }

    @Override
    public String toString() {
        return Utils.mapAsString(answers, null, null);
    }

    public Assignment getAssignment() {
        return assignment;
    }

}
