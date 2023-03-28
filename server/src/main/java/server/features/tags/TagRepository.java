package server.features.tags;

import commons.Tag;
import org.springframework.stereotype.Repository;
import server.features.CustomRepository;

@Repository
public interface TagRepository extends CustomRepository<Tag, Long> {
}
