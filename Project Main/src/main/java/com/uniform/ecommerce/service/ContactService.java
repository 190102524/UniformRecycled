package com.uniform.ecommerce.service;


import com.uniform.ecommerce.exception.ResourceNotFoundException;
import com.uniform.ecommerce.model.Contact;
import com.uniform.ecommerce.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepository contactRepository;

    public List<Contact> getAllMessages() {
        return contactRepository.findAll();
    }

    public Contact getMessageById(Long id) {
        return contactRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Message not found with id: " + id));
    }

    public void deleteMessage(Long id) {
        contactRepository.deleteById(id);
    }
}
