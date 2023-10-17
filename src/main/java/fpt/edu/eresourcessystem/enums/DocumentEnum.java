package fpt.edu.eresourcessystem.enums;

public class DocumentEnum {

    public enum DocumentAccessLevelEnum {
        PUBLIC,
        EXCEPT_GUESTS,
        PRIVATE // only uploader (Lecturer) and Librarians
    }

    public enum DocumentStateEnum {
        WAIT(),
        ON_PROCESS(),
        SUCCESS(),
        FAIL()
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
            return switch (suffixName) {
                case ".pdf" -> PDF;
                case "docx" -> DOC;
                case ".docx" -> DOCX;
                case ".pptx" -> PPTX;
                case ".xlsx" -> XLSX;
                case ".md" -> MD;
                case ".html" -> HTML;
                case ".txt" -> TXT;
                default -> UNKNOWN;
            };
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
