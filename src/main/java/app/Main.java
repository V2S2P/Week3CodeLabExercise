package app;

import app.DAO.CourseDAO;
import app.DAO.StudentDAO;
import app.DAO.TeacherDAO;
import app.config.HibernateConfig;
import app.entities.Course;
import app.entities.CourseName;
import app.entities.Student;
import app.entities.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        try (EntityManager em = emf.createEntityManager()) {

            CourseDAO courseDAO = new CourseDAO(emf);
            StudentDAO studentDAO = new StudentDAO(emf);
            TeacherDAO teacherDAO = new TeacherDAO(emf);

            // --- 1) Create some teachers ---
            Teacher t1 = new Teacher("alice@school.com", "Alice", "zoom1");
            Teacher t2 = new Teacher("bob@school.com", "Bob", "zoom2");
            teacherDAO.create(t1);
            teacherDAO.create(t2);

            // --- 2) Create some courses ---
            Course c1 = new Course(CourseName.MATH, "Algebra basics", t1);
            Course c2 = new Course(CourseName.ENGLISH, "Grammar 101", t1);
            Course c3 = new Course(CourseName.ART, "Painting for beginners", t2);
            courseDAO.create(c1);
            courseDAO.create(c2);
            courseDAO.create(c3);

            // --- 3) Create some students ---
            Student s1 = new Student("emma@student.com", "Emma");
            Student s2 = new Student("noah@student.com", "Noah");
            Student s3 = new Student("olivia@student.com", "Olivia");
            studentDAO.create(s1);
            studentDAO.create(s2);
            studentDAO.create(s3);

            // Link students to courses
            s1.addCourse(c1);
            s1.addCourse(c2);
            s2.addCourse(c1);
            s3.addCourse(c3);

            studentDAO.update(s1);
            studentDAO.update(s2);
            studentDAO.update(s3);

            // Link teachers to courses
            t1.addCourse(c1);
            t1.addCourse(c2);
            t2.addCourse(c3);

            teacherDAO.update(t1);
            teacherDAO.update(t2);

            // Testing DAO INFO
            System.out.println("All teachers: " + teacherDAO.getAll());
            System.out.println("All students: " + studentDAO.getAll());
            System.out.println("All courses: " + courseDAO.getAll());
        }
    }
}