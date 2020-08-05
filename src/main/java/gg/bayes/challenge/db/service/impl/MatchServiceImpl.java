package gg.bayes.challenge.db.service.impl;

import gg.bayes.challenge.db.entities.MatchDamageEntity;
import gg.bayes.challenge.db.entities.MatchItemEntity;
import gg.bayes.challenge.db.entities.MatchKillEntity;
import gg.bayes.challenge.db.entities.MatchSpellEntity;
import gg.bayes.challenge.db.service.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.Instant;
import java.util.function.Predicate;

@Slf4j
@Service
public class MatchServiceImpl implements MatchService {

    private final String KILLED = "killed";
    private final String BUYS = "buys";
    private final String CASTS = "casts";
    private final String HITS = "hits";
    private final String HERO_PREFIX = "npc_dota_hero_";
    private final String ITEM_PREFIX = "item_";

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MatchServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long ingestMatch(String payload) {
        Long matchId = storeMatch();

        new BufferedReader(new StringReader(payload)).lines().filter(isKill()).forEach(line -> storeKill(line, matchId));
        new BufferedReader(new StringReader(payload)).lines().filter(isItem()).forEach(line -> storeItem(line, matchId));
        new BufferedReader(new StringReader(payload)).lines().filter(isSpell()).forEach(line -> storeSpell(line, matchId));
        new BufferedReader(new StringReader(payload)).lines().filter(isDamage()).forEach(line -> storeDamage(line, matchId));

        return matchId;
    }

    private Long storeMatch() {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO bayes.match (time_creation) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, Instant.now().getEpochSecond());
            return ps;
        }, keyHolder);

        return (long) keyHolder.getKey();
    }

    private Predicate<String> isKill() {
        return s -> s.contains(KILLED) && actionOnHero(s);
    }

    private Predicate<String> isItem() {
        return s -> s.contains(BUYS);
    }

    private Predicate<String> isSpell() {
        return s -> s.contains(CASTS) && s.split(" ")[1].startsWith(HERO_PREFIX);
    }

    private Predicate<String> isDamage() {
        return s -> s.contains(HITS) && actionOnHero(s);
    }

    //npc_dota_hero_ must appears twice to be an action on a hero
    private boolean actionOnHero(String s) {
        return (s.split(HERO_PREFIX, -1).length - 1) == 2;
    }

    //[00:11:17.489] npc_dota_hero_snapfire is killed by npc_dota_hero_mars
    //[00:12:15.108] npc_dota_neutral_harpy_scout is killed by npc_dota_hero_pangolier this must be avoid
    private void storeKill(String line, Long matchId) {
        MatchKillEntity matchKillEntity = new MatchKillEntity();

        String[] strings = line.split(HERO_PREFIX);
        matchKillEntity.setMatchId(matchId);
        matchKillEntity.setHero(strings[2]);
        matchKillEntity.setTarget(strings[1].split(" ")[0]);
        jdbcTemplate.update("INSERT INTO bayes.match_kills (match_id, hero, target) VALUES (?, ?, ?)",
                matchKillEntity.getMatchId(), matchKillEntity.getHero(), matchKillEntity.getTarget());
    }

    //[00:08:46.693] npc_dota_hero_snapfire buys item item_clarity
    private void storeItem(String line, Long matchId) {
        String[] strings = line.split(HERO_PREFIX);

        MatchItemEntity matchItemEntity = new MatchItemEntity();
        matchItemEntity.setMatchId(matchId);
        matchItemEntity.setTimePurchase(calculateTimeGame(strings[0]));
        matchItemEntity.setHero(strings[1].split(" ")[0]);
        matchItemEntity.setItem(strings[1].split(" ")[3].split(ITEM_PREFIX)[1]);
        jdbcTemplate.update("INSERT INTO bayes.match_items (match_id, hero, item, time_purchase) VALUES (?, ?, ?, ?)",
                matchItemEntity.getMatchId(), matchItemEntity.getHero(), matchItemEntity.getItem(), matchItemEntity.getTimePurchase());
    }

    //[00:08:43.460] npc_dota_hero_pangolier casts ability pangolier_swashbuckle (lvl 1) on dota_unknown
    private void storeSpell(String line, Long matchId) {
        String[] strings = line.split(HERO_PREFIX);

        MatchSpellEntity matchSpellEntity = new MatchSpellEntity();
        matchSpellEntity.setMatchId(matchId);
        matchSpellEntity.setHero(strings[1].split(" ")[0]);
        matchSpellEntity.setSpell(strings[1].split(" ")[3]);
        jdbcTemplate.update("INSERT INTO bayes.match_spells (match_id, hero, spell) VALUES (?, ?, ?)",
                matchSpellEntity.getMatchId(), matchSpellEntity.getHero(), matchSpellEntity.getSpell());
    }

    //[00:10:42.031] npc_dota_hero_bane hits npc_dota_hero_abyssal_underlord with dota_unknown for 51 damage (740->689)
    //SELECT target, count(match_id) as damage_instances, sum(damage) as total_damage FROM BAYES.MATCH_DAMAGE where match_id = 1 and hero = 'puck' group by target
    private void storeDamage(String line, Long matchId) {
        String[] strings = line.split(HERO_PREFIX);
        MatchDamageEntity matchDamageEntity = new MatchDamageEntity();
        matchDamageEntity.setMatchId(matchId);
        matchDamageEntity.setHero(strings[1].split(" ")[0]);
        matchDamageEntity.setTarget(strings[2].split(" ")[0]);
        matchDamageEntity.setDamage(Integer.valueOf(strings[2].split(" ")[4]));
        jdbcTemplate.update("INSERT INTO bayes.match_damage (match_id, hero, target, damage) VALUES (?, ?, ?, ?)",
                matchDamageEntity.getMatchId(), matchDamageEntity.getHero(), matchDamageEntity.getTarget(), matchDamageEntity.getDamage());

    }

    //[00:08:46.693]
    private Long calculateTimeGame(String string) {
        Long milliSeconds;
        String time = string.split("\\[")[1].split("]")[0];
        String[] rawTime = time.split(":");
        milliSeconds =
                Long.valueOf(rawTime[0]) * 60 * 60 * 100
                        + Long.valueOf(rawTime[1]) * 60 * 1000
                        + Long.valueOf(rawTime[2].split("\\.")[0]) * 1000
                        + Long.valueOf(rawTime[2].split("\\.")[1])
        ;
        return milliSeconds;
    }

}
