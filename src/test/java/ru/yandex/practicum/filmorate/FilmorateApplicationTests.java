package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.*;


@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
class FilmorateApplicationTests {

    private final FilmDBStorage filmDBStorage;
    private final UserDBStorage userDBStorage;
    private final GenreDBStorage genreDBStorage;
    private final LikeDBStorage likeDBStorage;
    private final MpaDBStorage mpaDBStorage;
    private final DirectorDBStorage directorDBStorage;


    //ТЕСТЫ GENRES

    @DisplayName("Тест получения корректного имени Genre по ID")
    @Test
    public void findGenreByIdTest() {
        Genre genre = genreDBStorage.readById(1);
        Assertions.assertEquals("Комедия", genre.getName(), "Ожидался корректное имя Genre");
    }

    @DisplayName("Тест получения Genre по Film ID")
    @Test
    public void findGenreByFilmIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("The Big Lebowski")
                .description("Cool film")
                .releaseDate(LocalDate.of(1990, 5, 5))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .mpa(new Mpa(4, "R"))
                .build();

        filmDBStorage.saveFilm(film);
        List<Genre> genres = new ArrayList<>(genreDBStorage.getGenresByFilmID(1L));
        Assertions.assertEquals("Комедия", genres.get(0).getName(), "Ожидался корректный Genre " +
                "у конкретного Film");
    }

    @DisplayName("Тест получения всех Genre")
    @Test
    public void getAllGenresTest() {
        Assertions.assertEquals(6, genreDBStorage.readAll().size(), "Ожидались коррктные Genres");
    }


    // ТЕСТЫ MPA

    @DisplayName("Тест получения имени Mpa по ID")
    @Test
    public void findMpaByIdTest() {
        Mpa mpa = new Mpa(4, "R");
        Assertions.assertEquals(mpa, mpaDBStorage.readById(4), "Ожидался корректный Mpa");
    }

    @DisplayName("Тест получения Mpa по Film ID")
    @Test
    public void findMpaByFilmIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("The Big Lebowski")
                .description("Cool film")
                .releaseDate(LocalDate.of(1990, 5, 5))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);
        Assertions.assertEquals(film.getMpa(), mpaDBStorage.readById(4), "Ожидались коррктные Mpa");
    }

    @DisplayName("Тест получения всех Mpa")
    @Test
    public void findAllMpaTest() {
        Assertions.assertEquals(5, mpaDBStorage.readAll().size(), "Ожидались коррктные Mpa");
    }


    // ТЕСТЫ USER

    @DisplayName("Тест создания новго User")
    @Test
    public void createUserTest() {
        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("Aknaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Ожидались коррктный новый User после создания");
    }

    @DisplayName("Тест обновления данных у существующего User")
    @Test
    public void updateUserTest() {
        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("Aknaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        user.setName("Vovan");
        userDBStorage.updateUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Ожидались коррктный User после update");
    }

    @DisplayName("Тест получения существующего User по ID")
    @Test
    public void getUserById() {
        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("Aknaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        Assertions.assertEquals(user, userDBStorage.getUserById(1L), "Ожидался коррктный User");
    }

//    @Test
//    public void deleteUserTest(){
//        User user = User.builder()
//                .id(1L)
//                .name("123")
//                .email("123@asd.ru")
//                .login("Tert")
//                .birthday(LocalDate.of(1988, 5, 29))
//                .friends(new HashSet<>())
//                .build();
//        userDBStorage.saveUser(user);
//        Assertions.assertEquals(1,userDBStorage.readAllUsers().size());
//        userDBStorage.
//    }

    @DisplayName("Тест получения всех существующих User")
    @Test
    public void readAllUsers() {
        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("Aknaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        User user2 = User.builder()
                .id(2L)
                .name("Pavel")
                .email("pavel@yandex.ru")
                .login("pasHtetKING")
                .birthday(LocalDate.of(1999, 8, 12))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        userDBStorage.saveUser(user2);
        Assertions.assertEquals(2, userDBStorage.readAllUsers().size(), "Ожидались коррктные Userы");
    }


    //ТЕСТЫ FILM

    @DisplayName("Тест создания новго Film")
    @Test
    public void createFilmTest() {
        Film film = Film.builder()
                .id(1L)
                .name("The Big Lebowski")
                .description("Cool film")
                .releaseDate(LocalDate.of(1998, 1, 18))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .directors(new HashSet<>())
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Ожидался коррктный Film");
    }

    @DisplayName("Тест обновления существующего Film")
    @Test
    public void updateFilmTest() {
        Film film = Film.builder()
                .id(1L)
                .name("The Big Lebowski")
                .description("Cool film")
                .releaseDate(LocalDate.of(1998, 1, 18))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .directors(new HashSet<>())
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);
        film.setName("654");
        filmDBStorage.updateFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Ожидался коррктный Film после update");
    }

    @DisplayName("Тест получения всех Film")
    @Test
    public void readAllFilms() {
        Film film = Film.builder()
                .id(1L)
                .name("The Big Lebowski")
                .description("Cool film")
                .releaseDate(LocalDate.of(1998, 1, 18))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        Film film2 = Film.builder()
                .id(1L)
                .name("Snatch")
                .description("Cool film")
                .releaseDate(LocalDate.of(2000, 8, 23))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);
        filmDBStorage.saveFilm(film2);
        Assertions.assertEquals(2, filmDBStorage.readAllFilms().size(), "Ожидались коррктные Films");
    }

    @DisplayName("Тест получения существующего Film по ID")
    @Test
    public void getFilmByIdTest() {
        Film film = Film.builder()
                .id(1L)
                .name("Snatch")
                .description("Cool film")
                .releaseDate(LocalDate.of(2000, 8, 23))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .directors(new HashSet<>())
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);
        Assertions.assertEquals(film, filmDBStorage.getFilmById(1L), "Ожидались коррктный Film с конкретным id");
    }


    //ТЕСТЫ LIKES

    @DisplayName("Тест создания Like к существующему Film существующим User")
    @Test
    public void addLikesByFilmId() {
        Film film = Film.builder()
                .id(1L)
                .name("Snatch")
                .description("Cool film")
                .releaseDate(LocalDate.of(2000, 8, 23))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);

        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("AKnaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        likeDBStorage.addLike(1L, 1L);
        Long[] expected = {1L};
        Assertions.assertArrayEquals(expected, filmDBStorage.getFilmById(1L).getLikes().toArray(), "Ожидались " +
                "новый Like к Film с конкретным id");
    }

    @DisplayName("Тест удаления Like к существующему Film существующим User")
    @Test
    public void deleteLikesByFilmId() {
        Film film = Film.builder()
                .id(1L)
                .name("Snatch")
                .description("Cool film")
                .releaseDate(LocalDate.of(2000, 8, 23))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);

        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("AKnaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        likeDBStorage.addLike(1L, 1L);
        Long[] expected = {1L};
        Assertions.assertArrayEquals(expected, filmDBStorage.getFilmById(1L).getLikes().toArray());
        likeDBStorage.deleteLike(1L, 1L);
        Long[] expectedAfterDelete = {};
        Assertions.assertArrayEquals(expectedAfterDelete, filmDBStorage.getFilmById(1L).getLikes().toArray(), "Ожидалось " +
                "удаление существующего Like у Film с конкретным id");
    }

    @DisplayName("Тест получения Like к существующему Film по Id")
    @Test
    public void getAllLikesTestByFilmId() {
        Film film = Film.builder()
                .id(1L)
                .name("Snatch")
                .description("Cool film")
                .releaseDate(LocalDate.of(2000, 8, 23))
                .duration(120)
                .genres(Set.of(new Genre(1, "Комедия")))
                .likes(new HashSet<>())
                .mpa(new Mpa(4, "R"))
                .build();
        filmDBStorage.saveFilm(film);

        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("AKnaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        likeDBStorage.addLike(1L, 1L);

        User user2 = User.builder()
                .id(2L)
                .name("Pavel")
                .email("pavel@yandex.ru")
                .login("pasHtetKING")
                .birthday(LocalDate.of(1999, 8, 12))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user2);
        likeDBStorage.addLike(1L, 2L);

        Long[] expected = {1L, 2L};
        Assertions.assertArrayEquals(expected, filmDBStorage.getFilmById(1L).getLikes().toArray(), "Ожидалось получение " +
                "всех существующих Like у Film с конкретным id");
    }


    //ТЕСТЫ FRIENDS

    @DisplayName("Тест добавления Friend к существующему User по Id")
    @Test
    public void addFriendByUserIdTest() {
        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("AKnaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(2L)
                .name("Pavel")
                .email("pavel@yandex.ru")
                .login("MegaPavel")
                .birthday(LocalDate.of(1997, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        userDBStorage.saveUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] expected = {friend};
        Assertions.assertArrayEquals(expected, userDBStorage.getAllFriendByUserId(1L).toArray(), "Ожидалось "
                + "добавление friend к конкретному User");
    }

    @DisplayName("Тест удаления Friend у существующего User по Id")
    @Test
    public void deleteFriendByUserIdTest() {
        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("AKnaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(2L)
                .name("Pavel")
                .email("pavel@yandex.ru")
                .login("MegaPavel")
                .birthday(LocalDate.of(1997, 5, 29))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        userDBStorage.saveUser(friend);
        userDBStorage.userAddFriend(1L, 2L);
        User[] expected = {friend};
        Assertions.assertArrayEquals(expected, userDBStorage.getAllFriendByUserId(1L).toArray());
        userDBStorage.userDeleteFriend(1L, 2L);
        User[] expectedAfterDelete = {};
        Assertions.assertArrayEquals(expectedAfterDelete, userDBStorage.getAllFriendByUserId(1L).toArray(), "Ожидалось " +
                "удаление friend у конкретного User");
    }

    @DisplayName("Тест получения всех Friends у существующего User по Id")
    @Test
    public void getAllFriendByUserIdTest() {
        User user = User.builder()
                .id(1L)
                .name("Andrey")
                .email("ak@aknaz.ru")
                .login("AKnaz")
                .birthday(LocalDate.of(1988, 5, 29))
                .friends(new HashSet<>())
                .build();
        User friend = User.builder()
                .id(2L)
                .name("Pavel")
                .email("pavel@yandex.ru")
                .login("MegaPavel")
                .birthday(LocalDate.of(1997, 5, 29))
                .friends(new HashSet<>())
                .build();
        User friend2 = User.builder()
                .id(3L)
                .name("Oleg")
                .email("oleg@yandex.ru")
                .login("cOOleg")
                .birthday(LocalDate.of(1998, 6, 26))
                .friends(new HashSet<>())
                .build();
        userDBStorage.saveUser(user);
        userDBStorage.saveUser(friend);
        userDBStorage.saveUser(friend2);
        userDBStorage.userAddFriend(1L, 2L);
        userDBStorage.userAddFriend(1L, 3L);
        User[] expected = {friend, friend2};
        Assertions.assertArrayEquals(expected, userDBStorage.getAllFriendByUserId(1L).toArray(), "Ожидалось получение " +
                "всех friend у конкретного User");
    }

    @DisplayName("Тест создания нового Director")
    @Test
    public void createDirectorTest() {
        Director director = Director.builder()
                .id(1L)
                .name("Братья (Сестры) Вачовски")
                .build();
        directorDBStorage.saveDirector(director);
        Assertions.assertEquals(director, directorDBStorage.getDirectorById(1L).get(), "Ожидался корректный Director");
    }

    @DisplayName("Тест обновления существующего Director")
    @Test
    public void updateDirectorTest() {
        Director director = Director.builder()
                .id(1L)
                .name("Братья (Сестры) Вачовски")
                .build();
        directorDBStorage.saveDirector(director);
        director.setName("Спилберг");
        directorDBStorage.updateDirector(director);
        Assertions.assertEquals(director, directorDBStorage.getDirectorById(1L).get(), "Ожидался коректный Director");
    }

    @DisplayName("Тест получения всех Directors")
    @Test
    public void readAllDirectors() {
        Director director = Director.builder()
                .id(1L)
                .name("Братья (Сестры) Вачовски")
                .build();
        Director director2 = Director.builder()
                .id(2L)
                .name("Сталоне")
                .build();
        directorDBStorage.saveDirector(director);
        directorDBStorage.saveDirector(director2);
        Assertions.assertEquals(2, directorDBStorage.readAllDirectors().size(), "Ожидался корректный Director");
    }

    @DisplayName("Тест получения существующего Director по ID")
    @Test
    public void getDirectorByIdTest() {
        Director director = Director.builder()
                .id(1L)
                .name("Братья (Сестры) Вачовски")
                .build();
        directorDBStorage.saveDirector(director);
        Assertions.assertEquals(director, directorDBStorage.getDirectorById(1L).get(), "Ожидался коректный Director");
    }

    @DisplayName("Тест удаления Director по ID")
    @Test
    public void deleteDirectorById() {
        Director director = Director.builder()
                .id(1L)
                .name("Братья (Сестры) Вачовски")
                .build();
        Director director2 = Director.builder()
                .id(2L)
                .name("Сталоне")
                .build();
        directorDBStorage.saveDirector(director);
        directorDBStorage.saveDirector(director2);
        directorDBStorage.deleteDirectorById(1L);
        Assertions.assertEquals(1, directorDBStorage.readAllDirectors().size(), "Ожидался корректный Director");
    }
}
