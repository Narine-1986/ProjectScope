package am.itspace.projectscope.model;

import am.itspace.projectscope.ProjectscopeApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "log")
public class Log {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;

    @Column
    private int userId;

    @OneToOne
    private Project project;

    @Column(name="date")
    private LocalDate date;

    @Column
    private double hours;

    @PrePersist
    public void prePersist() {
        setDate(LocalDate.now());
    }

}
