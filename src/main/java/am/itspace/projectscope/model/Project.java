package am.itspace.projectscope.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "project")
public class Project {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column
    private String name;
    @Column
    private double hours;

    @Column(nullable = false)
    private int userId;

    @Transient
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Column(nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalDate createdDate;

    @Column
    @JsonFormat(pattern="yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private Date deadLine;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name="project_user",joinColumns=@JoinColumn(name="project_id"),
            inverseJoinColumns=@JoinColumn(name="user_id"))
    private List<User> members;


    @PrePersist
    public void prePersist() {
        setCreatedDate(LocalDate.now());
    }


}
