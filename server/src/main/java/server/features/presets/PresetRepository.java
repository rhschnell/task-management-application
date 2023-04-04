package server.features.presets;

import commons.CardColorPreset;
import org.springframework.stereotype.Repository;
import server.features.CustomRepository;

@Repository
public interface PresetRepository extends CustomRepository<CardColorPreset, Long> {

}
