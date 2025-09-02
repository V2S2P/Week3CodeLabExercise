package app.Populator;

import app.DAO.TeacherDAO;
import app.entities.Teacher;

public class TeacherPopulator {

    public static Teacher[] populate(TeacherDAO teacherDAO) {
        Teacher t1 = Teacher.builder()
                .email("alice@example.com")
                .name("Alice Andersen")
                .zoom("https://zoom.us/alice")
                .build();
        t1 = teacherDAO.create(t1);

        Teacher t2 = Teacher.builder()
                .email("bob@example.com")
                .name("Bob Berg")
                .zoom("https://zoom.us/bob")
                .build();
        t2 = teacherDAO.create(t2);

        return new Teacher[]{t1, t2};
    }
}

