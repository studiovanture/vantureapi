package me.vanturestudio.vantureapi.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import org.bukkit.permissions.Permission;

import java.util.Set;
import java.util.UUID;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public abstract class UserSecureData extends BaseSecureData {
	private UUID userId;
	private String username;
	private Set<Permission> permissions;
}
