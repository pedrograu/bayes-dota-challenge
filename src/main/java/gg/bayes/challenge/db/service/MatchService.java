package gg.bayes.challenge.db.service;

import gg.bayes.challenge.business.model.HeroItemsLogic;
import gg.bayes.challenge.business.model.HeroKillsLogic;

import java.util.List;

public interface MatchService {
    Long ingestMatch(String payload);

    List<HeroKillsLogic> getMatchDaoGivenMatchId(Long matchId);

    List<HeroItemsLogic> getHeroItemsDaoGivenMatchIdAndHero(Long matchId, String heroName);
}
