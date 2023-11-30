package fpt.edu.eresourcessystem.enums;

public class DocumentEnum {

    public enum DocumentStatusEnum {
        DISPLAY("1", "display"),
        HIDE("0", "hide");

        private final String displayValue;

        private final String toString;

        DocumentStatusEnum(String displayValue, String toString) {
            this.displayValue = displayValue;
            this.toString = toString;
        }
    }

    public enum DefaultCourseResourceTypes {
        MATERIALS("Materials");

        private final String displayValue;

        DefaultCourseResourceTypes(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
    }

    public enum DefaultTopicResourceTypes {
        LECTURE_NOTE("Lecture note"),
        READING("Reading"),
        ASSIGNMENT("Assignment"),
        EXERCISE("Exercise"),
        COMMON_MATERIAL("Common material"); // default, auto save in Materials

        private final String displayValue;

        DefaultTopicResourceTypes(String displayValue) {
            this.displayValue = displayValue;
        }

        public String getDisplayValue() {
            return displayValue;
        }
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

        VIDEO,

        AUDIO,

        IMAGE,

        OTHER,


        UNKNOWN;

        public static DocumentFormat getDocType(String suffixName) {
            return switch (suffixName) {
                case "pdf" -> DocumentFormat.PDF;
                case "doc" -> DocumentFormat.DOC;
                case "docx" -> DocumentFormat.DOCX;
                case "ppt" -> DocumentFormat.PPT;
                case "pptx" -> DocumentFormat.PPTX;
                case "xls" -> DocumentFormat.XLS;
                case "xlsx" -> DocumentFormat.XLSX;
                case "md" -> DocumentFormat.MD;
                case "html" -> DocumentFormat.HTML;
                case "txt" -> DocumentFormat.TXT;

                case "m4a", "flac", "mp3", "wav", "wma", "aac" -> AUDIO;

                case "mp4", "mov", "avi", "flv", "mkv", "webm" -> VIDEO;

                case "jpg", "jpeg", "gif", "png", "svg" -> IMAGE;

                case "exe", "psd", "eps", "jar", "zip", "rar" -> OTHER;

                default -> DocumentFormat.UNKNOWN;
            };
        }
    }
}
