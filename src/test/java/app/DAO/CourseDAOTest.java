package app.DAO;

import app.Populator.CoursePopulator;
import app.entities.Course;
import app.entities.CourseName;
import app.entities.Teacher;
import app.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CourseDAOTest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final CourseDAO courseDAO = new CourseDAO(emf);
    private static final TeacherDAO teacherDAO = new TeacherDAO(emf);
    private static Course c1;
    private static Course c2;
    private static Course c3;
    private static Teacher t1;
    private static Teacher t2;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // wipe tables in the right order because of FK constraints
            em.createQuery("DELETE FROM Course").executeUpdate();
            em.createQuery("DELETE FROM Teacher").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE course_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE teacher_id_seq RESTART WITH 1").executeUpdate();

            em.getTransaction().commit();

            // Repopulate teachers
            t1 = teacherDAO.create(Teacher.builder()
                    .email("teacher1@example.com")
                    .name("Teacher One")
                    .zoom("zoomlink1")
                    .build());

            t2 = teacherDAO.create(Teacher.builder()
                    .email("teacher2@example.com")
                    .name("Teacher Two")
                    .zoom("zoomlink2")
                    .build());

            // Repopulate courses
            Course[] courses = CoursePopulator.populate(courseDAO, new Teacher[]{t1, t2});
            c1 = courses[0];
            c2 = courses[1];
            c3 = courses[2];
        }
    }

    @AfterAll
    void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
            System.out.println("EntityManagerFactory closed.");
        }
    }

    @Test
    void getInstance() {
        assertNotNull(emf);
    }

    @Test
    void createCourse() {
        Course c4 = Course.builder()
                .courseName(CourseName.SCIENCE)
                .description("Physics for Beginners")
                .teacher(t1)
                .build();
        c4 = courseDAO.create(c4);

        assertNotNull(c4.getId());

        List<Course> courses = courseDAO.getAll();
        assertEquals(4, courses.size());
    }

    @Test
    void getCourseById() {
        Course found = courseDAO.find(c1.getId());
        assertNotNull(found);
        assertEquals(c1.getCourseName(), found.getCourseName());
        assertEquals(c1.getDescription(), found.getDescription());
    }

    @Test
    void updateCourse() {
        c1.setDescription("Updated Maths Course");
        boolean updated = courseDAO.update(c1);
        assertTrue(updated);

        Course found = courseDAO.find(c1.getId());
        assertEquals("Updated Maths Course", found.getDescription());
    }

    @Test
    void deleteCourse() {
        boolean deleted = courseDAO.delete(c1);
        assertTrue(deleted);

        List<Course> courses = courseDAO.getAll();
        assertEquals(2, courses.size());
        assertTrue(courses.stream().noneMatch(c -> c.getId().equals(c1.getId())));
    }

    @Test
    void getAllCourses() {
        List<Course> courses = courseDAO.getAll();
        assertEquals(3, courses.size());
    }
}

