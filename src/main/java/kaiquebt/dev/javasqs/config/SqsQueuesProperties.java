package kaiquebt.dev.javasqs.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "sqs")
public class SqsQueuesProperties {
    private Map<String, String> queues;
}
