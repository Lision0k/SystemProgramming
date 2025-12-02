import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {

    private static final String IP_ADDR = "192.168.101.20"; // Меняется в зависимости от подключенной сети
    private static final int PORT = 8189;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;

    // Цвета для ников пользователей
    private static final Color[] USER_COLORS = {
            new Color(220, 50, 50),
            new Color(50, 150, 50),
            new Color(50, 100, 220),
            new Color(180, 80, 220),
            new Color(220, 150, 50),
            new Color(50, 180, 180),
            new Color(180, 50, 120),
    };

    private Map<String, Color> userColors = new HashMap<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ClientWindow();
            }
        });
    }

    private final JTextPane log = new JTextPane();
    private final JTextField fieldNickname = new JTextField("Гость");
    private final JTextPane fieldInput = new JTextPane();

    // Кнопки форматирования
    private final JButton boldButton = new JButton("B");
    private final JButton italicButton = new JButton("I");
    private final JButton strikeButton = new JButton("S");
    private final JButton colorButton = new JButton("Цвет");

    // Текущие стили для форматирования
    private boolean isBold = false;
    private boolean isItalic = false;
    private boolean isStrike = false;
    private Color currentColor = Color.BLACK;

    private TCPConnection connection;

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);

        add(fieldNickname, BorderLayout.NORTH);

        // Слушатель для изменения ника
        fieldNickname.addActionListener(e -> {
            // При изменении ника добавляем его в карту цветов
            if (!userColors.containsKey(fieldNickname.getText())) {
                userColors.put(fieldNickname.getText(), getColorForUser(fieldNickname.getText()));
            }
        });

        // Настройка лога (JTextPane)
        log.setEditable(false);
        log.setContentType("text/html");
        // Устанавливаем начальный HTML документ
        log.setText("<html><body style='font-family: SansSerif; font-size: 12px; line-height: 1.4; padding: 5px;'></body></html>");

        JScrollPane logScrollPane = new JScrollPane(log);
        logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(logScrollPane, BorderLayout.CENTER);

        // Настройка поля ввода
        fieldInput.setPreferredSize(new Dimension(WIDTH - 100, 50));
        fieldInput.setBackground(new Color(240, 240, 240));

        // Настройка Enter для отправки и Shift+Enter для новой строки
        setupEnterKeyBehavior();

        // Панель для кнопок форматирования и поля ввода
        JPanel inputPanel = new JPanel(new BorderLayout());

        // Панель для кнопок форматирования
        JPanel formatPanel = new JPanel();
        formatPanel.setLayout(new BoxLayout(formatPanel, BoxLayout.Y_AXIS));
        formatPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Настройка кнопок
        setupFormatButton(boldButton, Color.LIGHT_GRAY, Font.BOLD, "Жирный");
        setupFormatButton(italicButton, Color.LIGHT_GRAY, Font.ITALIC, "Курсив");
        setupFormatButton(strikeButton, Color.LIGHT_GRAY, "S", "Зачёркнутый");
        setupColorButton();

        formatPanel.add(boldButton);
        formatPanel.add(Box.createVerticalStrut(5));
        formatPanel.add(italicButton);
        formatPanel.add(Box.createVerticalStrut(5));
        formatPanel.add(strikeButton);
        formatPanel.add(Box.createVerticalStrut(5));
        formatPanel.add(colorButton);

        inputPanel.add(new JScrollPane(fieldInput), BorderLayout.CENTER);
        inputPanel.add(formatPanel, BorderLayout.EAST);

        // Кнопка отправки
        JButton sendButton = new JButton("Отправить");
        sendButton.addActionListener(this);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(inputPanel, BorderLayout.CENTER);
        southPanel.add(sendButton, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Добавляем слушатели для кнопок форматирования
        boldButton.addActionListener(e -> toggleBold());
        italicButton.addActionListener(e -> toggleItalic());
        strikeButton.addActionListener(e -> toggleStrike());
        colorButton.addActionListener(e -> changeColor());

        // Устанавливаем начальный стиль для поля ввода
        updateInputFieldStyle();

        // Добавляем цвет для текущего пользователя
        userColors.put(fieldNickname.getText(), getColorForUser(fieldNickname.getText()));

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMessage("Connection exception: " + e);
        }
    }

    private Color getColorForUser(String username) {
        if (userColors.containsKey(username)) {
            return userColors.get(username);
        }

        // Генерируем стабильный цвет на основе имени
        int hash = username.hashCode();
        int index = Math.abs(hash) % USER_COLORS.length;
        Color color = USER_COLORS[index];
        userColors.put(username, color);
        return color;
    }

    private void setupFormatButton(JButton button, Color bgColor, int fontStyle, String tooltip) {
        Font font = button.getFont();
        button.setFont(new Font(font.getName(), fontStyle, 12));
        button.setBackground(bgColor);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(50, 25));
        button.setMaximumSize(new Dimension(50, 25));
        button.setFocusable(false);
    }

    private void setupFormatButton(JButton button, Color bgColor, String text, String tooltip) {
        button.setText(text);
        button.setBackground(bgColor);
        button.setToolTipText(tooltip);
        button.setPreferredSize(new Dimension(50, 25));
        button.setMaximumSize(new Dimension(50, 25));
        button.setFocusable(false);
    }

    private void setupColorButton() {
        colorButton.setBackground(currentColor);
        colorButton.setForeground(getContrastColor(currentColor));
        colorButton.setToolTipText("Изменить цвет текста");
        colorButton.setPreferredSize(new Dimension(50, 25));
        colorButton.setMaximumSize(new Dimension(50, 25));
        colorButton.setFocusable(false);
    }

    private void setupEnterKeyBehavior() {
        // Enter - отправка сообщения
        fieldInput.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "sendAction");
        fieldInput.getActionMap().put("sendAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        // Shift+Enter - новая строка
        fieldInput.getInputMap().put(KeyStroke.getKeyStroke("shift ENTER"), "insertBreak");
        fieldInput.getActionMap().put("insertBreak", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldInput.replaceSelection("\n");
            }
        });
    }

    private Color getContrastColor(Color color) {
        double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
        return luminance > 0.5 ? Color.BLACK : Color.WHITE;
    }

    private void toggleBold() {
        isBold = !isBold;
        applyFormattingToSelectionOrSetStyle();
        updateButtonAppearance();
    }

    private void toggleItalic() {
        isItalic = !isItalic;
        applyFormattingToSelectionOrSetStyle();
        updateButtonAppearance();
    }

    private void toggleStrike() {
        isStrike = !isStrike;
        applyFormattingToSelectionOrSetStyle();
        updateButtonAppearance();
    }

    private void changeColor() {
        Color newColor = JColorChooser.showDialog(
                this,
                "Выберите цвет текста",
                currentColor
        );

        if (newColor != null) {
            currentColor = newColor;
            applyFormattingToSelectionOrSetStyle();
            updateButtonAppearance();
        }
    }

    private void applyFormattingToSelectionOrSetStyle() {
        int start = fieldInput.getSelectionStart();
        int end = fieldInput.getSelectionEnd();

        if (start == end) {
            // Нет выделения - устанавливаем стиль для будущего текста
            updateInputFieldStyle();
        } else {
            // Есть выделение - применяем стиль к выделенному тексту
            StyledDocument doc = fieldInput.getStyledDocument();
            SimpleAttributeSet attr = new SimpleAttributeSet();
            StyleConstants.setBold(attr, isBold);
            StyleConstants.setItalic(attr, isItalic);
            StyleConstants.setStrikeThrough(attr, isStrike);
            StyleConstants.setForeground(attr, currentColor);

            doc.setCharacterAttributes(start, end - start, attr, false);
        }
    }

    private void updateInputFieldStyle() {
        // Устанавливаем стиль для нового вводимого текста
        SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setBold(attr, isBold);
        StyleConstants.setItalic(attr, isItalic);
        StyleConstants.setStrikeThrough(attr, isStrike);
        StyleConstants.setForeground(attr, currentColor);

        fieldInput.setCharacterAttributes(attr, false);
    }

    private void updateButtonAppearance() {
        // Обновляем внешний вид кнопок
        boldButton.setBackground(isBold ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        boldButton.setForeground(isBold ? Color.WHITE : Color.BLACK);

        italicButton.setBackground(isItalic ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        italicButton.setForeground(isItalic ? Color.WHITE : Color.BLACK);

        strikeButton.setBackground(isStrike ? Color.DARK_GRAY : Color.LIGHT_GRAY);
        strikeButton.setForeground(isStrike ? Color.WHITE : Color.BLACK);

        colorButton.setBackground(currentColor);
        colorButton.setForeground(getContrastColor(currentColor));
    }

    private String getFormattedMessage() {
        StringBuilder result = new StringBuilder();
        StyledDocument doc = fieldInput.getStyledDocument();

        try {
            String text = doc.getText(0, doc.getLength());
            if (text.trim().isEmpty()) return "";

            int length = doc.getLength();
            int pos = 0;

            while (pos < length) {
                Element element = doc.getCharacterElement(pos);
                AttributeSet attrs = element.getAttributes();

                boolean bold = StyleConstants.isBold(attrs);
                boolean italic = StyleConstants.isItalic(attrs);
                boolean strike = StyleConstants.isStrikeThrough(attrs);
                Color color = StyleConstants.getForeground(attrs);

                // Добавляем открывающие теги
                if (bold) result.append("[B]");
                if (italic) result.append("[I]");
                if (strike) result.append("[S]");
                if (!color.equals(Color.BLACK)) {
                    result.append(String.format("[C#%02X%02X%02X]",
                            color.getRed(), color.getGreen(), color.getBlue()));
                }

                // Добавляем текст
                int end = element.getEndOffset();
                String segment = text.substring(pos, Math.min(end, length));
                result.append(segment);

                // Добавляем закрывающие теги
                if (!color.equals(Color.BLACK)) result.append("[/C]");
                if (strike) result.append("[/S]");
                if (italic) result.append("[/I]");
                if (bold) result.append("[/B]");

                pos = end;
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        return result.toString();
    }

    private void sendMessage() {
        String formattedText = getFormattedMessage();
        if (formattedText.trim().isEmpty()) return;

        // Отправляем форматированный текст
        connection.sendString(fieldNickname.getText() + ": " + formattedText);

        // Очищаем поле ввода
        fieldInput.setText("");

        // Сбрасываем форматирование после отправки
        resetFormatting();
    }

    private void resetFormatting() {
        isBold = false;
        isItalic = false;
        isStrike = false;
        currentColor = Color.BLACK;

        updateInputFieldStyle();
        updateButtonAppearance();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        sendMessage();
    }

    private void appendFormattedMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Разделяем никнейм и сообщение
                int colonIndex = message.indexOf(": ");
                if (colonIndex == -1) {
                    // Если нет разделителя, это может быть системное сообщение
                    if (message.contains("TCPConnection") ||
                            message.contains("Client connected") ||
                            message.startsWith("Connection")) {
                        printMessage(message);
                    } else {
                        appendToLog(message, false);
                    }
                    return;
                }

                String nickname = message.substring(0, colonIndex);
                String formattedText = message.substring(colonIndex + 2);

                // Получаем цвет для ника
                Color nicknameColor = getColorForUser(nickname);
                String colorHex = String.format("#%02X%02X%02X",
                        nicknameColor.getRed(), nicknameColor.getGreen(), nicknameColor.getBlue());

                // Преобразуем форматированный текст в HTML
                String messageHTML = convertFormattedTextToHTML(formattedText);

                // Создаем HTML для отображения с цветным жирным ником
                String htmlMessage = String.format(
                        "<div style='margin: 2px 0;'><b style='color: %s;'>%s:</b> %s</div>",
                        colorHex,
                        escapeHTML(nickname),
                        messageHTML
                );

                // Используем правильный метод добавления HTML
                appendHTMLToLog(htmlMessage);

            } catch (Exception ex) {
                ex.printStackTrace();
                appendToLog(message, false);
            }
        });
    }

    private String convertFormattedTextToHTML(String formattedText) {
        StringBuilder result = new StringBuilder();
        Stack<String> openTags = new Stack<>();

        int i = 0;
        while (i < formattedText.length()) {
            char c = formattedText.charAt(i);

            if (c == '[' && i + 1 < formattedText.length()) {
                // Нашли начало тега
                int endTag = formattedText.indexOf(']', i);
                if (endTag == -1) {
                    result.append(escapeHTML(String.valueOf(c)));
                    i++;
                    continue;
                }

                String tagContent = formattedText.substring(i + 1, endTag);

                // Обрабатываем тег
                if (tagContent.equals("B")) {
                    result.append("<b>");
                    openTags.push("b");
                } else if (tagContent.equals("/B")) {
                    result.append("</b>");
                } else if (tagContent.equals("I")) {
                    result.append("<i>");
                    openTags.push("i");
                } else if (tagContent.equals("/I")) {
                    result.append("</i>");
                } else if (tagContent.equals("S")) {
                    result.append("<s>");
                    openTags.push("s");
                } else if (tagContent.equals("/S")) {
                    result.append("</s>");
                } else if (tagContent.equals("/C")) {
                    result.append("</span>");
                } else if (tagContent.startsWith("C#") && tagContent.length() == 8) {
                    // Цветной тег, пример: [C#FF0000]
                    String colorCode = tagContent.substring(2);
                    result.append("<span style='color:#").append(colorCode).append(";'>");
                    openTags.push("span");
                } else {
                    // Неизвестный тег или опечатка - добавляем как текст
                    result.append("[").append(escapeHTML(tagContent)).append("]");
                }

                i = endTag + 1;
            } else {
                // Обычный текст
                result.append(escapeHTML(String.valueOf(c)));
                i++;
            }
        }

        // Закрываем все незакрытые теги
        while (!openTags.isEmpty()) {
            String tag = openTags.pop();
            result.append("</").append(tag).append(">");
        }

        return result.toString();
    }

    private String escapeHTML(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;")
                .replace("\n", "<br/>");
    }

    private void appendHTMLToLog(String html) {
        try {
            Document doc = log.getDocument();
            int length = doc.getLength();

            // Вставляем HTML в конец документа
            HTMLEditorKit kit = (HTMLEditorKit) log.getEditorKit();
            kit.insertHTML((HTMLDocument) doc, length, html, 0, 0, null);

            // Прокручиваем вниз
            log.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void appendToLog(String text, boolean isHTML) {
        try {
            if (isHTML) {
                appendHTMLToLog(text);
            } else {
                String htmlText = "<div>" + escapeHTML(text) + "</div>";
                appendHTMLToLog(htmlText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMessage("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        appendFormattedMessage(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMessage("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, IOException e) {
        printMessage("Connection exception: " + e);
    }

    private synchronized void printMessage(String message) {
        SwingUtilities.invokeLater(() -> {
            appendToLog(message, false);
        });
    }
}
