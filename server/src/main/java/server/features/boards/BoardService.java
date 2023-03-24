package server.features.boards;


import commons.Board;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import server.features.RepositoryService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class BoardService implements RepositoryService<Board, String> {
    private final BoardRepository repo;

    public BoardService(BoardRepository repo) {
        this.repo = repo;
    }

    @Override
    public void insert(Board board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        repo.save(board);
    }

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

    @Override
    public List<Board> getAll() {
        return repo.findAll();
    }
}
