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
import jakarta.persistence.TypedQuery;

import java.util.List;

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

            // JPQL Queries
            // Get all students that are attending a specific course
            TypedQuery<Student> q1 = em.createQuery("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId", Student.class);
            q1.setParameter("courseId", c1.getId());
            List<Student> students = q1.getResultList();
            System.out.println("Students in course " + c1.getCourseName() + ": " + students);

            // Get all courses that a specific student is attending
            TypedQuery<Course> q2 = em.createQuery("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId", Course.class);
            q2.setParameter("studentId", s1.getId());
            List<Course> coursesOfs1 = q2.getResultList();
            System.out.println("Courses attended by " + s1.getName() + ": " + coursesOfs1);

            // Get all courses that a specific teacher is teaching
            TypedQuery<Course> q3 = em.createQuery("SELECT c FROM Course c JOIN c.teacher t WHERE t.id = :teacherId", Course.class);
            q3.setParameter("teacherId", t1.getId());
            List<Course> coursesOfs2 = q3.getResultList();
            System.out.println("Courses taught by " + t1.getName() + ": " + coursesOfs2);

            // Get all students that are attending a course that a specific teacher is teaching
            TypedQuery<Student> q4 = em.createQuery("SELECT DISTINCT s FROM Student s JOIN s.courses c WHERE c.teacher.id = :teacherId", Student.class);
            q4.setParameter("teacherId", t1.getId());
            List<Student> students2 = q4.getResultList();
            System.out.println("Students taught by " + t1.getName() + ": " + students2);
        }
    }
}