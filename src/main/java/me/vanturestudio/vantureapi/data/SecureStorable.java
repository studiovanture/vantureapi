package me.vanturestudio.vantureapi.data;

public interface SecureStorable {

	String getAuthor();
	void setAuthor(String author);

	String getDescription();
	void setDescription(String description);

	String getVersion();
	void setVersion(String version);

	long getCreatedAt();
	void setCreatedAt(long createdAt);

	long getModifiedAt();
	void setModifiedAt(long modifiedAt);

	String getPluginName();
	void setPluginName(String pluginName);

	String getFileType();
	void setFileType(String fileType);

	String getUUID();
	void setUUID(String uuid);

	String getChecksum();
	void setChecksum(String checksum);

	String getNotes();
	void setNotes(String notes);
}