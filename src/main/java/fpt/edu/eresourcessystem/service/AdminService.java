package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Admin;

public interface AdminService {
    Admin addAdmin(Admin admin);
    Admin updateAdmin(Admin admin);

    Admin findByAccountId(String accountId);



}
