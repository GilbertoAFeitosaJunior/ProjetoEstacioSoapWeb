package mobi.stos.projetoestacio.util;

public enum ConstantsType {

    ALL(new String[]{
        "image/png", "image/jpeg", "image/gif", "application/pdf",
        "plain/text", "application/msword", "application/vnd.ms-excel",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        "application/vnd.ms-powerpoint", "application/zip", "application/x-rar-compressed",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/vnd.openxmlformats-officedocument.presentationml.slideshow",
        "application/vnd.openxmlformats-officedocument.presentationml.presentation",
        "video/mp4", "application/mp4", "video/mp3", "application/mp3",
        "video/3gpp", "video/3gpp2", "audio/x-ms-wma",
        "audio/mpeg", "video/mpeg", "video/x-msvideo"
    }),
    IMAGE(new String[]{
        "image/png", "image/jpeg", "image/gif"
    }),
    AUDIO_VIDEO(new String[]{
        "video/mp4", "application/mp4", "video/mp3", "application/mp3",
        "video/3gpp", "video/3gpp2", "audio/x-ms-wma",
        "audio/mpeg", "video/mpeg", "video/x-msvideo"
    });
    
    private final String[] types;

    private ConstantsType(String[] types) {
        this.types = types;
    }

    public String[] getTypes() {
        return types;
    }

}
