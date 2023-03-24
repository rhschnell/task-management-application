package server.boards;

import commons.Board;
import org.springframework.stereotype.Repository;
import server.GetByIDRepo;


@Repository
public interface BoardRepository extends GetByIDRepo<Board, String> {
}
