package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.Librarian;

public interface LibrarianService {
    Librarian addLibrarian(Librarian librarian);
    Librarian updateLibrarian(Librarian librarian);

    Librarian findByAccountId(String accountId);

}
