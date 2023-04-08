package server.features;

import com.sun.istack.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@NoRepositoryBean
public interface CustomRepository<T, ID> extends JpaRepository<T, ID> {
    /**
     * Custom getById method because the default one is buggy
     * @param key must not be {@literal null}.
     * @return the found entity with the passed ID
     */
    @Override
    default T getById(@NotNull ID key) {
        Optional<T> optionalBoard = this.findById(key);
        if (optionalBoard.isEmpty()) {
            throw new EntityNotFoundException("How even did you manage to get here??");
        }
        return optionalBoard.get();
    }
}
