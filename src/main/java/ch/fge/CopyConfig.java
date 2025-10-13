package ch.fge;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.copy")
public record CopyConfig(List<String> tables) {
}
