package server.tags;

import commons.Tag;
import org.springframework.stereotype.Repository;
import server.GetByIDRepo;

@Repository
public interface TagRepository extends GetByIDRepo<Tag, Long> {
}
