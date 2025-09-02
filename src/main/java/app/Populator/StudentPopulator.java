package app.Populator;

import app.DAO.StudentDAO;
import app.entities.Student;

public class StudentPopulator {

    public static Student[] populate(StudentDAO studentDAO) {
        Student s1 = Student.builder()
                .email("emma@student.com")
                .name("Emma")
                .build();
        s1 = studentDAO.create(s1);

        Student s2 = Student.builder()
                .email("noah@student.com")
                .name("Noah")
                .build();
        s2 = studentDAO.create(s2);

        Student s3 = Student.builder()
                .email("olivia@student.com")
                .name("Olivia")
                .build();
        s3 = studentDAO.create(s3);

        return new Student[]{s1, s2, s3};
    }
}

