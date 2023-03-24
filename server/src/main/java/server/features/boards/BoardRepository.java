package server.features.boards;

import commons.Board;
import org.springframework.stereotype.Repository;
import server.features.CustomRepository;


@Repository
public interface BoardRepository extends CustomRepository<Board, String> {
}
