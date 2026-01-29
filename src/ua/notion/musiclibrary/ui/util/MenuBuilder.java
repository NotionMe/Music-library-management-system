package ua.notion.musiclibrary.ui.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jline.consoleui.elements.ConfirmChoice;
import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.PromptResultItemIF;
import org.jline.consoleui.prompt.builder.ListPromptBuilder;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.terminal.Terminal;

public class MenuBuilder {

    private final Terminal terminal;
    private final ConsolePrompt prompt;

    public MenuBuilder(Terminal terminal) {
        this.terminal = terminal;
        this.prompt = new ConsolePrompt(terminal);
    }

    public int createListMenu(String title, List<String> options) {
        PromptBuilder builder = prompt.getPromptBuilder();

        ListPromptBuilder listBuilder = builder.createListPrompt()
                .name("menu")
                .message(title);

        for (int i = 0; i < options.size(); i++) {
            listBuilder.newItem(String.valueOf(i)).text(options.get(i)).add();
        }

        listBuilder.addPrompt();

        try {
            Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
            String selectedValue = result.get("menu").getResult();
            return Integer.parseInt(selectedValue);
        } catch (Exception e) {
            return -1;
        }
    }

    public String createInputPrompt(String message, String defaultValue) {
        PromptBuilder builder = prompt.getPromptBuilder();

        builder.createInputPrompt()
                .name("input")
                .message(message)
                .defaultValue(defaultValue != null ? defaultValue : "")
                .addPrompt();

        try {
            Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
            return result.get("input").getResult();
        } catch (Exception e) {
            return null;
        }
    }

    public String createMaskedInput(String message) {
        PromptBuilder builder = prompt.getPromptBuilder();

        builder.createInputPrompt()
                .name("password")
                .message(message)
                .mask('*')
                .addPrompt();

        try {
            Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
            return result.get("password").getResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean createConfirmation(String message) {
        String response = createInputPrompt(message + " (y/n)", "n");
        return response != null && (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes"));
    }

    public Map<String, String> createMultiInputPrompt(Map<String, String> fields) {
        PromptBuilder builder = prompt.getPromptBuilder();

        for (Map.Entry<String, String> field : fields.entrySet()) {
            builder.createInputPrompt()
                    .name(field.getKey())
                    .message(field.getValue())
                    .addPrompt();
        }

        try {
            Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
            Map<String, String> inputs = new HashMap<>();

            for (String key : fields.keySet()) {
                inputs.put(key, result.get(key).getResult());
            }

            return inputs;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public Terminal getTerminal() {
        return terminal;
    }
}
