package com.riot_gaming.persist;

import com.riot_gaming.persist.Entity.SummonerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummonerRepository extends JpaRepository<SummonerEntity, String> {
  SummonerEntity findByName(String name);
}
