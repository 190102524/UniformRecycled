package ecommerce.com;

import static org.assertj.core.api.Assertions.assertThat;


import com.uniform.ecommerce.EcommerceApplication;
import com.uniform.ecommerce.model.Role;
import com.uniform.ecommerce.model.User;
import com.uniform.ecommerce.repository.RoleRepository;
import com.uniform.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    // registering a new user
    @Test
    public void testCreateUser() {
        User user = new User();
        user.setFirstName("Abdul-Jabbar");
        user.setLastName("Khan");
        user.setEmail("190102524@aston.ac.uk");
        user.setPassword("testing123");
        User savedUser = userRepo.save(user);
        User existUser = entityManager.find(User.class, savedUser.getId());
        assertThat(user.getEmail()).isEqualTo(existUser.getEmail());
    }

    // adding authentication to user
    @Test
    public void testAddRoleToNewUser() {
        Role roleAdmin = roleRepo.findByName("Admin");
        User user = new User();
        user.setFirstName("Abdul-Jabbar");
        user.setLastName("Khan");
        user.setEmail("190102524@aston.ac.uk");
        user.setPassword("testing123");
        user.addRole(roleAdmin);
        User savedUser = userRepo.save(user);
        assertThat(savedUser.getRoles().size()).isEqualTo(1);
    }



}
