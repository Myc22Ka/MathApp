package pl.myc22ka.mathapp.level.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "level_requirements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LevelRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int level;

    @Column(name = "required_xp", nullable = false)
    private int requiredXp;
}
