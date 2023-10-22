package fpt.edu.eresourcessystem.enums;

public class DocumentEnum {

    public enum DocumentDisplayEnum {
        DISPLAY("1", "display"),
        HIDE("0", "hide");

        private final String displayValue;

        private final String toString;

        DocumentDisplayEnum(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }
    }

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
        PPT,
        PPTX,
        XLS,
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
                case ".ppt" -> PPT;
                case ".pptx" -> PPTX;
                case ".xls" -> XLS;
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
