package server.database;

import commons.Board;
import org.springframework.stereotype.Repository;


@Repository
public interface BoardRepository extends GetByIDRepo<Board, String> {
}
