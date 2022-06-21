package blog.service.impl;

import blog.entity.Contact;
import blog.repository.ContactRepository;
import blog.service.ContactService;
import blog.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl extends BaseServiceImpl<Contact> implements ContactService {

    protected ContactServiceImpl(ContactRepository contactRepository) {
        super(contactRepository);
    }
}
