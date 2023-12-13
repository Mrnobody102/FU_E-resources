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
        MS_DOC,
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
                case "pdf" -> PDF;
                case "doc", "docx", "ppt", "pptx" -> MS_DOC;
                case "md" -> MD;
                case "html" -> HTML;
                case "txt" -> TXT;

                case "m4a", "flac", "mp3", "wav", "wma", "aac" -> AUDIO;

                case "mp4", "mov", "avi", "flv", "mkv", "webm" -> VIDEO;

                case "jpg", "jpeg", "gif", "png", "svg" -> IMAGE;

                case "exe", "psd", "eps", "jar", "zip", "rar" -> OTHER;

                default -> DocumentFormat.UNKNOWN;
            };
        }
    }

    public enum DocumentSupportFilesFormat {
        PDF,
        MS_DOC,
        MD,
        HTML,
        TXT,

        VIDEO,

        AUDIO,

        IMAGE,

        OTHER,


        UNKNOWN;

        public static DocumentSupportFilesFormat getDocType(String suffixName) {
            return switch (suffixName) {
                case "pdf" -> PDF;
                case "doc", "xls", "docx", "ppt", "pptx", "xlsx" -> MS_DOC;
                case "md" -> MD;
                case "html" -> HTML;
                case "txt" -> TXT;

                case "m4a", "flac", "mp3", "wav", "wma", "aac" -> AUDIO;

                case "mp4", "mov", "avi", "flv", "mkv", "webm" -> VIDEO;

                case "jpg", "jpeg", "gif", "png", "svg" -> IMAGE;

                case "exe", "psd", "eps", "jar", "zip", "rar" -> OTHER;

                default -> UNKNOWN;
            };
        }
    }
}
