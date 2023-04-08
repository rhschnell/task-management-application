package server.features.boards;


import commons.Board;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class BoardService implements RepositoryService<Board, String> {
    private final BoardRepository repo;

    /**
     * Constructor for the BoardService
     * @param repo a BoardRepository instance
     */
    public BoardService(BoardRepository repo) {
        this.repo = repo;
    }

    /**
     * Inserts the passed board into the server
     * @param board the board to be inserted
     * @return the inserted board
     */
    @Override
    public Board insert(Board board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        return repo.save(board);
    }

    /**
     * Deletes a board with the specified key
     * @param s key of the board to be deleted
     */
    @Override
    public void delete(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(s)) {
            throw new EntityNotFoundException();
        }
        repo.deleteById(s);
    }

    /**
     * Retrieves the board with the given key
     * @param s key of entity to be found and returned
     * @return the board with the given key
     */
    @Override
    public Board getByID(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        if (!repo.existsById(s)) {
            throw new EntityNotFoundException();
        }
        return repo.getById(s);
    }

    /**
     * Retrieves all existing boards
     * @return a list of all boards
     */
    @Override
    public List<Board> getAll() {
        return repo.findAll();
    }
}
