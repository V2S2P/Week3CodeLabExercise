package app.Populator;

import app.DAO.CourseDAO;
import app.entities.Course;
import app.entities.CourseName;
import app.entities.Teacher;

public class CoursePopulator {

    public static Course[] populate(CourseDAO courseDAO, Teacher[] teachers) {
        Course c1 = Course.builder()
                .courseName(CourseName.MATH)
                .description("Intro To Advanced Maths")
                .teacher(teachers[0])
                .build();
        c1 = courseDAO.create(c1);

        Course c2 = Course.builder()
                .courseName(CourseName.ART)
                .description("The Art of Art")
                .teacher(teachers[0])
                .build();
        c2 = courseDAO.create(c2);

        Course c3 = Course.builder()
                .courseName(CourseName.ENGLISH)
                .description("English For Beginners")
                .teacher(teachers[1])
                .build();
        c3 = courseDAO.create(c3);

        return new Course[]{c1, c2, c3};
    }
}


