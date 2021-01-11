package ua.iot.nosql;

import com.microsoft.azure.eventhubs.EventHubException;

import java.io.IOException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) throws IOException, EventHubException {
        Context context;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Press 1 to load data to CONSOLE");
        System.out.println("Press 2 to load data to AZURE EVENTS HUB");

        int option = scanner.nextInt();

        switch (option) {
            case 1:
                context = new Context(new Console());
                context.executeStrategy();
                break;
            case 2:
                context = new Context(new EventHub());
                context.executeStrategy();
                break;
        }
    }
}