package gui.menu;

import gui.LocaleChangeListener;
import gui.Localization;
import gui.MainApplicationFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Locale;
import javax.swing.*;

import log.Logger;

public class MenuBar implements LocaleChangeListener {
    public JMenuBar menuBar;
    public MainApplicationFrame mainFrame;
    private final JMenu lookAndFeel;
    private final JMenu testMenu;
    private final JMenu optionsMenu;
    private JMenuItem systemLook;
    private JMenuItem crossplatphormLook;
    private JMenuItem closeItem;
    private JMenuItem languageItem;
    private JMenuItem messageItem;

    public MenuBar(MainApplicationFrame frame) {
        menuBar = new JMenuBar();
        mainFrame = frame;
        lookAndFeel = createLookAndFeelMenu(frame);
        testMenu = createTestMenu();
        optionsMenu = createOptionsMenu(frame);
        menuBar.add(lookAndFeel);
        menuBar.add(testMenu);
        menuBar.add(optionsMenu);
    }

    private JMenu createLookAndFeelMenu(MainApplicationFrame frame) {
        JMenu lookAndFeelMenu = new JMenu(Localization.getValue("mode"));
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                Localization.getValue("modeDescription"));

        {
            JMenuItem systemLookAndFeel = new JMenuItem(Localization.getValue("system"), KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName(), frame);
                frame.invalidate();
            });
            systemLook = systemLookAndFeel;
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem(Localization.getValue("universal"), KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName(), frame);
                frame.invalidate();
            });
            crossplatphormLook = crossplatformLookAndFeel;
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }
        return lookAndFeelMenu;
    }

    private void setLookAndFeel(String className, MainApplicationFrame frame)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(frame);
        }
        catch (ClassNotFoundException | InstantiationException
                | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }

    private JMenu createOptionsMenu(MainApplicationFrame frame) {
        JMenu optionsMenu = new JMenu(Localization.getValue("operations"));
        optionsMenu.setMnemonic(KeyEvent.VK_M);

        JMenuItem close = new JMenuItem(Localization.getValue("exit"), KeyEvent.VK_C);
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = MainApplicationFrame.getConfirmOfClosing();
                if (confirm == 0) { System.exit(0); }
            }
        });
        closeItem = close;
        JMenu language = new JMenu(Localization.getValue("language"));
        languageItem = language;
        JMenuItem languageRu = new JMenuItem("Русский", KeyEvent.VK_C);
        languageRu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Locale locale = new Locale("ru", "RU");
                Localization.setLocale(locale);
                frame.onLocaleChange();
            }
        });
        JMenuItem languageEng = new JMenuItem("English", KeyEvent.VK_C);
        languageEng.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Locale locale = new Locale("en", "US");
                Localization.setLocale(locale);
                frame.onLocaleChange();
            }
        });
        language.add(languageRu);
        language.add(languageEng);
        optionsMenu.add(close);
        optionsMenu.add(language);
        return optionsMenu;
    }

    private JMenu createTestMenu() {
        JMenu testMenu = new JMenu(Localization.getValue("tests"));
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                Localization.getValue("testsDescription"));

        {
            JMenuItem addLogMessageItem = new JMenuItem(Localization.getValue("message"), KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("newString");
            });
            messageItem = addLogMessageItem;
            testMenu.add(addLogMessageItem);
        }
        return testMenu;
    }

    @Override
    public void onLocaleChange() {
        lookAndFeel.setText(Localization.getValue("mode"));
        optionsMenu.setText(Localization.getValue("operations"));
        testMenu.setText(Localization.getValue("tests"));
        systemLook.setText(Localization.getValue("system"));
        crossplatphormLook.setText(Localization.getValue("universal"));
        closeItem.setText(Localization.getValue("exit"));
        languageItem.setText(Localization.getValue("language"));
        messageItem.setText(Localization.getValue("message"));
    }
}