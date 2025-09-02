package app.DAO;

import app.Populator.TeacherPopulator;
import app.entities.Teacher;
import app.config.HibernateConfig;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherDAOTest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final TeacherDAO teacherDAO = new TeacherDAO(emf);
    private static Teacher t1;
    private static Teacher t2;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Wipe all teachers before each test
            em.createQuery("DELETE FROM Teacher").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE teacher_id_seq RESTART WITH 1").executeUpdate();

            em.getTransaction().commit();

            // Repopulate fresh teachers
            Teacher[] teachers = TeacherPopulator.populate(teacherDAO);
            t1 = teachers[0];
            t2 = teachers[1];
        } catch (Exception e) {
            e.printStackTrace();
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
    void createTeacher() {
        Teacher t3 = Teacher.builder()
                .email("charlie@example.com")
                .name("Charlie Christensen")
                .zoom("https://zoom.us/charlie")
                .build();
        t3 = teacherDAO.create(t3);

        assertNotNull(t3.getId());

        List<Teacher> teachers = teacherDAO.getAll();
        assertEquals(3, teachers.size());
    }

    @Test
    void getTeacherById() {
        Teacher found = teacherDAO.find(t1.getId());
        assertNotNull(found);
        assertEquals(t1.getEmail(), found.getEmail());
        assertEquals(t1.getName(), found.getName());
    }

    @Test
    void updateTeacher() {
        t1.setName("Alice Updated");
        boolean updated = teacherDAO.update(t1);
        assertTrue(updated);

        Teacher found = teacherDAO.find(t1.getId());
        assertEquals("Alice Updated", found.getName());
    }

    @Test
    void deleteTeacher() {
        boolean deleted = teacherDAO.delete(t1);
        assertTrue(deleted);

        List<Teacher> teachers = teacherDAO.getAll();
        assertEquals(1, teachers.size());
        assertEquals(t2.getEmail(), teachers.get(0).getEmail());
    }

    @Test
    void getAllTeachers() {
        List<Teacher> teachers = teacherDAO.getAll();
        assertEquals(2, teachers.size());
    }
}
