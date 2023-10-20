package open.source.nps.enumeration;

public enum FileExtension {

	CSV (".csv"),
	JSON (".json");

	private String extension;

	private FileExtension(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return extension;
	}

	public static FileExtension identify(String extension) {
		for (FileExtension fileExtension : FileExtension.values()) {
			if (fileExtension.getExtension().equalsIgnoreCase(extension)) {
				return fileExtension;
			}
		}
		return null;
	}

}
