package gg.bayes.challenge.db.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class HeroKillsDAO {

    private String hero;
    private Integer kills;
}
