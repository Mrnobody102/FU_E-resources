package fpt.edu.eresourcessystem.service;

import fpt.edu.eresourcessystem.enums.CommonEnum;
import fpt.edu.eresourcessystem.model.MultiFile;
import fpt.edu.eresourcessystem.repository.MultiFileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service("multiFileService")
public class MultiFileServiceImpl implements MultiFileService{
    private final MultiFileRepository multiFileRepository;

    public MultiFileServiceImpl(MultiFileRepository multiFileRepository) {
        this.multiFileRepository = multiFileRepository;
    }


    @Override
    public MultiFile findById(String multiFileId) {
        Optional<MultiFile> multiFile = multiFileRepository.findByIdAndDeleteFlg(multiFileId, CommonEnum.DeleteFlg.PRESERVED);
        return multiFile.isPresent() ? multiFile.get() : null;
    }

    @Override
    public MultiFile findByCloudFileName(String cloudFileName) {
        Optional<MultiFile> multiFile = multiFileRepository.findByCloudFileNameAndDeleteFlg(cloudFileName);
        return multiFile.isPresent() ? multiFile.get() : null;
    }

    @Override
    public MultiFile addMultiFile(MultiFile multiFile) {
        if (null == multiFile.getId()) {
            MultiFile result = multiFileRepository.save(multiFile);
            return result;
        } else if (!multiFileRepository.findById(multiFile.getId().trim()).isPresent()) {
            MultiFile result = multiFileRepository.save(multiFile);
            return result;
        }
        return null;
    }

    @Override
    public MultiFile updateMultiFile(MultiFile multiFile) {
        MultiFile savedMultiFile = multiFileRepository
                .findByIdAndDeleteFlg(multiFile.getId(), CommonEnum.DeleteFlg.PRESERVED).orElse(null);
        if(null != savedMultiFile){
            MultiFile result =  multiFileRepository.save(multiFile);
            return result;
        }
        return null;
    }

    @Override
    public boolean deleteMultiFile(MultiFile multiFile) {
        MultiFile check = multiFileRepository
                .findByIdAndDeleteFlg(multiFile.getId(), CommonEnum.DeleteFlg.PRESERVED).orElse(null);
        if(null != check){
            check.setDeleteFlg(CommonEnum.DeleteFlg.DELETED);
            multiFileRepository.save(check);
            return true;
        }
        return false;
    }

    @Override
    public boolean hardDeleteMultiFile(MultiFile multiFile) {
        MultiFile check = multiFileRepository
                .findByIdAndDeleteFlg(multiFile.getId(), CommonEnum.DeleteFlg.PRESERVED).orElse(null);
        if(null != check){
            multiFileRepository.delete(check);
            return true;
        }
        return false;
    }

    @Override
    public boolean hardDeleteMultiFile(String cloudFileName) {
        MultiFile check = multiFileRepository
                .findByCloudFileNameAndDeleteFlg(cloudFileName).orElse(null);
        if(null != check){
            multiFileRepository.delete(check);
            return true;
        }
        return false;
    }
}
