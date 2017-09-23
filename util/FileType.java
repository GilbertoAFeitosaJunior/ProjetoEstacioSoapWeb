package mobi.stos.projetoestacio.util;

public class FileType {

    public static String mimeToExtensao(String mime) {
        switch (mime) {
            case "image/png":
                return "png";
            case "image/jpeg":
                return "jpg";
            case "image/gif":
                return "gif";
            case "application/pdf":
                return "pdf";
            case "application/msword":
                return "doc";
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
                return "docx";
            case "application/vnd.ms-excel":
                return "xls";
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
                return "xlsx";
            case "application/vnd.ms-powerpoint":
                return "ppt";
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                return "pptx";
            case "application/vnd.openxmlformats-officedocument.presentationml.slideshow":
                return "ppsx";
        }
        return "";
    }

    public static String extensaoToMime(String extensao) {

        return "";
    }
}
