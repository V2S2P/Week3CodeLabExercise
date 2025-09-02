# The Dolphin JPA Exercise

This code is the starting point for this exercise:

- [Dolphin exercise](https://3semfall2025.kursusmaterialer.dk/backend/jpa-relations/exercises/dolphin/)

The goal is to train @OneToMany relations and much more.

## Noter

Typiske Lombok annotationer for hver klasse:

```plaintext
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
```

Typiske JPA annotationer;

```plaintext
@Entity
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@OneToOne(mappedBy="person", cascade = CascadeType.ALL)
@OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
@Builder.Default
@ManyToOne
@ToString.Exclude
```

```java
Hjælpe metode til at lave bi-directional relationer:

public void addPersonDetail(PersonDetail personDetail) {
    this.personDetail = personDetail;
    if (personDetail != null){
        personDetail.setPerson(this);
 }
}

public void addFee(Fee fee) {
    this.fees.add(fee);
    if (fee != null) {
        fee.setPerson(this);
    }
}
```

## Brug Set<> til One-To-Many for at undgå dubletter
```java
private Set<Fee> fees = new HashSet<>();
```

## Husk!

* At indsætte entities i Hibernate.config
* Oprette DB
* Oprette config.properties i resource mappe

```bash
DB_NAME=...
DB_USERNAME=postgres
DB_PASSWORD=postgres
```