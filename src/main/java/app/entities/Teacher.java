package app.entities;


import jakarta.persistence.*;
import lombok.*;

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
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;
    private String name;
    private String zoom;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Course> courses = new HashSet<>();

    public Teacher(String email, String name, String zoom) {
        this.email = email;
        this.name = name;
        this.zoom = zoom;
    }

    public void addCourse(Course course) {
        this.courses.add(course); // update inverse(mappedBy) side (Course)
        if (course != null) {
            course.setTeacher(this); // update owning side
        }
    }

}
