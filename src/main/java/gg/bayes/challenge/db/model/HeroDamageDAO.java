package gg.bayes.challenge.db.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class HeroDamageDAO {

    private String target;
    private Integer damageInstances;
    private Integer totalDamage;
}
