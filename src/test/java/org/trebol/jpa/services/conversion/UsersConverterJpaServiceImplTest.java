package org.trebol.jpa.services.conversion;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.trebol.exceptions.BadInputException;
import org.trebol.jpa.entities.Person;
import org.trebol.jpa.entities.User;
import org.trebol.jpa.entities.UserRole;
import org.trebol.jpa.repositories.IPeopleJpaRepository;
import org.trebol.jpa.repositories.IUserRolesJpaRepository;
import org.trebol.jpa.repositories.IUsersJpaRepository;
import org.trebol.jpa.services.ITwoWayConverterJpaService;
import org.trebol.pojo.PersonPojo;
import org.trebol.pojo.UserPojo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.trebol.constant.TestConstants.ANY;

@ExtendWith(MockitoExtension.class)
public class UsersConverterJpaServiceImplTest {

    @InjectMocks
    private UsersConverterJpaServiceImpl sut;

    @Mock
    private IUsersJpaRepository userRepository;
    @Mock
    private IUserRolesJpaRepository rolesRepository;
    @Mock
    private ITwoWayConverterJpaService<PersonPojo, Person> peopleService;
    @Mock
    private IPeopleJpaRepository peopleRepository;
    @Mock
    private ConversionService conversion;
    @Mock
    private PasswordEncoder passwordEncoder;
    private User user;
    private UserPojo userPojo;

    @BeforeEach
    void beforeEach() {
        user = new User();
        user.setName(ANY);
        user.setId(1L);
        final UserRole userRole = new UserRole();
        userRole.setName(ANY);
        user.setUserRole(userRole);
        Person person = new Person();
        user.setPerson(person);
        userPojo = new UserPojo();
        userPojo.setId(1L);
        userPojo.setName(ANY);
    }

    @AfterEach
    void afterEach() {
        user = null;
        userPojo = null;
    }

    @Test
    void testApplyChangesToExistingEntity() throws BadInputException {
        userPojo.setId(1L);
        userPojo.setName(ANY);
        userPojo.setRole(ANY);
        userPojo.setPassword(ANY);
        final PersonPojo personPojo = new PersonPojo();
        personPojo.setIdNumber(ANY);
        userPojo.setPerson(personPojo);


        user.setId(1L);
        user.setName(ANY + " ");
        final UserRole role = new UserRole();
        role.setName(ANY + " ");
        user.setUserRole(role);
        user.setPassword(ANY + " ");
        final Person person = new Person();
        person.setIdNumber(ANY + " ");
        user.setPerson(person);


        when(rolesRepository.findByName(anyString())).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn(ANY);
        when(peopleRepository.findByIdNumber(anyString())).thenReturn(Optional.of(person));

        User actual = sut.applyChangesToExistingEntity(userPojo, user);

        assertEquals(ANY + " ", actual.getPerson().getIdNumber());
    }

    @Test
    void testConvertToPojo() {
        when(peopleService.convertToPojo(any(Person.class))).thenReturn(new PersonPojo());
        UserPojo actual = sut.convertToPojo(user);
        assertNotNull(actual.getPerson());
    }

    @Test
    void testConvertToNewEntityBadInputException() throws BadInputException {
        when(conversion.convert(any(UserPojo.class), eq(User.class))).thenReturn(null);
        BadInputException badInputException = assertThrows(BadInputException.class, () -> sut.convertToNewEntity(userPojo));
        assertEquals("Invalid user data", badInputException.getMessage());
    }

    @Test
    void testConvertToNewEntity() throws BadInputException {
        when(conversion.convert(any(UserPojo.class), eq(User.class))).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn(ANY);
        final Person person = new Person();
        person.setId(3L);
        when(peopleRepository.findByIdNumber(anyString())).thenReturn(Optional.of(person));
        final UserRole userRole = new UserRole();
        userRole.setId(2L);
        when(rolesRepository.findByName(anyString())).thenReturn(Optional.of(userRole));

        userPojo.setPassword(ANY);
        final PersonPojo personPojo = new PersonPojo();
        personPojo.setIdNumber(ANY);
        userPojo.setPerson(personPojo);
        userPojo.setRole(ANY);

        User actual = sut.convertToNewEntity(userPojo);

        assertEquals(3L, actual.getPerson().getId());
        assertEquals(2L, actual.getUserRole().getId());
    }
}
