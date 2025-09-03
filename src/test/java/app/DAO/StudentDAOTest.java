package app.DAO;

import app.Populator.StudentPopulator;
import app.config.HibernateConfig;
import app.entities.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentDAOTest {

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private static final StudentDAO studentDAO = new StudentDAO(emf);
    private static Student s1;
    private static Student s2;

    @BeforeEach
    void setUp() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Wipe all students before each test
            em.createQuery("DELETE FROM Student").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE student_id_seq RESTART WITH 1").executeUpdate();

            em.getTransaction().commit();

            // Repopulate fresh students
            Student[] students = StudentPopulator.populate(studentDAO);
            s1 = students[0];
            s2 = students[1];
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
    void createStudent() {
        Student s3 = Student.builder()
                .email("charlie@example.com")
                .name("Charlie Christensen")
                .build();
        s3 = studentDAO.create(s3);

        assertNotNull(s3.getId());

        List<Student> students = studentDAO.getAll();
        assertEquals(4, students.size());
    }

    @Test
    void getStudentById() {
        Student found = studentDAO.find(s1.getId());
        assertNotNull(found);
        assertEquals(s1.getEmail(), found.getEmail());
        assertEquals(s1.getName(), found.getName());
    }

    @Test
    void updateStudent() {
        s1.setName("Emma Updated");
        boolean updated = studentDAO.update(s1);
        assertTrue(updated);

        Student found = studentDAO.find(s1.getId());
        assertEquals("Emma Updated", found.getName());
    }

    @Test
    void deleteStudent() {
        boolean deleted = studentDAO.delete(s1);
        assertTrue(deleted);

        List<Student> students = studentDAO.getAll();
        assertEquals(2, students.size());
        assertEquals(s2.getEmail(), students.get(0).getEmail());
    }

    @Test
    void getAllStudents() {
        List<Student> students = studentDAO.getAll();
        assertEquals(3, students.size());
    }
}
