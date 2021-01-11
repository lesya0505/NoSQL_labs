package ua.iot.nosql;

import com.microsoft.azure.eventhubs.EventHubException;

import java.io.IOException;

public class Context {
    DisplayStrategy displayStrategy;

    public Context(DisplayStrategy displayStrategy) {
        this.displayStrategy = displayStrategy;
    }

    public void executeStrategy() throws IOException, EventHubException {
        displayStrategy.display();
    }
}
