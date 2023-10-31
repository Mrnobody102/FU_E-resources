package fpt.edu.eresourcessystem.repository;

import fpt.edu.eresourcessystem.model.Admin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("adminRepository")
public interface AdminRepository extends MongoRepository<Admin, String> {
    Admin findByAccountId(String accountId);

    @Override
    List<Admin> findAll();
}
