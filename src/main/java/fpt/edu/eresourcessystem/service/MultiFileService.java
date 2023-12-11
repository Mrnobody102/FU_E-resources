package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.model.*;

import java.util.List;

public interface MultiFileService {

    MultiFile findById(String multiFileId);

    MultiFile addMultiFile(MultiFile multiFile);

    MultiFile updateMultiFile(MultiFile multiFile);

    boolean deleteMultiFile(MultiFile multiFile);

    boolean hardDeleteMultiFile(MultiFile multiFile);
}
