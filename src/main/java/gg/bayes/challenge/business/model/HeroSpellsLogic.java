package gg.bayes.challenge.business.model;

import lombok.*;

@Builder
@Data
public class HeroSpellsLogic {

    private String spell;
    private Integer casts;
}
