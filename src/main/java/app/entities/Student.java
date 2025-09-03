package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "courses")
@EqualsAndHashCode(exclude = "courses")
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime createdAt;
    private String email;
    private String name;
    private LocalDateTime updatedAt;

    public Student(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // M:M with Student
    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses = new HashSet<>();

    // Bi-Directional (You can look up a Student and see their Courses, and you can look up Courses and see Students for that/those Course(s))
    public void addCourse(Course course) {
        this.courses.add(course); // update owning side
        if (course != null) {
            course.getStudents().add(this); // update inverse(mappedBy) side(Course)
        }
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
    @PostUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
