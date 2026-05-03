package tr.edu.deu.efm.core.api;

/**
 * Defines the supported file format families for conversion operations.
 */
public enum FormatFamily {
    IMAGE,
    DOCUMENT,
    MEDIA,
    UNKNOWN;

    /**
     * Determines the format family based on the file extension.
     * 
     * @param extension The file extension (e.g., "png", ".jpg", "pdf")
     * @return The corresponding FormatFamily, or UNKNOWN if not supported.
     */
    public static FormatFamily fromExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            return UNKNOWN;
        }
        
        String ext = extension.toLowerCase();
        if (ext.startsWith(".")) {
            ext = ext.substring(1);
        }

        switch (ext) {
            case "png": case "jpg": case "jpeg": case "bmp": case "gif":
                return IMAGE;
            case "txt": case "pdf": case "docx":
                return DOCUMENT;
            case "mp4": case "mp3": case "wav": case "avi": case "mkv":
                return MEDIA;
            default:
                return UNKNOWN;
        }
    }
}