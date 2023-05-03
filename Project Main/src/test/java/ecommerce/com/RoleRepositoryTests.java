package ecommerce.com;


import com.uniform.ecommerce.EcommerceApplication;
import com.uniform.ecommerce.model.Role;
import com.uniform.ecommerce.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


import java.util.List;


@SpringBootTest(classes = EcommerceApplication.class)
@Transactional
public class RoleRepositoryTests {

    @Autowired
    RoleRepository roleRepo;

    @Test
    public void testCreateRoles() {
        Role user = new Role("USER");
        Role admin = new Role("ADMIN");

        roleRepo.saveAll(List.of(user, admin));

        List<Role> listRoles = roleRepo.findAll();
        assertThat(listRoles.size()).isEqualTo(2);
    }
}
