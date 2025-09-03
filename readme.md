# JPA & Lombok Cheat Sheet

## 1️⃣ Typical Lombok Annotations

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
```

## 2️⃣ Typical JPA Annotations

```plaintext
@Entity
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
@OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
@ManyToOne
@ManyToMany
@JoinTable(...)  // for Many-to-Many owning side
@Builder.Default   // required for initializing collections when using @Builder
@ToString.Exclude  // to avoid infinite recursion in bidirectional relationships

```
## 3️⃣ Helper Methods for Bi-directional Relationships
These methods keep both sides of a relationship in sync, avoiding inconsistencies in memory and ensuring proper persistence.
```java
        One-to-One (1:1)
public void addPersonDetail(PersonDetail personDetail) {
    this.personDetail = personDetail;      // inverse side
    if (personDetail != null) {
        personDetail.setPerson(this);      // owning side
    }
}

One-to-Many (1:M)
public void addFee(Fee fee) {
    this.fees.add(fee);                    // inverse side
    if (fee != null) {
        fee.setPerson(this);               // owning side
    }
}

public void addCourse(Course course) {
    this.courses.add(course);              // inverse side
    if (course != null) {
        course.setTeacher(this);           // owning side
    }
}

Many-to-Many (M:M)
// Owning side
public void addCourse(Course course) {
    this.courses.add(course);
    if (course != null) {
        course.getStudents().add(this);   // inverse side
    }
}

// Inverse side (optional)
public void addStudent(Student student) {
    this.students.add(student);
    if (student != null) {
        student.getCourses().add(this);   // owning side
    }
}


Tip: Always update both sides to keep your entities consistent in memory. JPA only persists the owning side.
```

## 4️⃣ Collections in Relationships
Use Set<> for One-to-Many and Many-to-Many to avoid duplicates:
```java
private Set<Fee> fees = new HashSet<>();
private Set<Course> courses = new HashSet<>();
```

## 5️⃣ Database & Hibernate Setup
## Husk!
* Insert entities in HibernateConfig.
* Create your database.
* Add config.properties in resources:

```bash
DB_NAME=...
DB_USERNAME=postgres
DB_PASSWORD=postgres
```

## 6️⃣ Owning Side vs. Inverse (mappedBy)

```plaintext
Owning side: Controls the database column/join table; JPA persists the relationship here.
Inverse side (mappedBy): Mirrors the owning side; read-only for JPA.
Rule of thumb: The side that has the foreign key in its table is the owning side.

In JPA, every relationship has an owning side — this is the side that controls the association in the database.

Owning side: JPA uses this side to update the join table (for ManyToMany) or foreign key column (for OneToMany/ManyToOne) in the database.

Inverse side: Also called the mappedBy side, this side is read-only from JPA’s perspective. It only reflects the relationship; it does not update the DB.

Think of it as:
Owning side = “I’m responsible for writing the relationship to the database”
Inverse side = “I just mirror what the owning side has done”
```

## Hvordan finder man ud af hvilken er Owning og Inverse(mappedBy) (ManyToOne og OneToMany)

```
@ManyToOne always owns the relationship because it stores the foreign key in its table.
@OneToMany is usually inverse, so you add mappedBy pointing to the owning side’s field.

Eksempel:
```
```java
// Owning side
@ManyToOne
private Teacher teacher; // stores teacher_id in Course table

// Inverse side
@OneToMany(mappedBy = "teacher")
private Set<Course> courses = new HashSet<>();

// Owning side
@ManyToMany
@JoinTable(
        name = "student_course",
        joinColumns = @JoinColumn(name = "student_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
)
private Set<Course> courses = new HashSet<>();

// Inverse side
@ManyToMany(mappedBy = "courses")
private Set<Student> students = new HashSet<>();
```
```plaintext
mappedBy = "teacher" → points to the field name in the owning side (Course.teacher).
If you try to persist only Teacher.courses without setting Course.teacher, nothing will be written to the DB.
✅ Key: the side with the foreign key in the table is the owning side.
```
## Hvordan man finder ud af det når det er ManyToMany
```plaintext
The owning side is the one that defines @JoinTable.
The inverse side uses mappedBy to point to the owning side’s collection.

Eksempel:
```
```java
// Owning side
@ManyToMany
@JoinTable(
    name = "student_course",
    joinColumns = @JoinColumn(name = "student_id"),
    inverseJoinColumns = @JoinColumn(name = "course_id")
)
private Set<Course> courses = new HashSet<>();

// Inverse side
@ManyToMany(mappedBy = "courses")
private Set<Student> students = new HashSet<>();
```
```plaintext
mappedBy = "courses" → points to the field in the owning side (Student.courses).
The owning side controls the join table, i.e., student_course.
```

```
Extra tips:

Always name mappedBy exactly as the field in the owning side.
Think “who has the foreign key or join table column?” → that’s the owning side.
Cascade types usually go on the owning side if you want saving/deleting to propagate.
```
## 📝 Rule of Thumb for Relationship Updates in JPA/Hibernate
```plaintext
1. ManyToOne / OneToMany (e.g. Course ↔ Teacher)

Owning side: @ManyToOne (Course.teacher)

Inverse side: @OneToMany(mappedBy = "teacher") (Teacher.courses)

✅ To persist correctly:
You must set the owning side → course.setTeacher(teacher)

✅ To keep memory consistent:
Also update the inverse side (teacher.getCourses().add(course)) → best handled by teacher.addCourse(course).

👉 Use the helper method on the One side (Teacher).
```

```plaintext
2. ManyToMany (e.g. Student ↔ Course)

Owning side: The side with @JoinTable → Student.courses

Inverse side: Course.students (mappedBy = "courses")

✅ To persist correctly:
You must update the owning side → student.getCourses().add(course)

✅ To keep memory consistent:
Also update the inverse side (course.getStudents().add(student)) → best handled by student.addCourse(course).

👉 Use the helper method on the owning side (Student).
```

```plaintext
3. OneToOne

Owning side: The one with the foreign key (@JoinColumn)

Inverse side: The one with mappedBy

✅ To persist correctly:
Set the owning side.

✅ For consistency:
Use a helper to set both. Example:
```
```java
public void setPassport(Passport passport) {
    this.passport = passport;
    if (passport != null) {
        passport.setStudent(this);
    }
}
```

## 🔑 Quick Cheatsheet
```plaintext
Always set the owning side → persistence requires it.

Use helper methods → keeps both sides in sync in memory.

Define helpers on the aggregate root / “parent” entity (usually the One side in OneToMany, or the side with @JoinTable in ManyToMany).
```