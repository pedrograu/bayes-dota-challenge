package gg.bayes.challenge.db.model;

import lombok.*;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class HeroItemsDAO {

    private String item;
    private Long time_purchase;
}
