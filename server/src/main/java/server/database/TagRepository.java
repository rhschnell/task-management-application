package server.database;

import commons.Tag;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends GetByIDRepo<Tag, Long> {
}
