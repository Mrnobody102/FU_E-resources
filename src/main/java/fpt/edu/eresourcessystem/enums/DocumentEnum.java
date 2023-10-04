package fpt.edu.eresourcessystem.enums;

public class DocumentEnum {

    public enum DocumentPermissionEnum {
        PUBLIC,
        PRIVATE,
        ONLY_LECTURER;
    }

    public enum DocumentStateEnum {
        WAIT(),
        ON_PROCESS(),
        SUCCESS(),
        FAIL();
    }
    public enum DocumentFormat {
        PDF,
        DOC,
        DOCX,
        PPTX,
        XLSX,
        MD,
        HTML,
        TXT,
        UNKNOWN;

        public static DocumentFormat getDocType(String suffixName) {
            switch (suffixName) {
                case ".pdf":
                    return PDF;
                case "docx":
                    return DOC;
                case ".docx":
                    return DOCX;
                case ".pptx":
                    return PPTX;
                case ".xlsx":
                    return XLSX;
                case ".md":
                    return MD;
                case ".html":
                    return HTML;
                case ".txt":
                    return TXT;
                default:
                    return UNKNOWN;
            }
        }
    }

    public enum AudioFileFormat {
        M4A,
        FLAC,
        MP3,
        MP4,
        WAV,
        WMA,
        AAC
    }

    public enum VideoFileFormat {
        MP4,
        MOV,
        AVI,
        FLV,
        MKV,
        WEBM
    }
}
