package ecommerce.com;

import com.uniform.ecommerce.EcommerceApplication;
import com.uniform.ecommerce.exception.ResourceNotFoundException;
import com.uniform.ecommerce.model.Contact;
import com.uniform.ecommerce.repository.ContactRepository;
import com.uniform.ecommerce.service.ContactService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;


import java.util.List;

@SpringBootTest(classes = EcommerceApplication.class)
public class ContactTests {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ContactService contactService;

    // Test saving a new contact message to the database
    @Test
    public void testSaveContact() {
        Contact contact = new Contact();
        contact.setName("John Doe");
        contact.setEmail("johndoe@example.com");
        contact.setPhone("1234567890");
        contact.setMessage("This is a test message.");

        Contact savedContact = contactRepository.save(contact);

        // Assert that the saved contact message has a non-null ID and the same values as the original message
        assertNotNull(savedContact.getId());
        assertEquals(contact.getName(), savedContact.getName());
        assertEquals(contact.getEmail(), savedContact.getEmail());
        assertEquals(contact.getPhone(), savedContact.getPhone());
        assertEquals(contact.getMessage(), savedContact.getMessage());
    }

    // Testing if contact messages can be retrieved from database - not null and not empty
    @Test
    public void testGetAllContacts() {
        List<Contact> contacts = contactService.getAllMessages();
        assertNotNull(contacts);
        assertFalse(contacts.isEmpty());
    }

    // Retrieving contact message by id
    @Test
    public void testGetContactById() {
        Contact contact = new Contact();
        contact.setName("Jane Doe");
        contact.setEmail("janedoe@example.com");
        contact.setPhone("0987654321");
        contact.setMessage("This is another test message.");

        Contact savedContact = contactRepository.save(contact);

        Long contactId = savedContact.getId();
        Contact retrievedContact = contactService.getMessageById(contactId);

        // Assert that the retrieved message has the same values as the saved message
        assertNotNull(retrievedContact);
        assertEquals(savedContact.getId(), retrievedContact.getId());
        assertEquals(savedContact.getName(), retrievedContact.getName());
        assertEquals(savedContact.getEmail(), retrievedContact.getEmail());
        assertEquals(savedContact.getPhone(), retrievedContact.getPhone());
        assertEquals(savedContact.getMessage(), retrievedContact.getMessage());
    }

    // testing if contact message can be deleted
    @Test
    public void testDeleteContact() {
        Contact contact = new Contact();
        contact.setName("Bob Smith");
        contact.setEmail("bobsmith@example.com");
        contact.setPhone("5555555555");
        contact.setMessage("This is a third test message.");

        Contact savedContact = contactRepository.save(contact);

        Long contactId = savedContact.getId();
        contactService.deleteMessage(contactId);

        // Should not be found by repository - assertFalse
        assertFalse(contactRepository.findById(contactId).isPresent());
    }

}
