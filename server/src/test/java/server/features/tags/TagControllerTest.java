package server.features.tags;

import commons.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TagControllerTest {

    private TestTagRepository repo;

    private TagController sut;

    @BeforeEach
    public void setup() {
        repo = new TestTagRepository();
        sut = new TagController(new TagService(repo));
    }


    @Test
    void insertInvalid() {
        assertEquals(HttpStatus.BAD_REQUEST, sut.insert(null).getStatusCode());
    }

    @Test
    void insertValid() {
        Tag toAdd = new Tag("Urgent", "Red");

        ResponseEntity<Void> response = sut.insert(toAdd);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(toAdd, repo.getById(toAdd.getId()));
    }

    @Test
    void findAll() {
        Tag tag1 = new Tag("Urgent", "Red");
        Tag tag2 = new Tag("Sexy", "Red");

        sut.insert(tag1);
        sut.insert(tag2);
        List<Tag> actual = sut.getAll().getBody();
        List<Tag> expected = List.of(tag1, tag2);

        assertEquals(expected, actual);

    }

    @Test
    void getByIdSuccess() {
        Tag tag = new Tag("Urgent", "Red");
        ResponseEntity<Void> response = sut.insert(tag);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tag, sut.getById(tag.getId()).getBody());
    }

    @Test
    void getByIdNotFound() {
        ResponseEntity<Tag> foundById = sut.getById(100);

        assertEquals(HttpStatus.NOT_FOUND, foundById.getStatusCode());
    }

    @Test
    void getByIdBadRequest() {
        ResponseEntity<Tag> foundById = sut.getById(-1);

        assertEquals(HttpStatus.BAD_REQUEST, foundById.getStatusCode());
    }

    @Test
    void deleteNonExisting() {
        ResponseEntity<Void> response = sut.delete(100);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

    }

    @Test
    void deleteBadRequest() {
        ResponseEntity<Void> response = sut.delete(-1);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void deleteExisting() {
        Tag tag = new Tag("Urgent", "Red");

        sut.insert(tag);
        ResponseEntity<Void> response = sut.delete(tag.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(repo.getTags().contains(tag));
    }
}