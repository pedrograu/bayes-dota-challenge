package gg.bayes.challenge.business.model;

import lombok.*;

@Builder
@Data
public class HeroDamageLogic {

    private String target;
    private Integer damageInstances;
    private Integer totalDamage;
}
