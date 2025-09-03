package me.vanturestudio.vantureapi.data;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseSecureData implements SecureStorable {
	private String author = "unknown";
	private String description = "";
	private String version = "1.0";
	private String pluginName = "unknown";
	private String fileType = "generic";
	private long createdAt = Instant.now().toEpochMilli();
	private long modifiedAt = Instant.now().toEpochMilli();
	private String checksum = "";
	private String notes = "";
	private boolean encrypted = true;

	// REQUIRED BY SecureStorable
	private String uuid = UUID.randomUUID().toString();

	@Override
	public String getUUID() {
		return uuid;
	}

	@Override
	public void setUUID(String uuid) {
		this.uuid = uuid;
	}

	// Implement any other abstract methods from SecureStorable similarly...
}